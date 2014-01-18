package org.kt3k.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.kt3k.gradle.plugin.coveralls.Application
import org.kt3k.gradle.plugin.coveralls.domain.CoberturaSourceReportFactory
import org.kt3k.gradle.plugin.coveralls.domain.JacocoSourceReportFactory
import org.kt3k.gradle.plugin.coveralls.domain.SourceReportFactory
import org.slf4j.LoggerFactory

class CoverallsPlugin implements Plugin<Project> {

	static String API_ENDPOINT = 'https://coveralls.io/api/v1/jobs'

	static String COBERTURA_REPORT_PATH = 'build/reports/cobertura/coverage.xml'
	static String JACOCO_REPORT_PATH = 'build/reports/jacoco/test/jacocoTestReport.xml'

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
