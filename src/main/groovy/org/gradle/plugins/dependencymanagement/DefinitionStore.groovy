package org.gradle.plugins.dependencymanagement

import org.gradle.api.artifacts.Dependency

/**
 */
interface DefinitionStore {
  void addDefinition(Dependency dependency)
}