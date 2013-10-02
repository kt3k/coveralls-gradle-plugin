package org.kt3k.gradle.plugin.coveralls

import org.junit.Test
import org.junit.Rule

import static org.mockito.Mockito.*

import org.gradle.api.logging.Logger

import static com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.WireMockRule

class ApplicationTest {

	@Test
	void testMainNoService() {

		Logger logger = mock Logger

		Application.main [:], "", "", logger

		verify(logger).error 'no available CI service'
	}

	@Test
	void testMainNoFile() {

		Logger logger = mock Logger

		Application.main([TRAVIS: 'true', TRAVIS_JOB_ID: '123'], "", "nonexistent-path", logger)

		verify(logger).error 'covertura report not available: ' + (new File('nonexistent-path').absolutePath)
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

		verify(logger).info 'HTTP/1.1 200 OK'
		verify(logger).info '[Content-Type: text/plain, Content-Length: 12, Server: Jetty(6.1.25)]'

		WireMock.verify(postRequestedFor(urlMatching('/abc')))

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

		verify(logger).error 'HTTP/1.1 404 Not Found'
		verify(logger).error '[Content-Type: text/plain, Content-Length: 9, Server: Jetty(6.1.25)]'

		WireMock.verify(postRequestedFor(urlMatching('/abc')))

	}

}
