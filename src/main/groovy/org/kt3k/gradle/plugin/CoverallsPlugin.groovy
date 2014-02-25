package org.kt3k.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.kt3k.gradle.plugin.coveralls.CoverallsTask
import org.kt3k.gradle.plugin.coveralls.domain.CoberturaSourceReportFactory
import org.kt3k.gradle.plugin.coveralls.domain.JacocoSourceReportFactory
import org.kt3k.gradle.plugin.coveralls.domain.SourceReportFactory

/**
 * Coveralls plugin for gradle.
 *
 * This plugin adds the only one task `coveralls` which searches coverage report xml, converts it into JSON string and post to coveralls.io.
 */
class CoverallsPlugin implements Plugin<Project> {

	/**
	 * Add `coveralls` task to the project.
	 *
	 * @param project the project to add coveralls task
	 */
	void apply(Project project) {

		// create coveralls project extension
		project.extensions.create('coveralls', CoverallsPluginExtension)

		// register coveralls task
		Task task = project.task('coveralls', type: CoverallsTask)

		// set project
		task.project = project

		// set env vars
		task.env = System.getenv()

		// add factories
		task.sourceReportFactoryMap[project.projectDir.path + '/' + project.extensions.coveralls.coberturaReportPath] = new CoberturaSourceReportFactory();
		task.sourceReportFactoryMap[project.projectDir.path + '/' + project.extensions.coveralls.jacocoReportPath] = new JacocoSourceReportFactory();

	}

}
