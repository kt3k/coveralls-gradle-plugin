package org.kt3k.gradle.plugin.coveralls.domain

import org.junit.Test
import static org.junit.Assert.*

class ReportTest {

	@Test
	void testConstructor() {
		Report report = new Report(new ServiceInfo('xyz-ci', '12345678', null), [new SourceReport('Test.java', 'class Test {}', [1])])

		assertNotNull report
		assertEquals 'xyz-ci', report.service_name
		assertEquals '12345678', report.service_job_id
		assertEquals null, report.repo_token
	}

	@Test
	void testToJson() {
		Report report = new Report(new ServiceInfo('xyz-ci', '12345678', null), [new SourceReport('Test.java', 'class Test {}', [1])])
		assertEquals '{"repo_token":null,"service_name":"xyz-ci","service_job_id":"12345678","source_files":[{"coverage":[1],"source":"class Test {}","name":"Test.java"}]}', report.toJson()
		report = new Report(new ServiceInfo('xyz-ci', '12345678', 'ABCDEF'), [new SourceReport('Test.java', 'class Test {}', [1])])
		assertEquals '{"repo_token":"ABCDEF","service_name":"xyz-ci","service_job_id":"12345678","source_files":[{"coverage":[1],"source":"class Test {}","name":"Test.java"}]}', report.toJson()
	}
}
