package org.kt3k.gradle.plugin.coveralls.domain

import org.junit.Test
import static org.junit.Assert.*

class ServiceInfoTest {

	@Test
	void testConstructor() {
		ServiceInfo serviceInfo = new ServiceInfo('x-ci', '1729', 'ABCDEF')

		assertNotNull serviceInfo
		assertEquals 'x-ci', serviceInfo.serviceName
		assertEquals '1729', serviceInfo.serviceJobId
		assertEquals 'ABCDEF', serviceInfo.repoToken
	}

}
