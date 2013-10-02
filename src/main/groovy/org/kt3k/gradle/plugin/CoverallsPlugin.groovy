package org.kt3k.gradle.plugin

import org.gradle.api.*

import org.slf4j.LoggerFactory

import org.kt3k.gradle.plugin.coveralls.Application

class CoverallsPlugin implements Plugin<Project> {

	static String API_ENDPOINT = 'https://coveralls.io/api/v1/jobs'

	static String COBERTURA_REPORT_PATH = 'build/reports/cobertura/coverage.xml'

	void apply(Project project) {
		project.task('coveralls') << {
			Application.main(System.getenv(), API_ENDPOINT, COBERTURA_REPORT_PATH, LoggerFactory.getLogger('coveralls-logger'))
		}
	}

}
