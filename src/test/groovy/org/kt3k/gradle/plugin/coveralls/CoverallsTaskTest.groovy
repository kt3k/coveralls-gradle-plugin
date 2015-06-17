package org.kt3k.gradle.plugin.coveralls

import java.io.FileNotFoundException

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

import org.mockito.Mockito

import static groovy.test.GroovyAssert.assertTrue
import static java.io.File.separatorChar

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

		Logger logger = task.logger = Mockito.mock Logger

		task.coverallsAction()

		Mockito.verify(logger).error 'no available CI service'
	}

	@Test
	void testMainNoFile() {

		Task task = this.project.task('coveralls', type: CoverallsTask)

		Logger logger = task.logger = Mockito.mock Logger

		// set nonexistent report file paths
		this.project.extensions.coveralls.coberturaReportPath = 'foo/bar.xml'
		this.project.extensions.coveralls.jacocoReportPath = 'baz/bar.xml'

		String projDir = this.project.projectDir.path

		task.env = [TRAVIS: 'true', TRAVIS_JOB_ID: '123']

		task.coverallsAction()

		Mockito.verify(logger).error 'No report file available: ' + [projDir + separatorChar + 'foo' + separatorChar + 'bar.xml', projDir + separatorChar + 'baz' + separatorChar + 'bar.xml']
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

		Logger logger = task.logger = Mockito.mock Logger

		task.postJsonToUrl '{}', 'http://localhost:8089/abc'

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

		Task task = this.project.task('coveralls', type: CoverallsTask)

		Logger logger = task.logger = Mockito.mock Logger

		task.postJsonToUrl '{}', 'http://localhost:8089/abc'

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

		Task task = this.project.task('coveralls', type: CoverallsTask)

		Logger logger = task.logger = Mockito.mock Logger

		task.sourceReportFactoryMap['src/test/fixture/coverage.xml'] = new CoberturaSourceReportFactory()

		task.env = [TRAVIS: 'true', TRAVIS_JOB_ID: '123']
		task.project = this.project
		task.project.extensions.coveralls.apiEndpoint = 'http://localhost:8089/abc'

		task.coverallsAction()

		Mockito.verify(logger).info 'HTTP/1.1 200 OK'
		Mockito.verify(logger).info '[Content-Type: text/plain, Content-Length: 12, Server: Jetty(6.1.25)]'

		WireMock.verify(postRequestedFor(urlMatching('/abc')))

	}


	@Test
	void testMainWithEmptyCoverageReport() {

		Task task = this.project.task('coveralls', type: CoverallsTask)

		Logger logger = task.logger = Mockito.mock Logger

		task.sourceReportFactoryMap['src/test/fixture/coverage_without_source_files.xml'] = new CoberturaSourceReportFactory()

		task.env = [TRAVIS: 'true', TRAVIS_JOB_ID: '123']
		task.project = this.project
		task.project.extensions.coveralls.apiEndpoint = 'http://localhost:8089/abc'

		task.coverallsAction()

		Mockito.verify(logger).error 'No source file found on the project: "test"'
		Mockito.verify(logger).error 'With coverage file: src/test/fixture/coverage_without_source_files.xml'

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
		Logger logger = task.logger = Mockito.mock Logger

		// dummy cobertura coverage report
		task.sourceReportFactoryMap['src/test/fixture/coverage.xml'] = new CoberturaSourceReportFactory()

		// set travis env variables and repo token (this lead to Travis-pro report)
		task.env = [TRAVIS: 'true', TRAVIS_JOB_ID: '123', COVERALLS_REPO_TOKEN: 'abc123xyz']
		task.project = this.project
		task.project.extensions.coveralls.apiEndpoint = 'http://localhost:8089/abc'

		task.coverallsAction()

		// verify logger interactions
		Mockito.verify(logger).warn 'service name: travis-pro'
		Mockito.verify(logger).warn 'service job id: 123'
		Mockito.verify(logger).warn 'repo token: present (not shown for security)'

		Mockito.verify(logger).info 'HTTP/1.1 200 OK'
		Mockito.verify(logger).info '[Content-Type: text/plain, Content-Length: 12, Server: Jetty(6.1.25)]'

		// verify api interaction
		WireMock.verify(postRequestedFor(urlMatching('/abc')))

	}

	@Test
	void testMainNoPostJson() {
		Task task = this.project.task('coveralls', type: CoverallsTask)

		// set mocked logger
		Logger logger = task.logger = Mockito.mock Logger

		// dummy cobertura coverage report
		task.sourceReportFactoryMap['src/test/fixture/coverage.xml'] = new CoberturaSourceReportFactory()

		task.env = [TRAVIS: 'true', TRAVIS_JOB_ID: '123']
		task.project = this.project

		task.project.extensions.coveralls.sendToCoveralls = false
		task.coverallsAction()

		Mockito.verify(logger, Mockito.never()).info 'HTTP/1.1 200 OK'
	}

	@Test
	void testMainSaveFile() {
		Task task = this.project.task('coveralls', type: CoverallsTask)

		// set mocked logger
		Logger logger = task.logger = Mockito.mock Logger

		// dummy cobertura coverage report
		task.sourceReportFactoryMap['src/test/fixture/coverage.xml'] = new CoberturaSourceReportFactory()

		task.env = [TRAVIS: 'true', TRAVIS_JOB_ID: '123']
		task.project = this.project

		task.project.extensions.coveralls.sendToCoveralls = false
		task.project.extensions.coveralls.saveAsFile = true
		task.coverallsAction()

		Mockito.verify(logger).info 'Saving output to file: ' + task.project.extensions.coveralls.saveFilePath

		assertTrue(new File(task.project.extensions.coveralls.saveFilePath).exists())
	}

	@Test
	void testMainSaveFileError() {
		Task task = this.project.task('coveralls', type: CoverallsTask)

		// set mocked logger
		Logger logger = task.logger = Mockito.mock Logger

		// dummy cobertura coverage report
		task.sourceReportFactoryMap['src/test/fixture/coverage.xml'] = new CoberturaSourceReportFactory()

		task.env = [TRAVIS: 'true', TRAVIS_JOB_ID: '123']
		task.project = this.project

		task.project.extensions.coveralls.sendToCoveralls = false
		task.project.extensions.coveralls.saveAsFile = true
		task.project.extensions.coveralls.saveFilePath = "."

		try {
			task.coverallsAction()
		} catch (FileNotFoundException e) {
			// Expected
		}

		Mockito.verify(logger).error 'Failed to write JSON file to .'
	}
}
