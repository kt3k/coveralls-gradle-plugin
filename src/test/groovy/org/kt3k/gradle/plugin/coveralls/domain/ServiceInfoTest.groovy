package org.kt3k.gradle.plugin.coveralls.domain

import org.junit.Test
import static org.junit.Assert.*

class ServiceInfoTest {


	@Test
	void testEquals() {
		assertEquals new ServiceInfo("a", "b", "c", "d", "e", "f"), new ServiceInfo("a", "b", "c", "d", "e", "f")
		assertNotEquals new ServiceInfo("a", "b", "c", "d", "e", "f"), new ServiceInfo("_", "b", "c", "d", "e", "f")
		assertNotEquals new ServiceInfo("a", "b", "c", "d", "e", "f"), new ServiceInfo("a", "_", "c", "d", "e", "f")
		assertNotEquals new ServiceInfo("a", "b", "c", "d", "e", "f"), new ServiceInfo("a", "b", "_", "d", "e", "f")
		assertNotEquals new ServiceInfo("a", "b", "c", "d", "e", "f"), new ServiceInfo("a", "b", "c", "_", "e", "f")
		assertNotEquals new ServiceInfo("a", "b", "c", "d", "e", "f"), new ServiceInfo("a", "b", "c", "d", "_", "f")
		assertNotEquals new ServiceInfo("a", "b", "c", "d", "e", "f"), new ServiceInfo("a", "b", "c", "d", "e", "_")
	}


	@Test
	void testHashCode() {
		ServiceInfo serviceInfo = new ServiceInfo("a", "b", "c", "d", "e", "f")

		assertNotNull serviceInfo.hashCode()
		assertTrue serviceInfo.hashCode().toString().isInteger()
	}

}
