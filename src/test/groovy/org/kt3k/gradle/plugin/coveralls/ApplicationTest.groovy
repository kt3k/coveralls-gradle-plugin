package org.kt3k.gradle.plugin.coveralls

import org.junit.Rule
import org.junit.Test

import com.github.tomakehurst.wiremock.client.WireMock
import static com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule

import org.gradle.api.Project
import org.gradle.api.logging.Logger

import org.kt3k.gradle.plugin.coveralls.domain.CoberturaSourceReportFactory
import org.kt3k.gradle.plugin.coveralls.domain.SourceReportFactory

import org.mockito.Mockito
import static org.mockito.Mockito.*

class ApplicationTest {

	@Test
	void testMainNoService() {

		Logger logger = mock Logger
		Project project = mock Project
		when(project.getProjectDir()).thenReturn new File('./')

		Application.main [:], project, "", [:], logger

		Mockito.verify(logger).error 'no available CI service'
	}

	@Test
	void testMainNoFile() {

		Logger logger = mock Logger
		Project project = mock Project
		when(project.getProjectDir()).thenReturn new File('./')

		Map<String, SourceReportFactory> sourceReportFactoryMap = [:]
		sourceReportFactoryMap[new File('hoge/fuga')] = new CoberturaSourceReportFactory();
		sourceReportFactoryMap[new File('foo/bar')] = new CoberturaSourceReportFactory();

		Application.main([TRAVIS: 'true', TRAVIS_JOB_ID: '123'], project, "", sourceReportFactoryMap, logger)

		Mockito.verify(logger).error 'No report file available: ' + ['hoge/fuga', 'foo/bar']
	}

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089)

	@Test
	void testPostJsonToUrl() {

		stubFor(post(urlEqualTo("/abc"))
				.willReturn(aResponse()
				.withStatus(200)
				.withHeader("Content-Type", "text/plain")
				.withBody("Some content")))

		Logger logger = mock Logger

		Application.postJsonToUrl '{}', 'http://localhost:8089/abc', logger

		Mockito.verify(logger).info 'HTTP/1.1 200 OK'
		Mockito.verify(logger).info '[Content-Type: text/plain, Content-Length: 12, Server: Jetty(6.1.25)]'

		WireMock.verify(postRequestedFor(urlMatching('/abc')).withRequestBody(matching('^.*$')))

	}

	@Test
	void testPostJsonToUrlFailure() {

		stubFor(post(urlEqualTo("/abc"))
				.willReturn(aResponse()
				.withStatus(404)
				.withHeader("Content-Type", "text/plain")
				.withBody('Not Found')))

		Logger logger = mock Logger

		Application.postJsonToUrl '{}', 'http://localhost:8089/abc', logger

		Mockito.verify(logger).error 'HTTP/1.1 404 Not Found'
		Mockito.verify(logger).error '[Content-Type: text/plain, Content-Length: 9, Server: Jetty(6.1.25)]'

		WireMock.verify(postRequestedFor(urlMatching('/abc')).withRequestBody(matching('.*')))

	}

	@Test
	void testMain() {

		stubFor(post(urlEqualTo("/abc"))
				.willReturn(aResponse()
				.withStatus(200)
				.withHeader("Content-Type", "text/plain")
				.withBody("Some content")))

		Project project = mock Project
		when(project.getProjectDir()).thenReturn new File('./')

		Logger logger = mock Logger
		Map<String, SourceReportFactory> sourceReportFactoryMap = [:]
		sourceReportFactoryMap['src/test/fixture/coverage.xml'] = new CoberturaSourceReportFactory()
		Application.main([TRAVIS: 'true', TRAVIS_JOB_ID: '123'], project, 'http://localhost:8089/abc', sourceReportFactoryMap, logger)

		Mockito.verify(logger).info 'HTTP/1.1 200 OK'
		Mockito.verify(logger).info '[Content-Type: text/plain, Content-Length: 12, Server: Jetty(6.1.25)]'

		WireMock.verify(postRequestedFor(urlMatching('/abc')))

	}

}
