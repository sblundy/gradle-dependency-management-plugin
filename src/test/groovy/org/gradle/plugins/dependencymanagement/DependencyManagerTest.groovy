package org.gradle.plugins.dependencymanagement

import groovy.mock.interceptor.MockFor
import org.apache.commons.lang.RandomStringUtils
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency
import org.gradle.api.plugins.ExtensionContainer
import org.junit.Assert
import org.junit.Test
/**
 */
class DependencyManagerTest extends org.unitils.UnitilsJUnit4 {
  @Test
  void findsDependencyInCurrentIfPresent() {
    DependencyManagementExtension extension = new DependencyManagementExtension()
    extension.dependency(dependencyDef)

    def mocks = newMocks()

    mocks.projectMock.demand.getExtensions { mocks.extensionsMock.proxyInstance() }
    mocks.extensionsMock.demand.findByType { extension }
    mocks.projectMock.demand.getDependencies { mocks.dependenciesMock.proxyInstance() }
    mocks.dependenciesMock.demand.create(dependencyDef) { dependency }

    def sut = new DependencyManager(mocks.projectMock.proxyInstance())
    def output = sut.lookup(testData.groupId, testData.artifactId)

    assert output
    Assert.assertSame(dependency, output)
  }

  @Test(expected = IllegalArgumentException)
  void throwsExceptionWhenNotFound() {
    DependencyManagementExtension extension = new DependencyManagementExtension()
    extension.dependency('not-' + dependencyDef)

    def mocks = newMocks()

    mocks.projectMock.demand.getExtensions { mocks.extensionsMock.proxyInstance() }
    mocks.extensionsMock.demand.findByType { extension }
    mocks.projectMock.demand.getParent { null }

    def sut = new DependencyManager(mocks.projectMock.proxyInstance())
    sut.lookup(testData.groupId, testData.artifactId)
  }

  @Test
  void findsDependencyInParentIfPresent() {
    DependencyManagementExtension currentExtension = new DependencyManagementExtension()
    DependencyManagementExtension parentExtension = new DependencyManagementExtension()
    parentExtension.dependency(dependencyDef)

    def mocks = newMocks()
    def parentMocks = newMocks()

    mocks.projectMock.demand.getExtensions { mocks.extensionsMock.proxyInstance() }
    mocks.extensionsMock.demand.findByType { currentExtension }
    mocks.projectMock.demand.getParent { parentMocks.projectMock.proxyInstance() }
    parentMocks.projectMock.demand.getExtensions { parentMocks.extensionsMock.proxyInstance() }
    parentMocks.extensionsMock.demand.findByType { parentExtension }
    mocks.projectMock.demand.getDependencies { mocks.dependenciesMock.proxyInstance() }
    mocks.dependenciesMock.demand.create(dependencyDef) { dependency }

    def sut = new DependencyManager(mocks.projectMock.proxyInstance())
    def output = sut.lookup(testData.groupId, testData.artifactId)

    assert output
    Assert.assertSame(dependency, output)
  }

  @Test
  void skipsParentIfExtensionNotPresent() {
    DependencyManagementExtension currentExtension = new DependencyManagementExtension()
    DependencyManagementExtension grandParentExtension = new DependencyManagementExtension()
    grandParentExtension.dependency(dependencyDef)

    def mocks = newMocks()
    def parentMocks = newMocks()
    def grandParentMocks = newMocks()

    mocks.projectMock.demand.getExtensions { mocks.extensionsMock.proxyInstance() }
    mocks.extensionsMock.demand.findByType { currentExtension }
    mocks.projectMock.demand.getParent { parentMocks.projectMock.proxyInstance() }
    parentMocks.projectMock.demand.getExtensions { parentMocks.extensionsMock.proxyInstance() }
    parentMocks.extensionsMock.demand.findByType { null }
    parentMocks.projectMock.demand.getParent { grandParentMocks.projectMock.proxyInstance() }
    grandParentMocks.projectMock.demand.getExtensions { grandParentMocks.extensionsMock.proxyInstance() }
    grandParentMocks.extensionsMock.demand.findByType { grandParentExtension }
    mocks.projectMock.demand.getDependencies { mocks.dependenciesMock.proxyInstance() }
    mocks.dependenciesMock.demand.create(dependencyDef) { dependency }

    def sut = new DependencyManager(mocks.projectMock.proxyInstance())
    def output = sut.lookup(testData.groupId, testData.artifactId)

    assert output
    Assert.assertSame(dependency, output)
  }

  private static newMocks() {
    [
            dependenciesMock: new MockFor(DependencyHandler),
            projectMock : new MockFor(Project),
            extensionsMock : new MockFor(ExtensionContainer)
    ]
  }
  private final testData = [
          groupId: RandomStringUtils.randomAlphabetic(5),
          artifactId: RandomStringUtils.randomAlphabetic(6),
          version: RandomStringUtils.randomNumeric(1) + '.' + RandomStringUtils.randomNumeric(1)
  ]
  private final dependencyDef = testData.groupId + ':' + testData.artifactId + ':' + testData.version
  private final dependency =
    new DefaultExternalModuleDependency(testData.groupId, testData.artifactId, testData.version)
}
