package org.gradle.plugins.dependencymanagement

import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency

/**
 */
class DependencyManager {
  private Project project

  DependencyManager(Project project) {
    this.project = project
  }

  def Dependency lookup(String groupId, String artifactId) {
    def dependency = findDependency(project, groupId + ':' + artifactId)

    if (null != dependency) {
      return dependency
    } else {
      throw new IllegalArgumentException("Dependency not found")
    }
  }

  private def Dependency findDependency(Project current, String prefix) {
    def ext = current.extensions.findByType(DependencyManagementExtension.class)

    if (null != ext) {
      for (String definition : ext.definitions) {
        if (definition.startsWith(prefix)) {
          return project.dependencies.create(definition)
        }
      }
    }

    def parent = current.getParent()
    if (null != parent) {
      return findDependency(parent, prefix)
    } else {
      return null
    }
  }
}
