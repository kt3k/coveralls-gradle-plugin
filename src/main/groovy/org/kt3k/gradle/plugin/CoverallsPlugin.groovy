package org.kt3k.gradle.plugin

import org.gradle.api.*

import org.kt3k.gradle.plugin.coveralls.Application

class CoverallsPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.task('coveralls') << {
            Application.main()
        }
    }

}
