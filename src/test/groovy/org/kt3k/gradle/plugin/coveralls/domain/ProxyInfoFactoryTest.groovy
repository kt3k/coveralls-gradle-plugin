package org.kt3k.gradle.plugin.coveralls.domain

import org.junit.Test
import static org.junit.Assert.*

public class ProxyInfoFactoryTest {

	@Test
	void testCreateFromEnvVarHostAndPort() {
		ProxyInfo proxyInfo = ProxyInfoFactory
				.createFromEnvVar 'https.proxyHost': 'some host',
				'https.proxyPort':'8080'
		assertEquals 'some host', proxyInfo.httpsProxyHost
		assertEquals 8080, proxyInfo.httpsProxyPort
	}

	@Test
	void testCreateFromEnvVarHostAndDefaultPort() {
		ProxyInfo proxyInfo = ProxyInfoFactory
				.createFromEnvVar 'https.proxyHost': 'some host'
		assertEquals 'some host', proxyInfo.httpsProxyHost
		assertEquals 443, proxyInfo.httpsProxyPort
	}

	@Test
	void testCreateFromEnvVarNoHost() {
		ProxyInfo proxyInfo = ProxyInfoFactory
				.createFromEnvVar [:]
		assert proxyInfo == null
	}

	@Test
	void testCreateFromEnvVarOnlyPort() {
		ProxyInfo proxyInfo = ProxyInfoFactory
				.createFromEnvVar 'https.proxyPort': '8080'
		assert proxyInfo == null
	}
}