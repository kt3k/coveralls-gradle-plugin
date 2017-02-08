package org.kt3k.gradle.plugin.coveralls.domain

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*
import org.kt3k.gradle.plugin.CoverallsPluginExtension

class PITSourceReportFactoryTest {

	Project project

	@Before
	public void setUp() {

		// fake a project
		project = ProjectBuilder.builder().build()

		// create coveralls extension
		project.extensions.create('coveralls', CoverallsPluginExtension)
	}

	@Test
	public void testCreateFromMutationsXML() {

		List<SourceReport> reports = PITSourceReportFactory.createReportList(
            [new File('src/test/fixture')],
            new File ('src/test/fixture/mutations.xml'))

		assertNotNull reports
		assertEquals 2, reports.size()

		assertEquals 'src/test/fixture/org/kt3k/bankaccount/BankAccount.java', reports[0].name
		assertEquals 30, reports[0].coverage.size()
		assertEquals 4, reports[0].coverage.findAll{ it != null }.size()
		assertEquals 3, reports[0].coverage.findAll{ it != null }.sum()
		assertEquals 'src/test/fixture/org/kt3k/bankaccount/TransferContext.java', reports[1].name
		assertEquals 45, reports[1].coverage.size()
		assertEquals 4, reports[1].coverage.findAll{ it != null }.size()
		assertEquals 4, reports[1].coverage.findAll{ it != null }.sum()
	}

}
