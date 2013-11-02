coveralls-gradle-plugin [![Build Status](https://travis-ci.org/kt3k/coveralls-gradle-plugin.png?branch=master)](https://travis-ci.org/kt3k/coveralls-gradle-plugin) [![Coverage Status](https://coveralls.io/repos/kt3k/coveralls-gradle-plugin/badge.png)](https://coveralls.io/r/kt3k/coveralls-gradle-plugin)
=======================

Post Cobertura coverage data to coveralls.io.

## Usage

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
        classpath 'net.saliman:gradle-cobertura-plugin:2.0.0' // coveralls plugin depends on cobertura plugin
        classpath 'org.kt3k.gradle.plugin:coveralls-gradle-plugin:0.1.6'
    }   
}

apply plugin: 'cobertura'
apply plugin: 'coveralls'

cobertura.coverageFormats = ['html', 'xml'] // coveralls plugin depends on xml format report
```

And run `gradle coveralls` after `gradle cobertura`

This plugin now supports Travis-CI only. Sample `.travis.yml` looks like following:

```yaml
language: java

jdk:
- oraclejdk7

env:
- TERM=dumb

before_install:
# use Gradle 1.7 
- wget http://services.gradle.org/distributions/gradle-1.7-bin.zip
- unzip gradle-1.7-bin.zip
- export GRADLE_HOME=$PWD/gradle-1.7
- export PATH=$GRADLE_HOME/bin:$PATH

after_success:
- gradle cobertura
- gradle coveralls
```

For groovy projects, add a following line to build.gradle:

```groovy
cobertura.coverageSourceDirs = sourceSets.main.groovy.srcDirs
```

## Example

see https://github.com/strawjs/straw-android-plugin


## License

MIT License ( Yoshiya Hinosawa )


## Release History

 * 2013-11-02   v0.1.6   Changed distribution repository from Github to Maven central.
 * 2013-10-27   v0.1.5   Fixed the case of multiple `<source>` tags.
