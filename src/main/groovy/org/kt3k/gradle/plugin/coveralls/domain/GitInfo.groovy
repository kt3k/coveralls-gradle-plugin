package org.kt3k.gradle.plugin.coveralls.domain

import groovy.transform.TupleConstructor

@TupleConstructor
class GitInfo {
    Head head
    String branch
    List<Remote> remotes

    @TupleConstructor
    public static class Head {
        String id
        String authorName
        String authorEmail
        String committerName
        String committerEmail
        String message
    }

    @TupleConstructor
    public static class Remote {
        String name
        String url
    }
}
