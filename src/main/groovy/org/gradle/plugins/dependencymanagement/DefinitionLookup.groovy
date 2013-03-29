package org.gradle.plugins.dependencymanagement

import org.gradle.api.artifacts.Dependency

/**
 */
interface DefinitionLookup {
  Dependency findDependency(Map<String, String> coordinates)
}