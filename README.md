# coveralls-gradle-plugin v0.2.1

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
        classpath 'org.kt3k.gradle.plugin:coveralls-gradle-plugin:0.2.1'
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
        classpath 'org.kt3k.gradle.plugin:coveralls-gradle-plugin:0.2.1'
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

## Examples

- https://github.com/mockito/mockito
- https://github.com/gesellix/gradle-debian-plugin
- https://github.com/strawjs/straw-android-plugin


## License

MIT License ( Yoshiya Hinosawa )


## Release History

 * 2013-12-09   v0.2.1   Add JaCoCo support. (via @ihiroky)
 * 2013-11-02   v0.1.6   Changed distribution repository from Github to Maven central.
 * 2013-10-27   v0.1.5   Fixed the case of multiple `<source>` tags. (via @bric3)
