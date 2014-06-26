# coveralls-gradle-plugin v0.5.0

[![Build Status](https://travis-ci.org/kt3k/coveralls-gradle-plugin.png?branch=master)](https://travis-ci.org/kt3k/coveralls-gradle-plugin) [![Coverage Status](https://coveralls.io/repos/kt3k/coveralls-gradle-plugin/badge.png)](https://coveralls.io/r/kt3k/coveralls-gradle-plugin)

> Send coverage data to coveralls.io.

## Usage

### use with *cobertura* reporter

Add following lines to build.gradle:

```groovy
apply plugin: 'cobertura'
apply plugin: 'coveralls'

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath 'net.saliman:gradle-cobertura-plugin:2.0.0' // cobertura plugin
        classpath 'org.kt3k.gradle.plugin:coveralls-gradle-plugin:0.5.0'
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
apply plugin: 'coveralls'

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

 * 2014-06-26   v0.5.0   Add Android plugin support ([#17](https://github.com/kt3k/coveralls-gradle-plugin/pull/17))
 * 2014-06-21   v0.4.1   Change some XML parser settings ([#16](https://github.com/kt3k/coveralls-gradle-plugin/pull/16))
 * 2014-03-15   v0.3.1   Upgrade HttpBuilder to v0.7.1. (issue #8)
 * 2014-03-11   v0.3.0   Make coverage report paths configurable. (issue #6)
 * 2014-02-19   v0.2.5   Added the support of Travis Pro. (issue #4, via @dhalperi)
 * 2014-01-21   v0.2.4   Fixed the case of absent source files. (issue #3)
 * 2013-12-09   v0.2.1   Added JaCoCo support. (via @ihiroky)
 * 2013-11-02   v0.1.6   Changed distribution repository from Github to Maven central.
 * 2013-10-27   v0.1.5   Fixed the case of multiple `<source>` tags. (via @bric3)
