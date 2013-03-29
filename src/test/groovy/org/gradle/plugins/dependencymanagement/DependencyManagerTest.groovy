package org.gradle.plugins.dependencymanagement

import groovy.mock.interceptor.MockFor
import org.gradle.api.Project
import org.gradle.api.plugins.Convention
import org.junit.Assert
import org.junit.Test
/**
 */
class DependencyManagerTest {
  @Test
  void findsDependencyInCurrentIfPresent() {
    def mocks = new Mocks()

    mocks.findsDependencyInExtensions()

    def sut = newSut(mocks)
    def output = sut.getDependency(testData)

    assert output
    Assert.assertSame(dependency, output)
  }

  @Test(expected = IllegalArgumentException)
  void throwsExceptionWhenNotFound() {
    def mocks = new Mocks()

    mocks.doesNotFindInLocal()
    mocks.parentReturnsNull()

    def sut = newSut(mocks)
    sut.getDependency(testData)
  }

  @Test
  void findsDependencyInParentIfPresent() {
    def mocks = new Mocks()
    def parentMocks = new Mocks()

    mocks.doesNotFindInLocal()
    mocks.looksUpParentProject(parentMocks)
    parentMocks.findsParentFinder()
    parentMocks.findsDependencyInManager()

    def sut = newSut(mocks)
    def output = sut.getDependency(testData)

    assert output
    Assert.assertSame(dependency, output)
  }

  @Test(expected = IllegalArgumentException)
  void throwsExceptionIfNotInEitherCurrentOrParent() {
    def mocks = new Mocks()
    def parentMocks = new Mocks()

    mocks.doesNotFindInLocal()
    mocks.looksUpParentProject(parentMocks)
    parentMocks.doesNotFindParentFinder()
    parentMocks.parentReturnsNull()

    def sut = newSut(mocks)
    sut.getDependency(testData)
  }

  @Test
  void findsDependencyInGrandParentIfNotInParent() {
    def mocks = new Mocks()
    def parentMocks = new Mocks()
    def grandParentMocks = new Mocks()

    mocks.doesNotFindInLocal()
    mocks.looksUpParentProject(parentMocks)
    parentMocks.doesNotFindParentFinder()
    parentMocks.looksUpParentProject(grandParentMocks)
    grandParentMocks.findsParentFinder()
    grandParentMocks.findsDependencyInManager()

    def sut = newSut(mocks)
    def output = sut.getDependency(testData)

    assert output
    Assert.assertSame(dependency, output)
  }

  @SuppressWarnings('GroovyAssignabilityCheck')
  private static DependencyManager newSut(Mocks mocks) {
    new DependencyManager(mocks.projectMock.proxyInstance(), mocks.lookupMock.proxyInstance())
  }

  private class Mocks {
    def projectMock = new MockFor(Project)
    def lookupMock = new MockFor(DefinitionLookup)
    def conventionMock = new MockFor(Convention)
    def managerMock = new MockFor(DefinitionFinder)

    void findsDependencyInExtensions() {
      lookupMock.demand.findDependency(testData.group, testData.name) { dependency }
    }

    void doesNotFindInLocal() {
      lookupMock.demand.findDependency(testData.group, testData.name) { null }
    }
    
    void parentReturnsNull() {
      projectMock.demand.getParent { null }
    }
    
    void looksUpParentProject(Mocks parentMocks) {
      projectMock.demand.getParent { parentMocks.projectMock.proxyInstance() }
    }

    void findsDependencyInManager() {
      managerMock.demand.findDependency { dependency }
    }

    void findsParentFinder() {
      projectMock.demand.getConvention { conventionMock.proxyInstance() }
      conventionMock.demand.findByType { managerMock.proxyInstance() }
    }

    void doesNotFindParentFinder() {
      projectMock.demand.getConvention { conventionMock.proxyInstance() }
      conventionMock.demand.findByType { null }
    }
  }
  
  private final testData = DummyDependencyUtils.randomCoordinates()

  private final dependency = DummyDependencyUtils.dummyDependency(testData)
}
