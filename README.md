# coveralls-gradle-plugin v0.6.6

[![Build Status](https://travis-ci.org/kt3k/coveralls-gradle-plugin.png?branch=master)](https://travis-ci.org/kt3k/coveralls-gradle-plugin) [![Coverage Status](https://coveralls.io/repos/kt3k/coveralls-gradle-plugin/badge.png)](https://coveralls.io/r/kt3k/coveralls-gradle-plugin)

> Send coverage data to coveralls.io.

## Usage

### use with *cobertura* reporter

Add following lines to build.gradle:

```groovy
apply plugin: 'cobertura'
apply plugin: 'com.github.kt3k.coveralls'

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath 'net.saliman:gradle-cobertura-plugin:2.0.0' // cobertura plugin
        classpath 'org.kt3k.gradle.plugin:coveralls-gradle-plugin:0.6.6'
    }
}

cobertura.coverageFormats = ['html', 'xml'] // coveralls plugin depends on xml format report
```

And run `coveralls` task after `cobertura` task.

This plugin now supports Travis-CI only. Sample `.travis.yml` looks like following:

```yaml
language: java

jdk:
- oraclejdk7

env:
- TERM=dumb

after_success:
- gradle cobertura coveralls
```

For groovy projects, add a following line to build.gradle:

```groovy
cobertura.coverageSourceDirs = sourceSets.main.groovy.srcDirs
```

### use with *JaCoCo* plugin

Add following lines to build.gradle:

```groovy
apply plugin: 'jacoco'
apply plugin: 'com.github.kt3k.coveralls'

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath 'org.kt3k.gradle.plugin:coveralls-gradle-plugin:0.3.0'
    }
}

jacocoTestReport {
    reports {
        xml.enabled = true // coveralls plugin depends on xml format report
        html.enabled = true
    }
}

```

Sample `.travis.yml` looks like following:

```yaml
language: java

jdk:
- oraclejdk7

env:
- TERM=dumb

after_success:
- gradle jacocoTestReport coveralls
```

### use with Travis-CI Pro & Coveralls Pro

When using Travis-CI Pro, you must provide your Coveralls Pro repo token in the
`COVERALLS_REPO_TOKEN` environment variable in `.travis.yml`.

```yaml
env:
  global:
    - COVERALLS_REPO_TOKEN=mySecRetRepoToken
```

You may also use a secure environment variable to hold `COVERALLS_REPO_TOKEN`
by following the instructions on [docs.travis-ci.com](http://docs.travis-ci.com/user/build-configuration/#Secure-environment-variables).

```yaml
env:
  global:
    - secure: <encrypted string here>
```

## Examples

- https://github.com/mockito/mockito
- https://github.com/gesellix/gradle-debian-plugin
- https://github.com/strawjs/straw-android-plugin


## License

MIT License ( Yoshiya Hinosawa )


## Release History

 * 2014-07-06   v0.6.1   Fixed XML parser behaviours. ([#18](https://github.com/kt3k/coveralls-gradle-plugin/pull/18))
 * 2014-07-02   v0.6.0   Multiple project support for jacoco. ([#13](https://github.com/kt3k/coveralls-gradle-plugin/pull/13))
 * 2014-06-26   v0.5.0   Android plugin support. ([#17](https://github.com/kt3k/coveralls-gradle-plugin/pull/17))
 * 2014-06-21   v0.4.1   Fixed XML parser settings. ([#16](https://github.com/kt3k/coveralls-gradle-plugin/pull/16))
 * 2014-03-15   v0.3.1   Upgraded HttpBuilder to v0.7.1. ([#8](https://github.com/kt3k/coveralls-gradle-plugin/pull/8))
 * 2014-03-11   v0.3.0   Configurable coverage report path. ([#7](https://github.com/kt3k/coveralls-gradle-plugin/pull/7))
 * 2014-02-19   v0.2.5   Travis Pro support. ([#4](https://github.com/kt3k/coveralls-gradle-plugin/pull/4))
 * 2014-01-21   v0.2.4   Ignore absent source files. ([#3](https://github.com/kt3k/coveralls-gradle-plugin/pull/3))
 * 2013-12-09   v0.2.1   JaCoCo support. ([#2](https://github.com/kt3k/coveralls-gradle-plugin/pull/2))
 * 2013-11-02   v0.1.6   Available at Maven Central.
 * 2013-10-27   v0.1.5   Multiple `<source>` tag support. ([#1](https://github.com/kt3k/coveralls-gradle-plugin/pull/1))
 * 2013-10-08   v0.1.3   The first usable version.
