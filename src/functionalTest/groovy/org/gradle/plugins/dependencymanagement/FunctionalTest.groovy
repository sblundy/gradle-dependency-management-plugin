package org.gradle.plugins.dependencymanagement

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

/**
 */
class FunctionalTest {
  @Test(timeout = 10000L)
  def void subProjectFindsDependencyInParent() {
    def builder = new TestProjectBuilder()
    builder.configureRoot {
      apply plugin: 'dependencyManagement'

      dependencyManagement {
        dependency('log4j:log4j:1.2.17')
      }
    }

    builder.add('subProject') {
      apply plugin: 'dependencyManagement'

      configurations {
        compile
      }

      dependencies {
        compile dependencyFinder.getDependency(group: 'log4j', name: 'log4j')
      }
    }

    builder.assertContainsDependency('subProject', 'compile', 'log4j', '1.2.17')
  }

  @Test(timeout = 10000L)
  def void dependencyClosureConfigInherited() {
    def builder = new TestProjectBuilder()
    builder.configureRoot {
      apply plugin: 'dependencyManagement'

      dependencyManagement {
        dependency('org.springframework:spring-core:3.2.2.RELEASE') {
          exclude module: 'aspectjweaver'
        }
      }
    }


    builder.add('subProject') {
      repositories {
        mavenCentral()
      }

      apply plugin: 'dependencyManagement'

      configurations {
        withClosure
      }

      dependencies {
        withClosure dependencyFinder.getDependency('org.springframework', 'spring-core')
      }
    }


    builder.assertContainsDependency('subProject', 'withClosure', 'spring-core', '3.2.2.RELEASE')
    List<File> withClosureFiles = builder.resolveDependencies('subProject', 'withClosure')
    assert withClosureFiles.any { file -> file.name.contains('commons-logging') }
    assert !withClosureFiles.any { file -> file.name.contains('aspectjweaver') }
  }

  class TestProjectBuilder {
    Map<String, Project> subProjects = [:]
    Project root = ProjectBuilder.builder().withName('root-project').build()

    TestProjectBuilder() {
      root.configure(root) {
        allprojects {
          repositories {
            mavenCentral()
          }
        }
      }
    }

    def add(String name, Closure configuration) {
      Project sub = ProjectBuilder.builder().withName(name).withParent(root).build()
      sub.configure(sub, configuration)
      subProjects[(name)] = sub
    }

    def configureRoot(Closure c) {
      root.configure(root, c)
    }

    List<File> resolveDependencies(String projectName, String configName) {
      subProjects[projectName].configurations.getByName(configName).resolve().toList()
    }

    void assertContainsDependency(String projectName, String configName,
                                  String expectedDependencyName, String expectedVersion) {
      assert subProjects[projectName].configurations.getByName(configName).dependencies.matching {
        dep -> dep.name == expectedDependencyName && dep.version == expectedVersion
      }
    }
  }
}
