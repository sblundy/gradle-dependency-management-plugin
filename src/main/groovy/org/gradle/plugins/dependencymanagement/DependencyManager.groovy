package org.gradle.plugins.dependencymanagement

import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency

/**
 */
class DependencyManager {
  private final Project project

  DependencyManager(Project project) {
    this.project = project
  }

  Dependency lookup(String groupId, String artifactId) {
    def dependency = findDependency(project, groupId + ':' + artifactId)

    if (null != dependency) {
      dependency
    } else {
      throw new IllegalArgumentException('Dependency not found')
    }
  }

  private Dependency findDependency(Project current, String prefix) {
    def ext = current.extensions.findByType(DependencyManagementExtension)

    if (null != ext) {
      for (String definition : ext.definitions) {
        if (definition.startsWith(prefix)) {
          return project.dependencies.create(definition)
        }
      }
    }

    def parent = current.parent
    if (null != parent) {
      findDependency(parent, prefix)
    } else {
      null
    }
  }
}
