package org.kt3k.gradle.plugin.coveralls.domain

import org.gradle.api.Project

class CoberturaSourceReportFactory implements SourceReportFactory {

	@Override
	List<SourceReport> createReportList(Project project, File file) {
		Node coverage = new XmlParser().parse(file)
		List<String> sourceDirectories = coverage.sources.source*.text()

		Map a = [:]

		coverage.packages.package.classes.class.each() { cls ->
			Map cov = a.get(cls.'@filename', [:])

			cls.lines.line.each() {
				Integer hits = cov.get(it.'@number'.toInteger() - 1, 0)
				cov[it.'@number'.toInteger() - 1] = hits + it.'@hits'.toInteger()
			}
		}

		List<SourceReport> reports = new ArrayList<SourceReport>()

		a.each { String filename, Map<Integer, Integer> cov ->
			String source = actualSourceFile(sourceDirectories, filename).text

			List r = [null] * source.readLines().size()
			cov.each { Integer line, Integer hits ->
				r[line] = hits
			}

			reports.add new SourceReport(filename, source, r)
		}

		return reports

	}

	private static File actualSourceFile(List<String> sourceDirs, String filename) {
		sourceDirs.collect { new File(it + '/' + filename) }.find { it.exists() }
	}
}
