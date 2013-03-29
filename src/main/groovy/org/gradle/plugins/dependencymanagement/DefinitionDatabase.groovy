package org.gradle.plugins.dependencymanagement

import org.gradle.api.artifacts.Dependency

/**
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

  Dependency findDependency(Map<String, String> coordinates) {
    def groupDefs = definitions.get(coordinates.group)

    if (null == groupDefs) {
      null
    } else {
      groupDefs.get(coordinates.name)
    }
  }
}
