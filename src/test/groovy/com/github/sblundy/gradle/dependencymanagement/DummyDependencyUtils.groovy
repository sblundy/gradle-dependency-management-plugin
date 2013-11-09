package com.github.sblundy.gradle.dependencymanagement

import org.apache.commons.lang.RandomStringUtils
import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency

/**
 */
class DummyDependencyUtils {
  static randomCoordinates() {
    [
          group: RandomStringUtils.randomAlphabetic(5),
          name: RandomStringUtils.randomAlphabetic(6),
          version: RandomStringUtils.randomNumeric(1) + '.' + RandomStringUtils.randomNumeric(1)
    ]
  }

  static dummyDependency(Map coordinates) {
    new DefaultExternalModuleDependency(coordinates.group, coordinates.name, coordinates.version)
  }
}
