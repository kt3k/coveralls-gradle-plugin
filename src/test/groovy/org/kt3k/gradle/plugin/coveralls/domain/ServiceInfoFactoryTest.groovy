package org.kt3k.gradle.plugin.coveralls.domain

import org.junit.Test
import static org.junit.Assert.*

class ServiceInfoFactoryTest {

	@Test
	void testCreateFromEnvVar() {
		// test the case no service is available
		assertNull ServiceInfoFactory.createFromEnvVar([:])

		// test the case of travis
		ServiceInfo serviceInfo = ServiceInfoFactory.createFromEnvVar TRAVIS: 'true', TRAVIS_JOB_ID: '12345678'

		assertEquals 'travis-ci', serviceInfo.serviceName
		assertEquals '12345678', serviceInfo.serviceJobId

		// test the case that travis == true but job_id is not available
		assertNull ServiceInfoFactory.createFromEnvVar(TRAVIS: 'true')
	}

}
