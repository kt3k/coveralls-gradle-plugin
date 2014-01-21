package org.kt3k.gradle.plugin.coveralls

import groovyx.net.http.HTTPBuilder
import org.apache.http.entity.ContentType
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.gradle.api.Project
import org.kt3k.gradle.plugin.coveralls.domain.*
import org.slf4j.Logger

import static groovyx.net.http.Method.POST

class Application {

	static void postJsonToUrl(String json, String url, final Logger logger) {

		HTTPBuilder http = new HTTPBuilder(url)

		http.request(POST) { req ->

			req.entity = MultipartEntityBuilder.create().addBinaryBody('json_file', json.getBytes('UTF-8'), ContentType.APPLICATION_JSON, 'json_file').build()

			response.success = { resp, reader ->
				logger.info resp.statusLine.toString()
				logger.info resp.getAllHeaders().toString()
				System.out << reader
			}

			response.failure = { resp, reader ->
				logger.error resp.statusLine.toString()
				logger.error resp.getAllHeaders().toString()
				System.out << reader
			}
		}
	}

	static void main(Map<String, String> env, Project project, String apiEndpoint, Map<String, SourceReportFactory> sourceReportFactoryMap, Logger logger) {

		// create service info from environmental variables
		ServiceInfo serviceInfo = ServiceInfoFactory.createFromEnvVar env

		if (serviceInfo == null) {
			logger.error 'no available CI service'

			return
		}

		logger.warn 'service name: ' + serviceInfo.serviceName
		logger.warn 'service job id: ' + serviceInfo.serviceJobId

		// search the coverage file
		Map.Entry<String, SourceReportFactory> entry = sourceReportFactoryMap.find { Map.Entry<String, SourceReportFactory> entry ->
			String coverageFile = entry.key
			return new File(coverageFile).exists()
		}

		if (entry == null) {
			logger.error 'No report file available: ' + sourceReportFactoryMap.keySet()
			return
		}

		String reportFilePath = entry.key
		File reportFile = new File(reportFilePath)
		SourceReportFactory sourceReportFactory = entry.value

		logger.info 'Report file: ' + reportFile

		List<SourceReport> sourceReports = sourceReportFactory.createReportList project, reportFile

		// if report size is zero then do nothing
		if (sourceReports.size == 0) {
			logger.error 'No source file found on the project: "' + project.name + '"'
			logger.error 'With coverage file: ' + reportFilePath
			return
		}

		Report rep = new Report(serviceInfo.serviceName, serviceInfo.serviceJobId, sourceReports)

		String json = rep.toJson()
		logger.info json

		postJsonToUrl json, apiEndpoint, logger
	}

}
