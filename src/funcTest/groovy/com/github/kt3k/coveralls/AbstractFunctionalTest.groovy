package com.github.kt3k.coveralls

import groovy.transform.CompileStatic
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.util.GradleVersion
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

abstract class AbstractFunctionalTest extends Specification {

    protected static final List<String> TESTED_COVERAGE_PLUGINS = [
        new Plugin('pitest', "id 'info.solidsoft.pitest' version '1.6.0'", 'pitest', '''
            pitest {
                targetClasses = ['com.acme.*']
            }
        ''', '5.6'),
        new Plugin('cobertura', "id 'net.saliman.cobertura' version '4.0.0'", 'cobertura'),
        new Plugin('jacoco', "id 'jacoco'", 'jacocoTestReport')
    ]

    protected static final List<GradleVersion> TESTED_GRADLE_VERSIONS = [
        GradleVersion.current(),
        GradleVersion.version('6.8'),
        GradleVersion.version('7.0-milestone-2'),
    ]

    @Rule
    TemporaryFolder temporaryFolder = new TemporaryFolder()

    private GradleVersion testedGradleVersion = GradleVersion.current()

    private String noConfigurationCacheReason

    protected void usingGradleVersion(GradleVersion gradleVersion) {
        testedGradleVersion = gradleVersion
    }

    protected void withoutConfigurationCache(String reason) {
        noConfigurationCacheReason = reason
    }

    File getProjectDir() {
        temporaryFolder.root
    }

    File getBuildFile() {
        file('build.gradle')
    }

    protected File file(String path) {
        new File(projectDir, path)
    }

    private GradleRunner gradleRunner(List<String> arguments) {
        GradleRunner.create()
            .withGradleVersion(testedGradleVersion.version)
            .forwardOutput()
            .withPluginClasspath()
            .withProjectDir(projectDir)
            .withArguments(arguments)
    }

    protected BuildResult build(String... arguments) {
        gradleRunner(calculateArguments(arguments)).build()
    }

    protected BuildResult buildAndFail(String... arguments) {
        gradleRunner(calculateArguments(arguments)).buildAndFail()
    }

    private List<String> calculateArguments(String... arguments) {
        def gradleVersionWithConfigurationCache = testedGradleVersion >= GradleVersion.version('6.6')
        if (gradleVersionWithConfigurationCache && noConfigurationCacheReason) {
            println("Configuration cache disabled: $noConfigurationCacheReason")
        }
        (gradleVersionWithConfigurationCache && !noConfigurationCacheReason
            ? ['--stacktrace',
               '--configuration-cache',
               // need to say to "warn" because for some reason the system property 'spock.iKnowWhatImDoing.disableGroovyVersionCheck'
               // is leaking to the process under test as being read
               '--configuration-cache-problems', 'warn']
            : ['--stacktrace']) + (arguments as List)
    }

    @CompileStatic
    protected static class Plugin {
        final String name
        final String pluginBlock
        final String task
        final String config
        final GradleVersion minimalGradleVersion

        Plugin(String name, String pluginBlock, String task, String configBlock = '', String minimalGradleVersion = GradleVersion.current().version) {
            this.name = name
            this.pluginBlock = pluginBlock
            this.task = task
            this.config = configBlock
            this.minimalGradleVersion = GradleVersion.version(minimalGradleVersion)
        }

        @Override
        String toString() {
            name
        }
    }
}
