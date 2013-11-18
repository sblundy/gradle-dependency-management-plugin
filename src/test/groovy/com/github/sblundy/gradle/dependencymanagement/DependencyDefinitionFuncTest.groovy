package com.github.sblundy.gradle.dependencymanagement

import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert
import org.junit.Test

/**
 */
class DependencyDefinitionFuncTest {
  @Test
  void stringNotationSupported() {
    def project = ProjectBuilder.builder().build()
    project.configure(project) {
      apply plugin: 'dependency-management'

      dependencyManagement {
        dependency('log4j:log4j:1.2.17')
      }
    }

    def output = project.dependencyFinder.findDependency([group: 'log4j', name: 'log4j'])
    Assert.assertNotNull(output)
    Assert.assertEquals('1.2.17', output.version)
  }

  @Test
  void mapNotationSupported() {
    def project = ProjectBuilder.builder().build()
    project.configure(project) {
      apply plugin: 'dependency-management'

      dependencyManagement {
        dependency(group: 'log4j', name: 'log4j', version: '1.2.17')
      }
    }

    def output = project.dependencyFinder.findDependency([group: 'log4j', name: 'log4j'])
    Assert.assertNotNull(output)
    Assert.assertEquals('1.2.17', output.version)
  }

  @Test
  void closuresSupported() {
    def project = ProjectBuilder.builder().build()
    project.configure(project) {
      apply plugin: 'dependency-management'

      dependencyManagement {
        dependency('log4j:log4j:1.2.17') {
          transitive = false
        }
      }
    }

    def output = project.dependencyFinder.findDependency([group: 'log4j', name: 'log4j'])
    Assert.assertFalse(output.isTransitive())
  }
}
