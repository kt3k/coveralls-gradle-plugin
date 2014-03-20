package org.kt3k.gradle.plugin.coveralls.domain

import org.gradle.api.Project
import org.junit.Test
import static org.junit.Assert.*
import static org.mockito.Mockito.mock

class CoberturaSourceReportFactoryTest {

	@Test
	void testCreateFromCoberturaXML() {

		Project project = mock Project
		CoberturaSourceReportFactory factory = new CoberturaSourceReportFactory()
		List<SourceReport> reports = factory.createReportList project, new File('src/test/fixture/coverage.xml')

		assertNotNull reports
		assertEquals 7, reports.size()

		assertEquals 'src/test/fixture/org/kt3k/gradle/plugin/CoverallsPlugin.groovy', reports[0].name
		assertEquals 'src/test/fixture/org/kt3k/gradle/plugin/coveralls/Application.groovy', reports[1].name
		assertEquals 'src/test/fixture/org/kt3k/gradle/plugin/coveralls/domain/Report.groovy', reports[2].name
		assertEquals 'src/test/fixture/org/kt3k/gradle/plugin/coveralls/domain/ServiceInfo.groovy', reports[3].name
		assertEquals 'src/test/fixture/org/kt3k/gradle/plugin/coveralls/domain/ServiceInfoFactory.groovy', reports[4].name
		assertEquals 'src/test/fixture/org/kt3k/gradle/plugin/coveralls/domain/SourceReport.groovy', reports[5].name
		assertEquals 'src/test/fixture/org/kt3k/gradle/plugin/coveralls/domain/SourceReportFactory.groovy', reports[6].name
	}


	@Test
	void testCreateFromCoberturaWithMultipleSourcesIncludingWrongOneXML() {

		Project project = mock Project
		CoberturaSourceReportFactory factory = new CoberturaSourceReportFactory()
		List<SourceReport> reports = factory.createReportList project, new File('src/test/fixture/coverage_with_multiple_sources_including_wrong_ones.xml')

		assertNotNull reports
		assertEquals 7, reports.size()

		assertEquals 'src/test/fixture/org/kt3k/gradle/plugin/CoverallsPlugin.groovy', reports[0].name
		assertEquals 'src/test/fixture/org/kt3k/gradle/plugin/coveralls/Application.groovy', reports[1].name
		assertEquals 'src/test/fixture/org/kt3k/gradle/plugin/coveralls/domain/Report.groovy', reports[2].name
		assertEquals 'src/test/fixture/org/kt3k/gradle/plugin/coveralls/domain/ServiceInfo.groovy', reports[3].name
		assertEquals 'src/test/fixture/org/kt3k/gradle/plugin/coveralls/domain/ServiceInfoFactory.groovy', reports[4].name
		assertEquals 'src/test/fixture/org/kt3k/gradle/plugin/coveralls/domain/SourceReport.groovy', reports[5].name
		assertEquals 'src/test/fixture/org/kt3k/gradle/plugin/coveralls/domain/SourceReportFactory.groovy', reports[6].name
	}


	@Test
	void testCreateFromCoberturaWithNonexistentFileEntry() {

		// mock up project
		Project project = mock Project

		CoberturaSourceReportFactory factory = new CoberturaSourceReportFactory()

		// create source report
		List<SourceReport> reports = factory.createReportList project, new File('src/test/fixture/coverage_with_nonexistent_file_entry.xml')

		assertNotNull reports
	}


	@Test
	void testLineHitsAddition() {

		// test line hits addition
		// see <line> tags in src/test/fixture/coverage_add_hits.xml

		Project project = mock Project
		CoberturaSourceReportFactory factory = new CoberturaSourceReportFactory()
		List<SourceReport> reports = factory.createReportList project, new File('src/test/fixture/coverage_add_hits.xml')

		assertNotNull reports

		assertEquals 'src/test/fixture/org/kt3k/gradle/plugin/CoverallsPlugin.groovy', reports[0].name
		assertEquals null, reports[0].coverage[0]
		assertEquals null, reports[0].coverage[1]
		assertEquals null, reports[0].coverage[2]
		assertEquals null, reports[0].coverage[3]
		assertEquals null, reports[0].coverage[4]
		assertEquals null, reports[0].coverage[5]
		assertEquals null, reports[0].coverage[6]
		assertEquals null, reports[0].coverage[7]
		assertEquals null, reports[0].coverage[8]
		assertEquals 11, reports[0].coverage[9]
		assertEquals null, reports[0].coverage[10]
		assertEquals null, reports[0].coverage[11]
		assertEquals null, reports[0].coverage[12]
		assertEquals 9, reports[0].coverage[13]
		assertEquals null, reports[0].coverage[14]
		assertEquals 9, reports[0].coverage[15]
		assertEquals null, reports[0].coverage[16]
		assertEquals null, reports[0].coverage[17]
		assertEquals null, reports[0].coverage[18]
	}

}
