package org.kt3k.gradle.plugin.coveralls.domain

/**
 * The model class of CI service information
 */
class ServiceInfo {
	String serviceName;
	String serviceJobId;
	String repoToken;

	public ServiceInfo(String serviceName, String serviceJobId, String repoToken) {
		this.serviceName = serviceName;
		this.serviceJobId = serviceJobId;
		this.repoToken = repoToken;
	}
}

