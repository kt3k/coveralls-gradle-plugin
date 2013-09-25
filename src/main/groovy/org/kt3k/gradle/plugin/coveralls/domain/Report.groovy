package org.kt3k.gradle.plugin.coveralls.domain

import groovy.json.*

// model for coveralls io report
class Report {
    String service_job_id;
    String service_name;
    List<SourceReport> source_files;

    public Report() {
    }

    public Report(String serviceName, String serviceJobId, List<SourceReport> sourceFiles) {
        this.service_name = serviceName;
        this.service_job_id = serviceJobId;
        this.source_files = sourceFiles;
    }

    public String toJson() {
        JsonBuilder json = new JsonBuilder(this)
        return json.toString()
    }
}
