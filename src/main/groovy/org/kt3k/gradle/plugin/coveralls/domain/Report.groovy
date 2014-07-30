package org.kt3k.gradle.plugin.coveralls.domain

import groovy.json.JsonBuilder

/**
 * The model class of the report for Coveralls' format.
 */
class Report {
    String repo_token
    String service_name
    String service_job_id
    String service_number
    String service_build_url
    String service_branch
    String service_pull_request
    List<SourceReport> source_files

    public Report(ServiceInfo serviceInfo, List<SourceReport> sourceFiles) {
        this.service_name = serviceInfo.serviceName
        this.service_number = serviceInfo.serviceNumber
        this.service_build_url = serviceInfo.serviceBuildUrl
        this.service_branch = serviceInfo.serviceBranch
        this.service_job_id = serviceInfo.serviceJobId
        this.service_pull_request = serviceInfo.servicePullRequest
        this.repo_token = serviceInfo.repoToken
        this.source_files = sourceFiles
    }

    public String toJson() {
        def minusNull = getProperties().findAll { it.key != "class" && it.value != null }.sort { it.key }
        JsonBuilder json = new JsonBuilder(minusNull)
        return json.toString()
    }
}
