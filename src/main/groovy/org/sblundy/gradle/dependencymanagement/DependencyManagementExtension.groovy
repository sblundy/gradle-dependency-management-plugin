package org.sblundy.gradle.dependencymanagement

import org.gradle.api.Project

/**
 * Build script interface to configure the dependencyManagement system, which mostly consists of adding dependencies for
 * later lookup.
 *
 * It is used in the build script like any other extension:
 *
 * <pre><code>
 *   dependencyManagement {
 *     dependency('org.example:example-core:1.2.3')
 *   }
 * </code></pre>
 *
 * You can provide a configuration closure as in a normal dependency configuration
 *
 * <pre><code>
 *   dependencyManagement {
 *     dependency('org.example:example-core:1.2.3') {
 *       exclude module: 'example-optional'
 *     }
 *   }
 * </code></pre>
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
