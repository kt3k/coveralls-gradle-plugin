package org.kt3k.gradle.plugin

import org.gradle.api.*

class CoverallsPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.task('hello') << {
            println 'hello'
        }
    }

}
