package org.kt3k.gradle.plugin.coveralls.domain

import org.gradle.api.Project

interface SourceReportFactory {
	List<SourceReport> createReportList(Project project, File file)
}
