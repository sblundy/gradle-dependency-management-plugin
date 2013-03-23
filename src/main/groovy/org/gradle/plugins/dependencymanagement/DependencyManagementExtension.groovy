package org.gradle.plugins.dependencymanagement

/**
 */
class DependencyManagementExtension {
  protected definitions = [] as Set<String>

  def dependency(String dependencyDefinition) {
    definitions.add(dependencyDefinition)
  }
}
