coveralls-gradle-plugin [![Build Status](https://travis-ci.org/kt3k/coveralls-gradle-plugin.png?branch=master)](https://travis-ci.org/kt3k/coveralls-gradle-plugin) [![Coverage Status](https://coveralls.io/repos/kt3k/coveralls-gradle-plugin/badge.png)](https://coveralls.io/r/kt3k/coveralls-gradle-plugin)
=======================

Post Cobertura coverage data to coveralls.io.

Usage
-----

Add followings to build.gradle:

```groovy
buildscript {
    repositories {
        mavenCentral()

        maven {
            url 'http://kt3k.github.io/repository/maven/release' // coveralls plugin is hosted on github.io.
        }   
    }   

    dependencies {
        classpath 'net.saliman:gradle-cobertura-plugin:1.1.2' // coveralls plugin depends on cobertura plugin
        classpath 'org.kt3k.gradle.plugin:coveralls-gradle-plugin:0.1.4'
    }   
}

apply plugin: 'cobertura'
apply plugin: 'coveralls'

cobertura.coverageFormats = ['html', 'xml'] // coveralls plugin depends on xml format report
```

And run `gradle coveralls` after `gradle cobertura`

This plugin is now supporting Travis-CI only. Sample `.travis.yml` looks like following:

```yaml
language: java

jdk:
- oraclejdk7

env:
- TERM=dumb

after_success:
- gradle cobertura
- gradle coveralls
```

For groovy projects, add a following line to build.gradle:

```groovy
cobertura.coverageSourceDirs = sourceSets.main.groovy.srcDirs
```

Example
-------

see https://github.com/kt3k/straw-android-plugin
