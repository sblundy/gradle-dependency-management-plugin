package org.gradle.plugins.dependencymanagement

import org.gradle.api.Plugin
import org.gradle.api.Project

class DependencyManagementPlugin implements Plugin<Project> {
  @Override
  void apply(final Project project) {
    def db = new DefinitionDatabase()
    project.extensions.create('dependencyManagement', DependencyManagementExtension, project, db)
    project.convention.create('dependencyFinder', DependencyManager, project, db)
  }
}