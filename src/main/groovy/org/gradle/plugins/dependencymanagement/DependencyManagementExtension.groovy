package org.gradle.plugins.dependencymanagement

/**
 */
class DependencyManagementExtension {
  protected Set<String> definitions = new HashSet<String>()

  def dependency(String dependencyDefinition) {
    definitions.add(dependencyDefinition)
  }
}
