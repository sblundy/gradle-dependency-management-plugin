package org.sblundy.gradle.dependencymanagement

import groovy.mock.interceptor.MockFor
import org.apache.commons.lang.RandomStringUtils
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.junit.Assert
import org.junit.Test

/**
 */
class DependencyManagementExtensionTest {
  private final projectMock = new MockFor(Project)
  private final storeMock = new MockFor(DefinitionStore)
  private final coordinates = DummyDependencyUtils.randomCoordinates()

  @Test
  void callsCreateAndStoresDependencyDefinition() {
    def dependenciesMock = new MockFor(DependencyHandler)
    def dependency = dummyDependency()
    def dependencyDef = RandomStringUtils.randomAlphabetic(16)
    String createArgument = null

    projectMock.demand.getDependencies { dependenciesMock.proxyInstance() }
    dependenciesMock.demand.create { d -> createArgument = d; dependency }
    storeMock.demand.addDefinition(dependency) { }

    def sut = newSut()
    sut.dependency(dependencyDef)

    Assert.assertEquals(dependencyDef, createArgument)
  }

  @Test
  void callsCreateWithClosureAndStoresDependencyDefinition() {
    def dependenciesMock = new MockFor(DependencyHandler)
    def dependency = dummyDependency()
    def dependencyDef = RandomStringUtils.randomAlphabetic(16)
    def createArgs = [ : ]

    projectMock.demand.getDependencies { dependenciesMock.proxyInstance() }
    dependenciesMock.demand.create { str, closure -> createArgs[str] = closure; dependency }
    storeMock.demand.addDefinition(dependency) { }

    def sut = newSut()
    sut.dependency(dependencyDef) { exclude module: 'exclusion' }

    Assert.assertNotNull(createArgs[dependencyDef])
  }

  private Dependency dummyDependency() {
    DummyDependencyUtils.dummyDependency(coordinates)
  }

  @SuppressWarnings('GroovyAssignabilityCheck')
  private DependencyManagementExtension newSut() {
    new DependencyManagementExtension(projectMock.proxyInstance(), storeMock.proxyInstance())
  }
}
