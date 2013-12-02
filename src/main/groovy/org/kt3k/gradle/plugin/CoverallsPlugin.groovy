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

	static File COBERTURA_REPORT_PATH = new File('build/reports/cobertura/coverage.xml')
	static File JACOCO_REPORT_PATH = new File('build/reports/jacoco/test/jacocoTestReport.xml')

	void apply(Project project) {
		project.task('coveralls') << {
			Map<File, SourceReportFactory> sourceReportFactoryMap = [:]
			sourceReportFactoryMap[COBERTURA_REPORT_PATH] = new CoberturaSourceReportFactory();
			sourceReportFactoryMap[JACOCO_REPORT_PATH] = new JacocoSourceReportFactory();
			Application.main(System.getenv(), project, API_ENDPOINT, sourceReportFactoryMap, LoggerFactory.getLogger('coveralls-logger'))
		}
	}

}
