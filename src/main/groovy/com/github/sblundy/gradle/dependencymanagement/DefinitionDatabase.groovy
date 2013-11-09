package com.github.sblundy.gradle.dependencymanagement

import org.gradle.api.artifacts.Dependency

/**
 * Store for dependency available configurations for a particular project.
 *
 * Instances are hierarchical when defined multiple projects in a multi-project build. This enables overriding and
 * extending dependencyManagement configurations.
 */
class DefinitionDatabase implements DefinitionLookup, DefinitionStore {
  private final Map<String, Map<String, Dependency>> definitions = [:]

  void addDefinition(Dependency dependency) {
    Map<String, Dependency> group = definitions.get(dependency.group)

    if (null == group) {
      group = [:]
      definitions.put(dependency.group, group)
    }

    group.put(dependency.name, dependency)

  }

  /**
   * Finds a dependency matching the query coordinates. If this instance does not have the desired configuration, it
   * will search it's parents also, querying the first instance it finds.
   * @return The first dependency it finds, or <code>null</code> if none is found
   */
  Dependency findDependency(Map<String, String> coordinates) {
    def groupDefs = definitions.get(coordinates.group)

    if (null == groupDefs) {
      null
    } else {
      groupDefs.get(coordinates.name)
    }
  }
}
