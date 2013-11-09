package com.github.sblundy.gradle.dependencymanagement

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Initializes the dependencyManagement plugin in a {@link Project}.
 *
 * The plugin is added to a project normally, like so:
 *
 * <pre><code>
 *   apply plugin: 'dependencyManagement'
 * </code></pre>
 */
class DependencyManagementPlugin implements Plugin<Project> {
  @Override
  void apply(final Project project) {
    def db = new DefinitionDatabase()
    project.extensions.create('dependencyManagement', DependencyManagementExtension, project, db)
    project.convention.create('dependencyFinder', DependencyManager, project, db)
  }
}