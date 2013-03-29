package org.gradle.plugins.dependencymanagement

import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency

/**
 */
class DependencyManager implements DefinitionFinder {
  private final Project project
  private final DefinitionLookup definitionLookup

  DependencyManager(Project project, DefinitionLookup definitionLookup) {
    this.project = project
    this.definitionLookup = definitionLookup
  }

  Dependency getDependency(String group, String name) {
    getDependency([group: group, name:  name])
  }

  Dependency getDependency(Map<String, String> coordinates) {
    def dependency = findDependency(coordinates)

    if (null != dependency) {
      dependency
    } else {
      throw new IllegalArgumentException('Dependency not found')
    }
  }

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
