# coveralls-gradle-plugin v2.12.2

[![Java CI](https://github.com/kt3k/coveralls-gradle-plugin/actions/workflows/ci.yml/badge.svg)](https://github.com/kt3k/coveralls-gradle-plugin/actions/workflows/ci.yml)
[![Coverage Status](https://coveralls.io/repos/kt3k/coveralls-gradle-plugin/badge.svg)](https://coveralls.io/r/kt3k/coveralls-gradle-plugin)

> Send coverage data to coveralls.io.

## Usage

### Use with _cobertura_ reporter

Add the following lines to build.gradle:

```groovy
plugins {
    id 'net.saliman.cobertura' version '4.0.0'
    id 'com.github.kt3k.coveralls' version '2.12.2'
}

cobertura.coverageFormats = ['html', 'xml'] // coveralls plugin depends on xml format report
```

And run `coveralls` task after `cobertura` task.

An example `.travis.yml` looks like following:

```yaml
language: java

jdk:
- oraclejdk11

after_success:
- ./gradlew cobertura coveralls
```

For groovy projects, add the following line to build.gradle:

```groovy
cobertura.coverageSourceDirs = sourceSets.main.groovy.srcDirs
```

### Use with _JaCoCo_ plugin

Add the following lines to build.gradle:

```groovy
plugins {
    id 'jacoco'
    id 'com.github.kt3k.coveralls' version '2.12.2'
}

jacocoTestReport {
    reports {
        xml.enabled = true // coveralls plugin depends on xml format report
        html.enabled = true
    }
}
```

An example `.travis.yml` looks like following:

```yaml
language: java

jdk:
- oraclejdk11

after_success:
- ./gradlew jacocoTestReport coveralls
```

Configuration Options

```
coveralls {
    jacocoReportPath 'build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml'
}
```

### Use with [_pitest_](https://github.com/szpak/gradle-pitest-plugin) plugin

Add the following lines to build.gradle:

```groovy
plugins {
    id "info.solidsoft.pitest" version '1.6.0'
    id 'com.github.kt3k.coveralls' version '2.12.2'
}

pitest {
    timestampedReports = false
    outputFormats = ['XML']
}
```

An example `.travis.yml` looks like following:

```yaml
language: java

jdk:
- oraclejdk11

after_success:
- ./gradlew pitest coveralls
```

### Use with Travis-CI Pro & Coveralls Pro

When using Travis-CI Pro, you must provide your Coveralls Pro repo token in the
`COVERALLS_REPO_TOKEN` environment variable in `.travis.yml`.

```yaml
env:
  global:
    - COVERALLS_REPO_TOKEN=mySecRetRepoToken
```

You may also use a secure environment variable to hold `COVERALLS_REPO_TOKEN` by
following the instructions on
[docs.travis-ci.com](http://docs.travis-ci.com/user/build-configuration/#Secure-environment-variables).

```yaml
env:
  global:
    - secure: <encrypted string here>
```

### Use with multi-project build

This plugin only handles one project per build. You need to merge coverage
report before coveralls plugin take it.

See example: https://github.com/ben-manes/caffeine/blob/v2.6.2/build.gradle#L133

### Configuring coveralls "task"

If you configure coveralls "task" (not the extension object), you need to write
as the following:

```
tasks.coveralls {
  dependsOn 'check'
}
```

### CI Services

The following CI services should _automatically_ work:

- [Travis CI](https://travis-ci.org/)
- [CircleCI](https://circleci.com/)
- [Jenkins CI](http://jenkins-ci.org/)
- [Bitrise CI](https://bitrise.io/)

If you need to customize something or support a different CI service, you can
configure environment variables:

- `CI_NAME`
- `CI_BUILD_NUMBER`
- `CI_BUILD_URL`
- `CI_BRANCH`
- `CI_PULL_REQUEST`
- `COVERALLS_REPO_TOKEN`

#### Supported CI names

- Travis-CI: `travis-ci`
- Travis-Pro: `travis-pro`
- CircleCI: `circleci`
- Jenkins: `jenkins`
- Snap CI: `snapci`
- Bitrise CI: `bitrise`

If you have `COVERALLS_REPO_TOKEN` set and you're using Travis-CI not
Travis-Pro, you need to set `CI_NAME=travis-ci`.

### HTTPS proxy settings

You can set https proxy if needed. Environment variables to configure:

- `https.proxyHost`
- `https.proxyPort` (default value is **443**)

## Examples

- https://github.com/strawjs/straw-android

## License

MIT License ( Yoshiya Hinosawa )

## Note

- The ID of the plugin has been changed from `coveralls` to
  `com.github.kt3k.coveralls` according to
  [the guidelines of gradle plugin](http://plugins.gradle.org/submit). Please
  see the examples below for details. (2014/07/21)

## See also

- https://codecov.io/
  - An alternative web service to coveralls.
    - Better API design, which accepts any of major formats.
    - Nice browser extensions - You can see coverage information in GitHub UI.

## Release History

- 2023-03-07 v2.12.2 Better error message.
  ([#114](https://github.com/kt3k/coveralls-gradle-plugin/pull/114))
- 2021-04-07 v2.12.0 Added Drone support.
  ([#111](https://github.com/kt3k/coveralls-gradle-plugin/pull/111))
- 2020-08-07 v2.10.2 Fix path handling of JaCoCo report.
  ([#108](https://github.com/kt3k/coveralls-gradle-plugin/pull/108))
- 2020-02-28 v2.10.1 Upgrade httpmime dependency.
  ([#104](https://github.com/kt3k/coveralls-gradle-plugin/pull/104))
- 2020-02-16 v2.10.0 Add branch detection.
  ([#102](https://github.com/kt3k/coveralls-gradle-plugin/pull/102))
- 2019-12-11 v2.9.0 Add GitHub Actions support.
  ([#99](https://github.com/kt3k/coveralls-gradle-plugin/pull/99))
- 2019-05-25 v2.8.4 Maintainance update (refactored).
- 2017-10-17 v2.8.2 Fixed PR ID in Jenkins.
  ([#79](https://github.com/kt3k/coveralls-gradle-plugin/pull/79))
- 2017-02-08 v2.8.1 Fixed PIT report.
  ([#73](https://github.com/kt3k/coveralls-gradle-plugin/pull/73))
- 2017-02-06 v2.8.0 Added [PIT](http://pitest.org/) support.
  ([#71](https://github.com/kt3k/coveralls-gradle-plugin/pull/71))
- 2016-11-14 v2.7.1 Recover Java 7 support. (v2.7.0 doesn't work with Java 8)
- 2016-11-11 v2.7.0 Added HTTPS proxy settings.
  ([#67](https://github.com/kt3k/coveralls-gradle-plugin/pull/67))
- 2016-01-29 v2.6.3 Recover Java 7 support. (v2.6.0 and v2.6.1 don't work with
  Java 8)
- 2016-01-29 v2.6.3 Recover Java 7 support. (v2.6.0 and v2.6.1 don't work with
  Java 8)
- 2016-01-29 v2.6.1 Improved CircleCI support.
  ([#52](https://github.com/kt3k/coveralls-gradle-plugin/pull/52))
- 2016-01-22 v2.6.0 Improved CircleCI support.
  ([#51](https://github.com/kt3k/coveralls-gradle-plugin/pull/51))
- 2016-01-18 v2.5.0 Added Bitrise CI support.
  ([#50](https://github.com/kt3k/coveralls-gradle-plugin/pull/50))
- 2015-05-10 v2.4.0 Improved Travis CI support.
  ([#40](https://github.com/kt3k/coveralls-gradle-plugin/pull/40))
- 2015-02-10 v2.3.1 Improved Snap CI support.
  ([#36](https://github.com/kt3k/coveralls-gradle-plugin/pull/36))
- 2015-02-10 v2.3.0 [Snap CI](https://snap-ci.com/) support.
  ([#35](https://github.com/kt3k/coveralls-gradle-plugin/pull/35))
- 2015-02-08 v2.2.0 JenkinsCI support.
  ([#33](https://github.com/kt3k/coveralls-gradle-plugin/pull/33))
- 2015-01-05 v2.1.0 CircleCI support.
  ([#31](https://github.com/kt3k/coveralls-gradle-plugin/pull/31))
- 2014-10-05 v2.0.1 Compatibility with gradle-android-plugin 0.13.x.
  ([#25](https://github.com/kt3k/coveralls-gra dle-plugin/pull/25))
- 2014-08-01 v2.0.0 More CI services support. Improvements.
  ([#21](https://github.com/kt3k/coveralls-gradle-plugin/pull/21))
- 2014-07-21 v1.0.2 Plugin ID changed. `coveralls` ->
  `com.github.kt3k.coveralls`
  ([#19](https://github.com/kt3k/coveralls-gradle-plugin/pull/19))
- 2014-07-06 v0.6.1 XML parser behaviours fixed.
  ([#18](https://github.com/kt3k/coveralls-gradle-plugin/pull/18))
- 2014-07-02 v0.6.0 Multiple project support for JaCoCo.
  ([#13](https://github.com/kt3k/coveralls-gradle-plugin/pull/13))
- 2014-06-26 v0.5.0 Android plugin support.
  ([#17](https://github.com/kt3k/coveralls-gradle-plugin/pull/17))
- 2014-06-21 v0.4.1 XML parser settings fixed.
  ([#16](https://github.com/kt3k/coveralls-gradle-plugin/pull/16))
- 2014-03-15 v0.3.1 HttpBuilder upgraded to v0.7.1.
  ([#8](https://github.com/kt3k/coveralls-gradle-plugin/pull/8))
- 2014-03-11 v0.3.0 Coverage report path became configurable.
  ([#7](https://github.com/kt3k/coveralls-gradle-plugin/pull/7))
- 2014-02-19 v0.2.5 Travis Pro support.
  ([#4](https://github.com/kt3k/coveralls-gradle-plugin/pull/4))
- 2014-01-21 v0.2.4 Ignore absent source files.
  ([#3](https://github.com/kt3k/coveralls-gradle-plugin/pull/3))
- 2013-12-09 v0.2.1 JaCoCo support.
  ([#2](https://github.com/kt3k/coveralls-gradle-plugin/pull/2))
- 2013-11-02 v0.1.6 Available at Maven Central.
- 2013-10-27 v0.1.5 Multiple `<source>` tag support.
  ([#1](https://github.com/kt3k/coveralls-gradle-plugin/pull/1))
- 2013-10-08 v0.1.3 First usable version.
