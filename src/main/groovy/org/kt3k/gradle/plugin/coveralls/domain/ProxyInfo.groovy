package org.kt3k.gradle.plugin.coveralls.domain

import groovy.transform.TupleConstructor

@TupleConstructor
class ProxyInfo {
	String httpsProxyHost
	Integer httpsProxyPort

	boolean equals(o) {
		if (this.is(o)) return true
		if (getClass() != o.class) return false

		ProxyInfo proxyInfo = (ProxyInfo) o

		if (httpsProxyHost != proxyInfo.httpsProxyHost) return false
		if (httpsProxyPort != proxyInfo.httpsProxyPort) return false

		return true
	}

	int hashCode() {
		int result
		result = 31 * result + (httpsProxyHost != null ? httpsProxyHost.hashCode() : 0)
		result = 31 * result + (httpsProxyPort != null ? httpsProxyPort.hashCode() : 0)
		return result
	}
}
