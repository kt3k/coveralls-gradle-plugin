package org.kt3k.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.kt3k.gradle.plugin.coveralls.Application
import org.kt3k.gradle.plugin.coveralls.domain.CoberturaSourceReportFactory
import org.kt3k.gradle.plugin.coveralls.domain.JacocoSourceReportFactory
import org.kt3k.gradle.plugin.coveralls.domain.SourceReportFactory
import org.slf4j.LoggerFactory

/**
 * Coveralls plugin for gradle.
 *
 * This plugin adds the only one task `coveralls` which searches coverage report xml, converts it into JSON string and post to coveralls.io.
 */
class CoverallsPlugin implements Plugin<Project> {

	/** API endpoint url of Coveralls */
	static String API_ENDPOINT = 'https://coveralls.io/api/v1/jobs'

	/** Cobertura report path */
	static String COBERTURA_REPORT_PATH = 'build/reports/cobertura/coverage.xml'

	/** JaCoCo report path */
	static String JACOCO_REPORT_PATH = 'build/reports/jacoco/test/jacocoTestReport.xml'

	/**
	 * Add `coveralls` task to the project.
	 *
	 * @param project the project to add coveralls task
	 */
	void apply(Project project) {
		project.task('coveralls') << {
			// project dir
			String projectDir = project.projectDir.path

			// create coverage file to Factory class mapping
			Map<String, SourceReportFactory> sourceReportFactoryMap = [:]

			// add factories
			sourceReportFactoryMap[projectDir + '/' + COBERTURA_REPORT_PATH] = new CoberturaSourceReportFactory();
			sourceReportFactoryMap[projectDir + '/' + JACOCO_REPORT_PATH] = new JacocoSourceReportFactory();

			// run main procedure
			Application.main(System.getenv(), project, API_ENDPOINT, sourceReportFactoryMap, LoggerFactory.getLogger('coveralls-logger'))
		}
	}

}
