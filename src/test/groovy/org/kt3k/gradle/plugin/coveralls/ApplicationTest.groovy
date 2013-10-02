package org.kt3k.gradle.plugin.coveralls

import org.junit.Test

import static org.mockito.Mockito.*

import org.gradle.api.logging.Logger

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

}
