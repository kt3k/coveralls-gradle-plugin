package org.kt3k.gradle.plugin

class CoverallsPluginExtension {

	/** CI service name */
	String service = null // it is for the services other than travis-ci

	/** the flag to save JSON string as file */
	Boolean saveAsFile = false

	Boolean sendToCoveralls = true

	/** the JSON file path */
	String saveFilePath = 'build/coveralls/report.json'

	/** API endpoint of Coveralls */
	String apiEndpoint = 'https://coveralls.io/api/v1/jobs'

	/** Kotlin root package */
	String kotlinRootPackage = ''

	/** Jacoco report path */
	Object jacocoReportPath = 'build/reports/jacoco/test/jacocoTestReport.xml'

	/** Cobertura report path */
	Object coberturaReportPath = 'build/reports/cobertura/coverage.xml'

	/** PIT report path */
	Object pitReportPath = 'build/reports/pitest/mutations.xml'

	/**
	 * Additional source directories
	 */
	List<String> sourceDirs = []

}
