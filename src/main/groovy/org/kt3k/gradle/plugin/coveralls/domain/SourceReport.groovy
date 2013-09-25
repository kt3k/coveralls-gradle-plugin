package org.kt3k.gradle.plugin.coveralls.domain

// model for coveralls io report's source file report
class SourceReport {
    String name;
    String source;
    List<Integer> coverage;

    public SourceReport(String name, String source, List<Integer> coverage) {
        this.name = name;
        this.source = source;
        this.coverage = coverage;
    }

}
