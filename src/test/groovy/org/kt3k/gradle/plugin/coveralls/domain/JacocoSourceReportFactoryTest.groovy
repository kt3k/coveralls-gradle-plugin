package org.kt3k.gradle.plugin.coveralls.domain

import org.gradle.api.Project
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.scala.ScalaPlugin
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.*

class JacocoSourceReportFactoryTest {

	@Test
	public void testCreateReport() throws Exception {
		List<SourceReport> reports = JacocoSourceReportFactory.createReportList([new File('src/main/groovy')], new File('src/test/fixture/jacocoTestReport.xml'))

		assertNotNull reports
		assertEquals 7, reports.size()

		reports.sort { it.name }
		assertEquals 'org/kt3k/gradle/plugin/CoverallsPlugin.groovy', reports[0].name
		assertEquals 'org/kt3k/gradle/plugin/coveralls/domain/CoberturaSourceReportFactory.groovy', reports[1].name
		assertEquals 'org/kt3k/gradle/plugin/coveralls/domain/JacocoSourceReportFactory.groovy', reports[2].name
		assertEquals 'org/kt3k/gradle/plugin/coveralls/domain/Report.groovy', reports[3].name
		assertEquals 'org/kt3k/gradle/plugin/coveralls/domain/ServiceInfo.groovy', reports[4].name
		assertEquals 'org/kt3k/gradle/plugin/coveralls/domain/ServiceInfoFactory.groovy', reports[5].name
		assertEquals 'org/kt3k/gradle/plugin/coveralls/domain/SourceReport.groovy', reports[6].name
	}


	@Test
	public void testCreateReportList() throws Exception {
		Project project = ProjectBuilder.builder().build()
		project.plugins.apply(GroovyPlugin)

		List<SourceReport> reports = new JacocoSourceReportFactory().createReportList(project, new File('src/test/fixture/jacocoTestReport.xml'))

		assertNotNull(reports)
	}


	@Test
	public void testCreateReportForJavaPlugin() throws Exception {
		Project project = ProjectBuilder.builder().build()
		project.plugins.apply(JavaPlugin)

		List<File> srcDirs = JacocoSourceReportFactory.createTargetSrcDirs(project)

		srcDirs.each {
			assertTrue it.absolutePath.endsWith("src/main/java")
		}
	}


	@Test
	public void testCreateReportForGroovyPlugin() throws Exception {
		Project project = ProjectBuilder.builder().build()
		project.plugins.apply(GroovyPlugin)

		List<File> srcDirs = JacocoSourceReportFactory.createTargetSrcDirs(project)

		srcDirs.each {
			assertTrue it.absolutePath.endsWith("src/main/java") || it.absolutePath.endsWith('src/main/groovy')
		}
	}


	@Test
	public void testCreateReportForScalaPlugin() throws Exception {
		Project project = ProjectBuilder.builder().build()
		project.plugins.apply(ScalaPlugin)

		List<File> srcDirs = JacocoSourceReportFactory.createTargetSrcDirs(project)

		srcDirs.each {
			assertTrue it.absolutePath.endsWith("src/main/java") || it.absolutePath.endsWith('src/main/scala')
		}
	}
}
