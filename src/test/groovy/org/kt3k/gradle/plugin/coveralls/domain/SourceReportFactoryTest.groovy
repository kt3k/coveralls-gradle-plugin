package org.kt3k.gradle.plugin.coveralls.domain

import org.junit.Test
import static org.junit.Assert.*

class SourceReportFactoryTest {

	@Test
	void testCreateFromCoberturaXML() {

		List<SourceReport> reports = SourceReportFactory.createFromCoberturaXML new File('src/test/fixture/coverage.xml')

		assertNotNull reports
		assertEquals 7, reports.size()

		assertEquals 'org/kt3k/gradle/plugin/CoverallsPlugin.groovy', reports[0].name
		assertEquals 'org/kt3k/gradle/plugin/coveralls/Application.groovy', reports[1].name
		assertEquals 'org/kt3k/gradle/plugin/coveralls/domain/Report.groovy', reports[2].name
		assertEquals 'org/kt3k/gradle/plugin/coveralls/domain/ServiceInfo.groovy', reports[3].name
		assertEquals 'org/kt3k/gradle/plugin/coveralls/domain/ServiceInfoFactory.groovy', reports[4].name
		assertEquals 'org/kt3k/gradle/plugin/coveralls/domain/SourceReport.groovy', reports[5].name
		assertEquals 'org/kt3k/gradle/plugin/coveralls/domain/SourceReportFactory.groovy', reports[6].name
	}
}
