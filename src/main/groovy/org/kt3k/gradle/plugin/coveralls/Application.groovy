package org.kt3k.gradle.plugin.coveralls

import groovyx.net.http.HTTPBuilder

import static groovyx.net.http.Method.POST

import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.ContentType

import org.kt3k.gradle.plugin.coveralls.domain.*


class Application {

	static String API_ENDPOINT = 'https://coveralls.io/api/v1/jobs'

	static String COBERTURA_REPORT_PATH = 'build/reports/cobertura/coverage.xml'

	static void postToCoveralls(String json) {

		HTTPBuilder http = new HTTPBuilder(API_ENDPOINT)

		http.request(POST) { req ->

			req.entity = MultipartEntityBuilder.create().addBinaryBody('json_file', json.getBytes('UTF-8'), ContentType.APPLICATION_JSON, 'json_file').build()

			response.success = { resp, reader ->
				println resp.statusLine
				println resp.getAllHeaders()
				println resp.getData()
				System.out << reader
			}

			response.failure = { resp, reader ->
				println resp.statusLine
				println resp.getAllHeaders()
				println resp.getData()
				System.out << reader
			}
		}
	}

	static void main(Map env) {
		ServiceInfo serviceInfo = ServiceInfoFactory.createFromEnvVar(env)

		if (serviceInfo == null) {
			println 'no available service'

			return
		}

		println 'service name: ' + serviceInfo.serviceName
		println 'service job id: ' + serviceInfo.serviceJobId

		File file = new File(COBERTURA_REPORT_PATH)

		if (!file.exists()) {
			println 'covertura report not available: ' + file.absolutePath

			return
		}

		println 'cobertura report file: ' + file.absolutePath

		List<SourceReport> sourceReports = SourceReportFactory.createFromCoberturaXML file

		Report rep = new Report(serviceInfo.serviceName, serviceInfo.serviceJobId, sourceReports)

		println rep.toJson()

		postToCoveralls rep.toJson()
	}

}
