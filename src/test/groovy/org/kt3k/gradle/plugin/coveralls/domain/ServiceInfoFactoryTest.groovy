package org.kt3k.gradle.plugin.coveralls.domain

import org.junit.Test
import static org.junit.Assert.*

class ServiceInfoFactoryTest {

    @Test
    void testCreateFromEnvVarNoServiceAvaiable() {
        // test the case no service is available
        assertNull ServiceInfoFactory.createFromEnvVar([:])
    }

    @Test
    void testCreateFromEnvVarTravis() {
        // test the case of travis
        ServiceInfo serviceInfo = ServiceInfoFactory.createFromEnvVar TRAVIS: 'true', TRAVIS_JOB_ID: '12345678'

        assertEquals 'travis-ci', serviceInfo.serviceName
        assertEquals '12345678', serviceInfo.serviceJobId
        assertEquals null, serviceInfo.repoToken
    }

    @Test
    void testCreateFromEnvVarTravisNoJobId() {
        // test the case that travis == true but job_id is not available
        assertNull ServiceInfoFactory.createFromEnvVar(TRAVIS: 'true')
    }


    @Test
    void testCreateFromEnvVarTravisPro() {
        // test the case of travis-pro
        serviceInfo = ServiceInfoFactory.createFromEnvVar TRAVIS: 'true', TRAVIS_JOB_ID: '12345678', COVERALLS_REPO_TOKEN: 'ABCDEF'

        assertEquals 'travis-pro', serviceInfo.serviceName
        assertEquals '12345678', serviceInfo.serviceJobId
        assertEquals 'ABCDEF', serviceInfo.repoToken
    }


    @Test
    void testCreateFromEnvVarFromRepoToken() {
        // test the case of the environment other than travis with repo token
        serviceInfo = ServiceInfoFactory.createFromEnvVar('COVERALLS_REPO_TOKEN': 'ABCDEF')

        assertEquals 'other', serviceInfo.serviceName
        assertEquals null, serviceInfo.serviceJobId
        assertEquals 'ABCDEF', serviceInfo.repoToken
    }

    @Test
    void testCreateFromEnvVarFromRepoToken2() {

        // "other" CI that supplies env vars (like Codeship)
        serviceInfo = ServiceInfoFactory.createFromEnvVar(
                COVERALLS_REPO_TOKEN: 'ABCDEF',
                CI_NAME: 'name',
                CI_BUILD_NUMBER: 'build_number',
                CI_BUILD_URL: 'build_url',
                CI_BRANCH: 'branch',
                CI_PULL_REQUEST: 'pull_request',
        )

        assert serviceInfo == new ServiceInfo('name', 'build_number', 'build_url', 'branch', 'pull_request', 'ABCDEF')
    }

}
