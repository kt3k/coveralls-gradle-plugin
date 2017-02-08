package org.kt3k.gradle.plugin.coveralls.domain

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

/**
 * Factory class for SourceReport for PIT report file.
 */
class PITSourceReportFactory implements SourceReportFactory {

    @Override
    List<SourceReport> createReportList(Project project, File reportFile) {
        // Build a PATH of source directories.
        List<File> sourceDirectories = project.extensions.coveralls.sourceDirs

        project.plugins.withType(JavaPlugin) {
            sourceDirectories += project.sourceSets.main.java.srcDirs
        }

        return createReportList(sourceDirectories, reportFile)
    }

    // This method has been separated for testing.
    static List<SourceReport> createReportList(
        List<File> sourceDirectories, File reportFile)
    {
        def mutations = new XmlSlurper().parse(reportFile)

        // mapping of [filename] => [line] => [hits]
        Map<String, Map<Integer, Integer>> hitsPerLineMapPerFile = [:]

        mutations.mutation.each() { mutation ->
            def status = mutation.@status
            Integer hit
            if (status == 'KILLED') {
                hit = 1
            } else if (status == 'LIVED' || status == 'NO_COVERAGE') {
                hit = 0
            } else {
                // Leave null for lines that could not be mutated.
                return
            }

            // PIT reports only the source file's base name, which might not be
            // unique. Assume the source file's directory can be derived from
            // the package name.
            // This pattern matches more than valid package names,
            // but we're assuming the name is valid if it compiled.
            def matcher = (mutation.mutatedClass.text() =~ /(?:[a-zA-Z0-9_]+\.)*/)
            if (!matcher.find()) {
                // Cannot parse the class name.
                // TODO: Give a warning to the user at least.
                return
            }
            def packageName = matcher.group(0)
            def filename = packageName.replace('.', File.separator) + mutation.sourceFile.text()

            def hitsPerLine = hitsPerLineMapPerFile.get(filename, [:])
            Integer lineNumber = mutation.lineNumber.text().toInteger() - 1
            Integer hits = hitsPerLine.get(lineNumber, 0)
            hitsPerLine[lineNumber] = hits + hit
        }

        List<SourceReport> reports = new ArrayList<SourceReport>()

        hitsPerLineMapPerFile.each { String filename, Map<Integer, Integer> hitsPerLine ->

            // find actual source file from directory candidates
            String sourceFilename = findSourceFile(sourceDirectories, filename)

            if (sourceFilename == null) {
                // if sourceFilename is not found then ignore the entry
                return
            }

            File sourceFile = new File(sourceFilename)
            String sourceText = sourceFile.text

            // create hits per line list
            List<Integer> hitsArray = [null] * sourceText.readLines().size()

            hitsPerLine.each { Integer line, Integer hits ->
                hitsArray[line] = hits
            }

            reports.add new SourceReport(sourceFilename, sourceText, hitsArray)
        }

        return reports
    }

    /**
     * Finds the actual source file path and returns File object
     *
     * @param sourceDirs the list of candidate source dirs
     * @param filename the file name to search
     * @return found File object
     */
    private static String findSourceFile(List<File> sourceDirs, String filename) {
        return sourceDirs.collect { new File(it, filename) }.find { it.exists() }
    }
}
