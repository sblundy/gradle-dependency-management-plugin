package org.sblundy.gradle.dependencymanagement

import org.gradle.api.artifacts.Dependency

/**
 * Interface of the project component that locates a previously configured dependency manager configuration. Used when
 * configuring build configurations to add a specific dependency to it.
 */
interface DefinitionFinder {
  Dependency getDependency(String group, String name)
  Dependency getDependency(Map<String, String> coordinates)
  Dependency findDependency(Map<String, String> coordinates)
}