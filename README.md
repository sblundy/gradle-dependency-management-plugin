gradle-dependency-management-plugin
===================================

Bringing centralized dependency management to Gradle multi-module projects. Copying the same dependency definition is multiple modules is poor engineering. But Gradle doesn't have a built in mechanism for defining the specifics of a dependency once and then pulling it into a module on demand by identity. 

Yes, this is shamelessly lifted from Maven. But these plugin will aim to be an improvement, both in terms of shedding odd Mavenisms and improved usefulness and clarity. 

Usage
-----------------------------------

The dependency definitions are defined in the `dependencyManagement` block. Normally, it'll be in the root project, but it can be in any of the user module's parents, even the user module itself.

*$PROJECT_HOME/build.gradle*
```groovy
apply plugin: 'dependencyManagement'

dependencyManagement {
  dependency('log4j:log4j:1.2.17')
}
```

Referencing a defined dependency is done normally in the `dependencies` block, by calling `dependencyManager.lookup(<group>, <name>)`

*$PROJECT_HOME/module/build.gradle*
```groovy
apply plugin: 'dependencyManagement'

dependencies {
  test dependencyManager.lookup('log4j', 'log4j')
}
```
