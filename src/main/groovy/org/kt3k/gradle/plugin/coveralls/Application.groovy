package org.kt3k.gradle.plugin.coveralls

import groovyx.net.http.HTTPBuilder

import static groovyx.net.http.Method.POST

import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.ContentType

import org.gradle.api.logging.Logger

import org.kt3k.gradle.plugin.coveralls.domain.*


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

	static void main(Map env, String apiEndpoint, String coverageFilePath, Logger logger) {

		// create service info from environmental variable
		ServiceInfo serviceInfo = ServiceInfoFactory.createFromEnvVar(env)

		if (serviceInfo == null) {
			logger.error 'no available CI service'

			return
		}

		logger.warn 'service name: ' + serviceInfo.serviceName
		logger.warn 'service job id: ' + serviceInfo.serviceJobId

		File file = new File(coverageFilePath)

		if (!file.exists()) {
			logger.error 'covertura report not available: ' + file.absolutePath

			return
		}

		logger.info 'cobertura report file: ' + file.absolutePath

		List<SourceReport> sourceReports = SourceReportFactory.createFromCoberturaXML file

		Report rep = new Report(serviceInfo.serviceName, serviceInfo.serviceJobId, sourceReports)

		logger.info rep.toJson()

		postJsonToUrl rep.toJson(), apiEndpoint, logger
	}

}
