package org.kt3k.gradle.plugin.coveralls.domain

import org.gradle.api.Project
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.scala.ScalaPlugin
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import org.junit.Before
import org.kt3k.gradle.plugin.CoverallsPluginExtension

import static org.junit.Assert.*

class JacocoSourceReportFactoryTest {

	Project project

	@Before
	public void setUp() {

		// fake a project
		project = ProjectBuilder.builder().build()

		// create coveralls extension
		project.extensions.create('coveralls', CoverallsPluginExtension)
	}

	@Test
	public void testCreateReport() throws Exception {
		List<SourceReport> reports = JacocoSourceReportFactory.createReportList([new File('src/main/groovy')], new File('src/test/fixture/jacocoTestReport.xml'))

		assertNotNull reports
		assertEquals 7, reports.size()

		reports.sort { it.name }
		assertEquals 'src/main/groovy/org/kt3k/gradle/plugin/CoverallsPlugin.groovy', reports[0].name
		assertEquals 'src/main/groovy/org/kt3k/gradle/plugin/coveralls/domain/CoberturaSourceReportFactory.groovy', reports[1].name
		assertEquals 'src/main/groovy/org/kt3k/gradle/plugin/coveralls/domain/JacocoSourceReportFactory.groovy', reports[2].name
		assertEquals 'src/main/groovy/org/kt3k/gradle/plugin/coveralls/domain/Report.groovy', reports[3].name
		assertEquals 'src/main/groovy/org/kt3k/gradle/plugin/coveralls/domain/ServiceInfo.groovy', reports[4].name
		assertEquals 'src/main/groovy/org/kt3k/gradle/plugin/coveralls/domain/ServiceInfoFactory.groovy', reports[5].name
		assertEquals 'src/main/groovy/org/kt3k/gradle/plugin/coveralls/domain/SourceReport.groovy', reports[6].name
	}


	@Test
	public void testCreateReportList() throws Exception {
		project.plugins.apply(GroovyPlugin)

		List<SourceReport> reports = new JacocoSourceReportFactory().createReportList(project, new File('src/test/fixture/jacocoTestReport.xml'))

		assertNotNull(reports)
	}


	@Test
	public void testCreateReportForJavaPlugin() throws Exception {

		project.plugins.apply(JavaPlugin)

		List<File> srcDirs = JacocoSourceReportFactory.createTargetSrcDirs(project)

		srcDirs.each {
			assertTrue it.absolutePath.endsWith("src/main/java")
		}
	}


	@Test
	public void testCreateReportForGroovyPlugin() throws Exception {

		project.plugins.apply(GroovyPlugin)

		List<File> srcDirs = JacocoSourceReportFactory.createTargetSrcDirs(project)

		srcDirs.each {
			assertTrue it.absolutePath.endsWith("src/main/java") || it.absolutePath.endsWith('src/main/groovy')
		}
	}


	@Test
	public void testCreateReportForScalaPlugin() throws Exception {

		project.plugins.apply(ScalaPlugin)

		List<File> srcDirs = JacocoSourceReportFactory.createTargetSrcDirs(project)

		srcDirs.each {
			assertTrue it.absolutePath.endsWith("src/main/java") || it.absolutePath.endsWith('src/main/scala')
		}
	}
}
