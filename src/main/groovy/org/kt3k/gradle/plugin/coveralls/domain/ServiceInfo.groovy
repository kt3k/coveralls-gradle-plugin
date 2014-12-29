package org.kt3k.gradle.plugin.coveralls.domain

import groovy.transform.TupleConstructor

/**
 * The model class of CI service information
 */
@TupleConstructor
class ServiceInfo {
	String repoToken
	String serviceName
	String serviceNumber
	String serviceBuildUrl
	String serviceBranch
	String servicePullRequest
	String serviceJobId

	boolean equals(o) {
		if (this.is(o)) return true
		if (!(o instanceof ServiceInfo)) return false

		ServiceInfo that = (ServiceInfo) o

		if (repoToken != that.repoToken) return false
		if (serviceBranch != that.serviceBranch) return false
		if (serviceBuildUrl != that.serviceBuildUrl) return false
		if (serviceJobId != that.serviceJobId) return false
		if (serviceName != that.serviceName) return false
		if (serviceNumber != that.serviceNumber) return false
		if (servicePullRequest != that.servicePullRequest) return false

		return true
	}

	int hashCode() {
		int result
		result = repoToken.hashCode()
		result = 31 * result + (serviceName != null ? serviceName.hashCode() : 0)
		result = 31 * result + (serviceNumber != null ? serviceNumber.hashCode() : 0)
		result = 31 * result + (serviceBuildUrl != null ? serviceBuildUrl.hashCode() : 0)
		result = 31 * result + (serviceBranch != null ? serviceBranch.hashCode() : 0)
		result = 31 * result + (servicePullRequest != null ? servicePullRequest.hashCode() : 0)
		result = 31 * result + (serviceJobId != null ? serviceJobId.hashCode() : 0)
		return result
	}
}

