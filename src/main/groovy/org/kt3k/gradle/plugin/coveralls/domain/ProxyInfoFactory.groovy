package org.kt3k.gradle.plugin.coveralls.domain

import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging


class ProxyInfoFactory {

	public static ProxyInfo createFromEnvVar(Map<String, String> env) {
		Logger logger = Logging.getLogger('coveralls-logger')
		if (httpsProxySet(env)) {
			def host = env.get('https.proxyHost')
			def port = env.getOrDefault('https.proxyPort', "443").toInteger()
			logger.info 'Using HTTPS proxy $host:$port'
			return new ProxyInfo(
					httpsProxyHost: host,
					httpsProxyPort: port
			)
		}
	}

	private static boolean httpsProxySet(Map<String, String> env) {
		env.get('https.proxyHost') != null
	}
}
