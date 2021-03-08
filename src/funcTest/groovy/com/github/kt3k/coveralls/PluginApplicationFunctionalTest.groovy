package com.github.kt3k.coveralls

import org.junit.Assume
import spock.lang.Unroll

class PluginApplicationFunctionalTest extends AbstractFunctionalTest {
    @Unroll
    def "can apply the plugin #plugin with Gradle #version"() {
        Assume.assumeTrue(version >= plugin.minimalGradleVersion)

        buildFile << """
            plugins {
                id 'java-library'
                ${plugin.pluginBlock}
                id 'com.github.kt3k.coveralls'
            }

            repositories {
                mavenCentral()
            }

            dependencies {
                testImplementation 'junit:junit:4.13.2'
            }

            $plugin.config
        """
        file('src/main/java/com/acme').mkdirs()
        file('src/main/java/com/acme/Foo.java') << """package com.acme;

            class Foo {
                static int magic() { return 42; }
            }

        """

        when:
        usingGradleVersion(version)
        build(plugin.task, 'coveralls')

        then:
        noExceptionThrown()

        where:
        [plugin, version] << [
            TESTED_COVERAGE_PLUGINS,
            TESTED_GRADLE_VERSIONS
        ].combinations()
    }
}
