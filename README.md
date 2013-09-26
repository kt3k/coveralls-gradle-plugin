coveralls-gradle-plugin [![Build Status](https://travis-ci.org/kt3k/coveralls-gradle-plugin.png?branch=master)](https://travis-ci.org/kt3k/coveralls-gradle-plugin) [![Coverage Status](https://coveralls.io/repos/kt3k/groovy-coveralls-post/badge.png)](https://coveralls.io/r/kt3k/groovy-coveralls-post)
=======================

Convert Cobertura data and post to coveralls.io.

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
        classpath 'org.kt3k:coveralls-gradle-plugin:0.1.0'
    }   
}

apply plugin: 'cobertura'
apply plugin: 'coveralls'

cobertura.coverageFormats = ['html', 'xml'] // coveralls plugin depends on xml format report
```

And run `gradle coveralls` after `gradle cobertura`

This plugin is now supporting travis-ci only. Sample `.travis.yml` look like following:

```yaml
language: java

jdk:
- oraclejdk7

after_success:
- gradle -q cobertura
- gradle -q coveralls
```
