package org.kt3k.gradle.plugin.coveralls.domain

/**
 * The model class of CI service information
 */
class ServiceInfo {
	final String repoToken
	final String serviceName
    final String serviceNumber
    final String serviceBuildUrl
    final String serviceBranch
    final String servicePullRequest
	final String serviceJobId

	ServiceInfo(String serviceName, String serviceJobId, String repoToken) {
		this.serviceName = serviceName
		this.serviceJobId = serviceJobId
		this.repoToken = repoToken
        this.serviceNumber = null
        this.serviceBuildUrl = null
        this.serviceBranch = null
        this.servicePullRequest = null
	}

    ServiceInfo(String serviceName, String serviceNumber, String serviceBuildUrl, String serviceBranch, String servicePullRequest, String repoToken) {
        this.serviceName = serviceName
        this.serviceNumber = serviceNumber
        this.serviceBuildUrl = serviceBuildUrl
        this.serviceBranch = serviceBranch
        this.servicePullRequest = servicePullRequest
        this.repoToken = repoToken
        this.serviceJobId = null
    }

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

