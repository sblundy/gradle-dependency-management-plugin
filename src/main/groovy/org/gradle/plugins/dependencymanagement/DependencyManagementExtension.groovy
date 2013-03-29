package org.gradle.plugins.dependencymanagement

import org.gradle.api.Project

/**
 */
class DependencyManagementExtension {
  private final Project project
  private final DefinitionStore store

  DependencyManagementExtension(Project project, DefinitionStore store) {
    this.project = project
    this.store = store
  }

  def dependency(String dependencyNotation) {
    def dependency = project.dependencies.create(dependencyNotation)
    store.addDefinition(dependency)
  }

  def dependency(Object dependencyNotation, Closure configureClosure) {
    def dependency = project.dependencies.create(dependencyNotation, configureClosure)
    store.addDefinition(dependency)
  }
}
