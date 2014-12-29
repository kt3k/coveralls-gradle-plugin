package org.kt3k.gradle.plugin.coveralls.domain

import org.junit.Test

import static org.junit.Assert.assertNotNull


class GitInfoFactoryTest {

    @Test
    void testRepoCanBeParsed() {
        assertNotNull GitInfoFactory.load(new File(GitInfoFactoryTest.class.getResource("/").toURI())).head.id
    }

}
