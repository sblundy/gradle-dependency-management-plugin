package com.github.sblundy.gradle.dependencymanagement

import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency

/**
 * Write interface for some recorder of dependency lookups. Used during configuration to add a dependency configuration
 * for later use in a build dependency configuration. Does not actually add it to any build {@link Configuration}.
 */
interface DefinitionStore {
  void addDefinition(Dependency dependency)
}