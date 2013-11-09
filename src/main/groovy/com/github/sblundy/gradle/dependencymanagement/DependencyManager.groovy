package com.github.sblundy.gradle.dependencymanagement

import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency

/**
 * Component injected into the project to lookup dependencies that have been previously added.
 */
class DependencyManager implements DefinitionFinder {
  private final Project project
  private final DefinitionLookup definitionLookup

  DependencyManager(Project project, DefinitionLookup definitionLookup) {
    this.project = project
    this.definitionLookup = definitionLookup
  }

  /**
   * Convenience method for <code>getDependency([group: group, name:  name])</code>
   */
  Dependency getDependency(String group, String name) {
    getDependency([group: group, name:  name])
  }

  /**
   * This is the most commonly used lookup method. Note: It is not guaranteed to point to a real, resolvable, or
   * findable dependency. Your build might still fail.
   * @return The desired dependency configuration. Will not be <code>null</code>.
   * @throws IllegalArgumentException If no dependency configuration is found matching the query
   */
  Dependency getDependency(Map<String, String> coordinates) {
    def dependency = findDependency(coordinates)

    if (null != dependency) {
      dependency
    } else {
      throw new IllegalArgumentException('Dependency not found')
    }
  }

  /**
   *
   * @return The desired dependency configuration, or <code>null</code> if none is found.
   */
  Dependency findDependency(Map<String, String> coordinates) {
    def dependency = definitionLookup.findDependency(coordinates)
    if (null != dependency) {
      return dependency
    }

    def parentManager = findParentManager()
    null == parentManager ? null : parentManager.findDependency(coordinates)
  }

  private DefinitionFinder findParentManager() {
    def current = project.parent

    while (current != null) {
      def parentManager = current.convention.findByType(DefinitionFinder)
      if (parentManager == null) {
        current = current.parent
      } else {
        return parentManager
      }
    }

    null
  }
}
