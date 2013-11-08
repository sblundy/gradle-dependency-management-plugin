package org.sblundy.gradle.dependencymanagement

import org.gradle.api.artifacts.Dependency

/**
 * The query interface for some recorder of dependency lookups. Any {@link Dependency} returned does not necessarily
 * exist in the build already. Though it may be valid, in that it's well formed, it may not be resolvable.
 */
interface DefinitionLookup {
  Dependency findDependency(Map<String, String> coordinates)
}