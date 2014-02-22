package org.kt3k.gradle.plugin.coveralls.domain

import org.gradle.api.Project

/**
 * The interface for factory classes of SourceReport.
 */
interface SourceReportFactory {
	List<SourceReport> createReportList(Project project, File file)
}
