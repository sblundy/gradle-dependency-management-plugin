package org.gradle.plugins.dependencymanagement

import org.gradle.api.Plugin
import org.gradle.api.Project

class DependencyManagementPlugin implements Plugin<Project> {
  @Override
  void apply(final Project project) {
    project.extensions.create('dependencyManagement', DependencyManagementExtension)
    project.convention.create('dependencyManager', DependencyManager, project)
  }
}