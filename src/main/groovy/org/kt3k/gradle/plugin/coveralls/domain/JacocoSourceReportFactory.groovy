package org.kt3k.gradle.plugin.coveralls.domain

import org.gradle.api.Project
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.scala.ScalaPlugin

/**
 * Factory class of SourceReport for JaCoCo report file.
 */
class JacocoSourceReportFactory implements SourceReportFactory {

	@Override
	public List<SourceReport> createReportList(Project project, File jacocoReportFile) {

		List<File> targetSrcDirs = createTargetSrcDirs(project)

		return createReportList(targetSrcDirs.sort(), jacocoReportFile)

	}

	static List<File> createTargetSrcDirs(Project project) {

		List<File> targetSrcDirs = new ArrayList<File>()

        project.plugins.matching { it.class.name == "com.android.build.gradle.BasePlugin" }.all {
            targetSrcDirs += project.android.sourceSets.main.java.getSrcDirs()
        }

		project.plugins.withType(JavaPlugin) {
			targetSrcDirs += project.sourceSets.main.java.srcDirs
		}

		project.plugins.withType(GroovyPlugin) {
			targetSrcDirs += project.sourceSets.main.groovy.srcDirs
		}

		project.plugins.withType(ScalaPlugin) {
			targetSrcDirs += project.sourceSets.main.scala.srcDirs
		}

		return project.extensions.coveralls.sourceDirs + targetSrcDirs.sort()
	}

	static List<SourceReport> createReportList(List<File> srcDirs, File jacocoReportFile) {
		// create parser
		XmlParser parser = new XmlParserFactory().xmlParser

		// parse
		Node report = parser.parse(jacocoReportFile)

		Map<String, Map<Integer, Integer>> a = [:]

		(report.group.package + report.package).each { pkg ->

			pkg.sourcefile.each { sf ->
				Map<Integer, Integer> cov = a.get("${pkg.@name}/${sf.@name}", [:])

				sf.line.each { line ->
					Integer lineIndex = line.@nr.toInteger() - 1

					// jacoco doesn't count hits
					if (line.@ci.toInteger() > 0) {
						cov[lineIndex] = 1
					} else {
						cov[lineIndex] = 0
					}
				}
			}
		}

		List<SourceReport> reports = new ArrayList<SourceReport>()

		// find actual source files
		// and create the list of SourceReport instances
		a.each { String filename, Map<Integer, Integer> cov ->

			File sourceFile = srcDirs.collect { new File(it, filename) }.find { it.exists() }

			if (sourceFile == null) {
				return
			}

			String source = sourceFile.text

			List r = [null] * source.readLines().size()

			cov.each { Integer line, Integer hits ->
				r[line] = hits
			}

			// Compute relative path from . via https://gist.github.com/ysb33r/5804364
			String relPath = new File('.').toURI().relativize( sourceFile.toURI() ).toString()
			reports.add new SourceReport(relPath, source, r)
		}

		return reports
	}
}
