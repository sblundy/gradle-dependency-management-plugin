package org.gradle.plugins.dependencymanagement

import org.gradle.api.artifacts.Dependency

/**
 */
interface DefinitionFinder {
  Dependency getDependency(String group, String name)
  Dependency getDependency(Map<String, String> coordinates)
  Dependency findDependency(Map<String, String> coordinates)
}