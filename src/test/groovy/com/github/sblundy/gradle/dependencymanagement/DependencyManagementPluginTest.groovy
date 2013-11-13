package com.github.sblundy.gradle.dependencymanagement

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert
import org.junit.Test

class DependencyManagementPluginTest {
  @Test
  public void addsManagedMethodToDependencyBlock() {
    Project project = ProjectBuilder.builder().build()
    project.configure(project) {
      apply plugin: DependencyManagementPlugin
    }

    Assert.assertFalse(project.dependencies.metaClass.respondsTo(project.dependencies, 'managed', Map).empty)
  }

  @Test
  public void managedMethodLooksUpDependency() {
    Project project = ProjectBuilder.builder().build()
    project.configure(project) {
      apply plugin: DependencyManagementPlugin

      dependencyManagement {
        dependency('log4j:log4j:1.2.9')
      }

      configurations {
        compile
      }

      dependencies {
        compile managed(group: 'log4j', name: 'log4j')
      }
    }

    def log4jDeps = project.configurations.getByName('compile').dependencies.findAll { it.name == 'log4j' }
    Assert.assertFalse(log4jDeps.empty)
  }
}
