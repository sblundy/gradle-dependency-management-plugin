package com.github.sblundy.gradle.dependencymanagement

import org.gradle.api.artifacts.Dependency
import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency
import org.junit.Test

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic
import static org.apache.commons.lang.RandomStringUtils.randomNumeric
import static org.testng.Assert.assertNull
import static org.testng.Assert.assertSame

/**
 */
class DefinitionDatabaseTest {
  @Test
  void definitionAddedIsFoundByGroupAndName() {
    def coordinates = randomCoordinates()
    Dependency dep = dummyDependency(coordinates)

    def sut = newSut()
    sut.addDefinition(dep)
    def output = sut.findDependency([group: coordinates.group, name: coordinates.name])

    assertSame(output, dep)
  }

  @Test
  void definitionAddedIsFoundByJustName() {
    def coordinates = randomCoordinates()
    Dependency dep = dummyDependency(coordinates)

    def sut = newSut()
    sut.addDefinition(dep)
    def output = sut.findDependency([name: coordinates.name])

    assertSame(output, dep)
  }

  @Test(expected = IllegalArgumentException)
  void exceptionThrownIfJustNameProvidedAndMultipleSpecified() {
    def coordinates = randomCoordinates()
    Dependency dep1 = dummyDependency(coordinates)
    def coordinates2 = [
        group: 'not-' + coordinates.group,
        name: coordinates.name,
        version: coordinates.version
    ]
    Dependency dep2 = dummyDependency(coordinates2)

    def sut = newSut()
    sut.addDefinition(dep1)
    sut.addDefinition(dep2)
    sut.findDependency([name: coordinates.name])
  }

  @Test
  void canDiffSameGroupDifferentName() {
    def coordinates = randomCoordinates()
    def similar = [group: coordinates.group, name: randomAlphabetic(7), version: coordinates.version]
    Dependency dep = dummyDependency(coordinates)
    Dependency two = dummyDependency(similar)

    def sut = newSut()
    sut.addDefinition(dep)
    sut.addDefinition(two)
    def output = sut.findDependency([group: coordinates.group, name: coordinates.name])

    assertSame(output, dep)
  }

  @Test
  void returnsNullIfNotPresent() {
    def coordinates = randomCoordinates()
    Dependency dep = dummyDependency(coordinates)

    def sut = newSut()
    sut.addDefinition(dep)
    def output = sut.findDependency([group: 'not-' + coordinates.group, name: 'not-' + coordinates.name])

    assertNull(output)
  }

  @Test
  void returnsNullEvenWhenGroupsSame() {
    def coordinates = randomCoordinates()
    Dependency dep = dummyDependency(coordinates)

    def sut = newSut()
    sut.addDefinition(dep)
    def output = sut.findDependency([group: coordinates.group, name: 'not-' + coordinates.name])

    assertNull(output)
  }

  @Test
  void returnsNullEvenWhenNamesSame() {
    def coordinates = randomCoordinates()
    Dependency dep = dummyDependency(coordinates)

    def sut = newSut()
    sut.addDefinition(dep)
    def output = sut.findDependency([group: 'not-' + coordinates.group, name: coordinates.name])

    assertNull(output)
  }

  private static Dependency dummyDependency(Map<String, String> coordinates) {
    new DefaultExternalModuleDependency(coordinates.group, coordinates.name, coordinates.version)
  }

  private static DefinitionDatabase newSut() {
    new DefinitionDatabase()
  }

  private static randomCoordinates() {
    [group: randomAlphabetic(5), name: randomAlphabetic(6), version: randomNumeric(1) + '.0']
  }
}
