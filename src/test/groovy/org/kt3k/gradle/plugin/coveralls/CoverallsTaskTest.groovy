package org.kt3k.gradle.plugin.coveralls

import org.junit.Rule
import org.junit.Test
import org.junit.Before

import com.github.tomakehurst.wiremock.client.WireMock
import static com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.Logger
import org.gradle.testfixtures.ProjectBuilder

import org.kt3k.gradle.plugin.CoverallsPluginExtension
import org.kt3k.gradle.plugin.coveralls.domain.CoberturaSourceReportFactory
import org.kt3k.gradle.plugin.coveralls.domain.SourceReportFactory

import org.mockito.Mockito

class CoverallsTaskTest {

	Project project

	@Before
	void setUp() {
		this.project = ProjectBuilder.builder().build()
		this.project.extensions.create('coveralls', CoverallsPluginExtension)
	}

	@Test
	void testMainNoService() {

		Task task = this.project.task('coveralls', type: CoverallsTask)

		task.logger = Mockito.mock Logger

		task.coverallsAction()

		Mockito.verify(task.logger).error 'no available CI service'
	}

	@Test
	void testMainNoFile() {

		Task task = this.project.task('coveralls', type: CoverallsTask)

		task.logger = Mockito.mock Logger

		task.sourceReportFactoryMap[new File('hoge/fuga')] = new CoberturaSourceReportFactory();
		task.sourceReportFactoryMap[new File('foo/bar')] = new CoberturaSourceReportFactory();

		task.env = [TRAVIS: 'true', TRAVIS_JOB_ID: '123']

		task.coverallsAction()

		Mockito.verify(task.logger).error 'No report file available: ' + ['hoge/fuga', 'foo/bar']
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

		Task task = this.project.task('coveralls', type: CoverallsTask)

		task.logger = Mockito.mock Logger

		task.postJsonToUrl '{}', 'http://localhost:8089/abc'

		Mockito.verify(task.logger).info 'HTTP/1.1 200 OK'
		Mockito.verify(task.logger).info '[Content-Type: text/plain, Content-Length: 12, Server: Jetty(6.1.25)]'

		WireMock.verify(postRequestedFor(urlMatching('/abc')).withRequestBody(matching('^.*$')))

	}


	@Test
	void testPostJsonToUrlFailure() {

		stubFor(post(urlEqualTo("/abc"))
				.willReturn(aResponse()
				.withStatus(404)
				.withHeader("Content-Type", "text/plain")
				.withBody('Not Found')))

		Task task = this.project.task('coveralls', type: CoverallsTask)

		task.logger = Mockito.mock Logger

		task.postJsonToUrl '{}', 'http://localhost:8089/abc'

		Mockito.verify(task.logger).error 'HTTP/1.1 404 Not Found'
		Mockito.verify(task.logger).error '[Content-Type: text/plain, Content-Length: 9, Server: Jetty(6.1.25)]'

		WireMock.verify(postRequestedFor(urlMatching('/abc')).withRequestBody(matching('.*')))

	}


	@Test
	void testMain() {

		stubFor(post(urlEqualTo("/abc"))
				.willReturn(aResponse()
				.withStatus(200)
				.withHeader("Content-Type", "text/plain")
				.withBody("Some content")))

		Task task = this.project.task('coveralls', type: CoverallsTask)

		task.logger = Mockito.mock Logger

		task.sourceReportFactoryMap['src/test/fixture/coverage.xml'] = new CoberturaSourceReportFactory()

		task.env = [TRAVIS: 'true', TRAVIS_JOB_ID: '123']
		task.project = this.project
		task.project.extensions.coveralls.apiEndpoint = 'http://localhost:8089/abc'

		task.coverallsAction()

		Mockito.verify(task.logger).info 'HTTP/1.1 200 OK'
		Mockito.verify(task.logger).info '[Content-Type: text/plain, Content-Length: 12, Server: Jetty(6.1.25)]'

		WireMock.verify(postRequestedFor(urlMatching('/abc')))

	}


	@Test
	void testMainWithEmptyCoverageReport() {

		Task task = this.project.task('coveralls', type: CoverallsTask)

		task.logger = Mockito.mock Logger

		task.sourceReportFactoryMap['src/test/fixture/coverage_without_source_files.xml'] = new CoberturaSourceReportFactory()

		task.env = [TRAVIS: 'true', TRAVIS_JOB_ID: '123']
		task.project = this.project
		task.project.extensions.coveralls.apiEndpoint = 'http://localhost:8089/abc'

		task.coverallsAction()

		Mockito.verify(task.logger).error 'No source file found on the project: "test"'
		Mockito.verify(task.logger).error 'With coverage file: src/test/fixture/coverage_without_source_files.xml'

	}


	@Test
	void testMainWithTravisProSituation() {

		// stub api endpoint
		stubFor(post(urlEqualTo("/abc"))
				.willReturn(aResponse()
				.withStatus(200)
				.withHeader("Content-Type", "text/plain")
				.withBody("Some content")))

		// create task
		Task task = this.project.task('coveralls', type: CoverallsTask)

		// set mocked logger
		task.logger = Mockito.mock Logger

		// dummy cobertura coverage report
		task.sourceReportFactoryMap['src/test/fixture/coverage.xml'] = new CoberturaSourceReportFactory()

		// set travis env variables and repo token (this lead to Travis-pro report)
		task.env = [TRAVIS: 'true', TRAVIS_JOB_ID: '123', COVERALLS_REPO_TOKEN: 'abc123xyz']
		task.project = this.project
		task.project.extensions.coveralls.apiEndpoint = 'http://localhost:8089/abc'

		task.coverallsAction()

		// verify logger interactions
		Mockito.verify(task.logger).warn 'service name: travis-pro'
		Mockito.verify(task.logger).warn 'service job id: 123'
		Mockito.verify(task.logger).warn 'repo token: present (not shown for security)'

		Mockito.verify(task.logger).info 'HTTP/1.1 200 OK'
		Mockito.verify(task.logger).info '[Content-Type: text/plain, Content-Length: 12, Server: Jetty(6.1.25)]'

		// verify api interaction
		WireMock.verify(postRequestedFor(urlMatching('/abc')))

	}

}
