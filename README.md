gradle-dependency-management-plugin
===================================

Bringing centralized dependency management to Gradle multi-module projects. Copying the same dependency definition is multiple modules is poor engineering. But Gradle doesn't have a built in mechanism for defining the specifics of a dependency once and then pulling it into a module on demand by identity. 

Yes, this is shamelessly lifted from Maven. But these plugin will aim to be an improvement, both in terms of shedding odd Mavenisms and improved usefulness and clarity. 

Usage
-----------------------------------

The dependency definitions are defined in the `dependencyManagement` block. Normally, it'll be in the root project, but it can be in any of the user module's parents, even the user module itself. `dependencyManagement` block in sub-modules have a hierarchical relationship with any in their parents and can be used to overriding individual dependency configurations.

*$PROJECT_HOME/build.gradle*
```groovy
apply plugin: 'dependency-management'

dependencyManagement {
  dependency('org.testng:testng:5.5:jdk15')
}
```

An optional configuration closure can be added.

*$PROJECT_HOME/build.gradle*
```groovy
apply plugin: 'dependency-management'

dependencyManagement {
  dependency('org.testng:testng:5.5:jdk15') {
    transitive = false
  }
}
```

Referencing a defined dependency is done normally in the `dependencies` block, by calling `dependencyFinder.getDependency(<group>, <name>)`

*$PROJECT_HOME/module/build.gradle*
```groovy
apply plugin: 'dependency-management'

dependencies {
  test dependencyFinder.getDependency(group: 'org.testng', name: 'testng')
}
```
