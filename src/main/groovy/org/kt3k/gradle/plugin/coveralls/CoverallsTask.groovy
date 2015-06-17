package org.kt3k.gradle.plugin.coveralls

import groovy.json.JsonOutput
import groovyx.net.http.HTTPBuilder
import org.apache.http.entity.ContentType
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.TaskAction
import org.kt3k.gradle.plugin.CoverallsPluginExtension
import org.kt3k.gradle.plugin.coveralls.domain.*

import static groovyx.net.http.Method.POST

/**
 * `coveralls` task class
 */
class CoverallsTask extends DefaultTask {

	/** environmental variable */
	Map<String, String> env = [:]

	/** the logger */
	Logger logger = Logging.getLogger('coveralls-logger')

	/** source report factory mapping */
	Map<String, SourceReportFactory> sourceReportFactoryMap = [:]


	/**
	 * Posts JSON string to the url (as a multipart HTTP POST).
	 *
	 * @param json the JSON string to post
	 * @param url the url to post
	 * @param logger the logger to use
	 */
	void postJsonToUrl(String json, String url) {

		HTTPBuilder http = new HTTPBuilder(url)

		http.request(POST) { req ->

			req.entity = MultipartEntityBuilder.create().addBinaryBody('json_file', json.getBytes('UTF-8'), ContentType.APPLICATION_JSON, 'json_file').build()

			response.success = { resp, reader ->
				this.logger.info resp.statusLine.toString()
				this.logger.info resp.getAllHeaders().toString()
				System.out << reader
			}

			response.failure = { resp, reader ->
				this.logger.error resp.statusLine.toString()
				this.logger.error resp.getAllHeaders().toString()
				System.out << reader
			}
		}
	}

	void saveJsonToFile(String json, String saveFilePath) {
		try {
			File reportFile = new File(saveFilePath)
			if (reportFile.getParentFile()) {
				reportFile.getParentFile().mkdirs()
			}
			reportFile.withWriter { it << JsonOutput.prettyPrint(json) }
		} catch (IOException e) {
			this.logger.error 'Failed to write JSON file to ' + saveFilePath
			throw e
		}
	}

	/**
	 * Main procedure of `coveralls` task.
	 *
	 * @param env environmental variables
	 * @param project project to deal with
	 * @param apiEndpoint API endpoint to post resulting report
	 * @param sourceReportFactoryMap the mapping of sourceReportFactories to use
	 * @param logger the logger to use
	 */
	@TaskAction
	void coverallsAction() {

		// create service info from environmental variables
		ServiceInfo serviceInfo = ServiceInfoFactory.createFromEnvVar this.env

		if (serviceInfo == null) {
			this.logger.error 'no available CI service'

			return
		}

		this.logger.warn 'service name: ' + serviceInfo.serviceName
		this.logger.warn 'service job id: ' + serviceInfo.serviceJobId
		if (serviceInfo.repoToken != null) {
			this.logger.warn 'repo token: present (not shown for security)'
		} else {
			this.logger.warn 'repo token: null'
		}



		def directory = this.project.projectDir
		GitInfo gitInfo = GitInfoFactory.load directory
		if (gitInfo == null) {
			this.logger.warn "no git repo found in: " + directory
		}

		// add factories
		CoverallsPluginExtension coveralls = this.project.extensions.getByType(CoverallsPluginExtension)
		this.sourceReportFactoryMap[this.project.file(coveralls.coberturaReportPath).absolutePath] = new CoberturaSourceReportFactory()
		this.sourceReportFactoryMap[this.project.file(coveralls.jacocoReportPath).absolutePath] = new JacocoSourceReportFactory()

		// search the coverage file
		Map.Entry<String, SourceReportFactory> entry = this.sourceReportFactoryMap.find { Map.Entry<String, SourceReportFactory> entry ->
			String coverageFile = entry.key
			return new File(coverageFile).exists()
		}

		if (entry == null) {
			this.logger.error 'No report file available: ' + sourceReportFactoryMap.keySet()
			return
		}

		String reportFilePath = entry.key
		File reportFile = new File(reportFilePath)
		SourceReportFactory sourceReportFactory = entry.value

		this.logger.info 'Report file: ' + reportFile

		List<SourceReport> sourceReports = sourceReportFactory.createReportList project, reportFile

		// if report size is zero then do nothing
		if (sourceReports.size == 0) {
			this.logger.error 'No source file found on the project: "' + project.name + '"'
			this.logger.error 'With coverage file: ' + reportFilePath
			return
		}

		Report rep = new Report(serviceInfo, sourceReports, gitInfo)

		String json = rep.toJson()
		this.logger.info json

		if (coveralls.saveAsFile) {
			this.logger.info 'Saving output to file: ' + coveralls.saveFilePath
			saveJsonToFile json, coveralls.saveFilePath
		}

		if (coveralls.sendToCoveralls) {
			postJsonToUrl json, coveralls.apiEndpoint
		}
	}

}
