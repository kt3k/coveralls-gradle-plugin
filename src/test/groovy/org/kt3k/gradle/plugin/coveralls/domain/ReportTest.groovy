package org.kt3k.gradle.plugin.coveralls.domain

import org.junit.Test
import static org.junit.Assert.*

class ReportTest {

    @Test
    void testConstructor() {
        Report report = new Report(new ServiceInfo(serviceName: 'xyz-ci', serviceJobId: '12345678'), [new SourceReport('Test.java', 'class Test {}', [1])])

        assertNotNull report
        assertEquals 'xyz-ci', report.service_name
        assertEquals '12345678', report.service_job_id
        assertEquals null, report.repo_token
    }

    @Test
    void testToJsonV1() {
        Report report = new Report(new ServiceInfo(serviceName: 'xyz-ci', serviceJobId: '12345678'), [new SourceReport('Test.java', 'class Test {}', [1])])
        assertEquals '{"service_job_id":"12345678","service_name":"xyz-ci","source_files":[{"coverage":[1],"source":"class Test {}","name":"Test.java"}]}', report.toJson()
    }

    @Test
    void testToJsonV2() {
        Report report = new Report(new ServiceInfo(serviceName: 'xyz-ci', serviceJobId: '12345678', repoToken: 'ABCDEF'), [new SourceReport('Test.java', 'class Test {}', [1])])
        assertEquals '{"repo_token":"ABCDEF","service_job_id":"12345678","service_name":"xyz-ci","source_files":[{"coverage":[1],"source":"class Test {}","name":"Test.java"}]}', report.toJson()
    }

    @Test
    void testToJsonV3() {
        Report report = new Report(new ServiceInfo(serviceName: 'name', serviceNumber: 'build_number', serviceBuildUrl: 'build_url', serviceBranch: 'branch', servicePullRequest: 'pull_request', repoToken: 'ABCDEF'), [new SourceReport('Test.java', 'class Test {}', [1])])
        assertEquals '{"repo_token":"ABCDEF","service_branch":"branch","service_build_url":"build_url","service_name":"name","service_number":"build_number","service_pull_request":"pull_request","source_files":[{"coverage":[1],"source":"class Test {}","name":"Test.java"}]}', report.toJson()
    }
}
