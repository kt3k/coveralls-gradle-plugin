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
    void testCreateFromEnvVarTravisCiWithRepoToken() {
        // test the case of travis-pro
        ServiceInfo serviceInfo = ServiceInfoFactory.createFromEnvVar(
                CI_NAME: 'travis-ci',
                TRAVIS: 'true',
                TRAVIS_JOB_ID: '12345678',
                COVERALLS_REPO_TOKEN: 'ABCDEF',
                TRAVIS_PULL_REQUEST: '3232',
                TRAVIS_BRANCH: 'branchX')

        assertEquals 'travis-ci', serviceInfo.serviceName
        assertEquals '12345678', serviceInfo.serviceJobId
        assertEquals 'ABCDEF', serviceInfo.repoToken

        assertEquals '12345678', serviceInfo.environment['travis_job_id']
        assertEquals '3232', serviceInfo.environment['travis_pull_request']
        assertEquals 'branchX', serviceInfo.environment['branch']
    }

    @Test
    void testCreateFromEnvVarTravisNoJobId() {
        // test the case that travis == true but job_id is not available
        assertNull ServiceInfoFactory.createFromEnvVar(TRAVIS: 'true')
    }


    @Test
    void testCreateFromEnvVarTravisPro() {
        // test the case of travis-pro
        ServiceInfo serviceInfo = ServiceInfoFactory.createFromEnvVar(
                TRAVIS: 'true',
                TRAVIS_JOB_ID: '12345678',
                COVERALLS_REPO_TOKEN: 'ABCDEF',
                TRAVIS_PULL_REQUEST: '3232',
                TRAVIS_BRANCH: 'branchX')

        assertEquals 'travis-pro', serviceInfo.serviceName
        assertEquals '12345678', serviceInfo.serviceJobId
        assertEquals 'ABCDEF', serviceInfo.repoToken

        assertEquals '12345678', serviceInfo.environment['travis_job_id']
        assertEquals '3232', serviceInfo.environment['travis_pull_request']
        assertEquals 'branchX', serviceInfo.environment['branch']
    }


    @Test
    void testCreateFromEnvVarCircleci() {
        // test the case of travis-pro
        ServiceInfo serviceInfo = ServiceInfoFactory.createFromEnvVar(
                CIRCLECI: 'true',
                CIRCLE_BUILD_NUM: '12345678',
                COVERALLS_REPO_TOKEN: 'ABCDEF',
                CI_PULL_REQUEST: 'https://github.com/kt3k/coveralls-gradle-plugin/pull/51',
                CIRCLE_BRANCH: 'branchX',
                CIRCLE_SHA1: '231asdfadsf424')

        assertEquals 'circleci', serviceInfo.serviceName
        assertEquals '12345678', serviceInfo.serviceNumber
        assertEquals 'ABCDEF', serviceInfo.repoToken
        assertEquals '51', serviceInfo.servicePullRequest
        assertEquals 'branchX', serviceInfo.environment['branch']
        assertEquals '231asdfadsf424', serviceInfo.environment['commit_sha']
        assertEquals '12345678', serviceInfo.environment['circleci_build_num']
    }

    @Test
    void testCreateFromEnvVarSnap() {
        // test the case of snap-ci
        ServiceInfo serviceInfo = ServiceInfoFactory.createFromEnvVar(
                SNAP_CI: "true",
                SNAP_PIPELINE_COUNTER: "12345678",
                COVERALLS_REPO_TOKEN: 'ABCDEF',
                SNAP_BRANCH: "branchX",
                SNAP_STAGE_NAME: "test",
                SNAP_COMMIT: "231asdfadsf424",
                SNAP_PULL_REQUEST_NUMBER: "42"
        )

        assertEquals 'snapci', serviceInfo.serviceName
        assertEquals '12345678', serviceInfo.serviceNumber
        assertEquals 'branchX', serviceInfo.serviceBranch
        assertEquals 'ABCDEF', serviceInfo.repoToken
        assertEquals '42', serviceInfo.servicePullRequest

        assertEquals '12345678', serviceInfo.environment['pipeline_counter']
        assertEquals 'test', serviceInfo.environment['stage_name']
        assertEquals 'branchX', serviceInfo.environment['branch']
        assertEquals '231asdfadsf424', serviceInfo.environment['commit_sha']
    }

    @Test
    void testCreateFromEnvVarBitrise() {
        // test the case of bitrise
        ServiceInfo serviceInfo = ServiceInfoFactory.createFromEnvVar(
                BITRISE_BUILD_NUMBER: "123456789",
                BITRISE_BUILD_URL: "https://www.bitrise.io/build/1234567890abcdef",
                BITRISE_GIT_BRANCH: "branchX",
                BITRISE_GIT_COMMIT: "231asdfadsf424",
                BITRISE_PULL_REQUEST: "11",
                COVERALLS_REPO_TOKEN: 'ABCDEF',
        )

        assertEquals 'bitrise', serviceInfo.serviceName
        assertEquals '123456789', serviceInfo.serviceNumber
        assertEquals 'branchX', serviceInfo.serviceBranch
        assertEquals 'ABCDEF', serviceInfo.repoToken
        assertEquals '11', serviceInfo.servicePullRequest

        assertEquals 'branchX', serviceInfo.environment['branch']
        assertEquals '231asdfadsf424', serviceInfo.environment['commit_sha']
    }

    @Test
    void testCreateFromEnvVarBuildkite() {
        // test the case of buildkite
        ServiceInfo serviceInfo = ServiceInfoFactory.createFromEnvVar(
                BUILDKITE: "true",
                BUILDKITE_BUILD_NUMBER: "123",
                BUILDKITE_BUILD_URL: "https://buildkite.com/your-org/your-repo/builds/123",
                BUILDKITE_BUILD_ID: "58b195c0-94aa-43ba-ae43-00b93c29a8b7",
                BUILDKITE_BRANCH: "branchX",
                BUILDKITE_COMMIT: "231asdfadsf424",
                BUILDKITE_PULL_REQUEST: "11",
                COVERALLS_REPO_TOKEN: 'ABCDEF',
        )

        assertEquals 'ABCDEF', serviceInfo.repoToken
        assertEquals 'buildkite', serviceInfo.serviceName
        assertEquals '123', serviceInfo.serviceNumber
        assertEquals 'https://buildkite.com/your-org/your-repo/builds/123', serviceInfo.serviceBuildUrl
        assertEquals 'branchX', serviceInfo.serviceBranch
        assertEquals '11', serviceInfo.servicePullRequest
        assertEquals '58b195c0-94aa-43ba-ae43-00b93c29a8b7', serviceInfo.serviceJobId

        assertEquals 'branchX', serviceInfo.environment['branch']
        assertEquals '231asdfadsf424', serviceInfo.environment['commit_sha']
    }

    @Test
    void testCreateFromEnvVarJenkinsNonPullRequest() {
        // test the case of travis-pro
        ServiceInfo serviceInfo = ServiceInfoFactory.createFromEnvVar(
                JENKINS_URL: 'abc',
                BUILD_NUMBER: '12345678',
                COVERALLS_REPO_TOKEN: 'ABCDEF',
                GIT_BRANCH: 'branchX',
                GIT_COMMIT: '231asdfadsf424')

        assertEquals 'jenkins', serviceInfo.serviceName
        assertEquals '12345678', serviceInfo.serviceNumber
        assertEquals 'ABCDEF', serviceInfo.repoToken
        assertEquals 'branchX', serviceInfo.environment['branch']
        assertEquals '231asdfadsf424', serviceInfo.environment['commit_sha']
        assertEquals '12345678', serviceInfo.environment['jenkins_build_num']
    }

    @Test
    void testCreateFromEnvVarJenkinsPullRequest() {
        // test the case of travis-pro
        ServiceInfo serviceInfo = ServiceInfoFactory.createFromEnvVar(
                JENKINS_URL: 'abc',
                BUILD_NUMBER: '12345678',
                COVERALLS_REPO_TOKEN: 'ABCDEF',
                GIT_BRANCH: 'branchX',
                GIT_COMMIT: '231asdfadsf424',
                ghprbPullId: '170',
                ghprbActualCommit: '424fsdafdsa132')

        assertEquals 'jenkins', serviceInfo.serviceName
        assertEquals '12345678', serviceInfo.serviceNumber
        assertEquals 'ABCDEF', serviceInfo.repoToken
        assertEquals 'branchX', serviceInfo.environment['branch']
        assertEquals '424fsdafdsa132', serviceInfo.environment['commit_sha']
        assertEquals '12345678', serviceInfo.environment['jenkins_build_num']
        assertEquals '170', serviceInfo.servicePullRequest
    }

    @Test
    void testCreateFromEnvVarFromRepoToken() {
        // test the case of the environment other than travis with repo token
        ServiceInfo serviceInfo = ServiceInfoFactory.createFromEnvVar('COVERALLS_REPO_TOKEN': 'ABCDEF')

        assertEquals 'other', serviceInfo.serviceName
        assertEquals null, serviceInfo.serviceJobId
        assertEquals 'ABCDEF', serviceInfo.repoToken
    }

    @Test
    void testCreateFromEnvVarFromRepoToken2() {

        // "other" CI that supplies env vars (like Codeship)
        ServiceInfo serviceInfo = ServiceInfoFactory.createFromEnvVar(
                COVERALLS_REPO_TOKEN: 'ABCDEF',
                CI_NAME: 'name',
                CI_BUILD_NUMBER: 'build_number',
                CI_BUILD_URL: 'build_url',
                CI_BRANCH: 'branch',
                CI_PULL_REQUEST: 'pull_request',
        )

        assertEquals serviceInfo, new ServiceInfo(
                serviceName: 'name',
                serviceNumber: 'build_number',
                serviceBuildUrl: 'build_url',
                serviceBranch: 'branch',
                servicePullRequest: 'pull_request',
                repoToken: 'ABCDEF')
    }

    @Test
    void testCreateFromEnvVarGitHubActionBranch() {
        // test the case of github-actions
        ServiceInfo serviceInfo = ServiceInfoFactory.createFromEnvVar(
                COVERALLS_REPO_TOKEN: 'ABCDEF',
                GITHUB_WORKFLOW: 'Java CI',
                GITHUB_WORKSPACE: '/home/runner/work/ci/ci',
                GITHUB_EVENT_NAME: 'push',
                GITHUB_ACTIONS: 'true',
                GITHUB_EVENT_PATH: '/home/runner/work/_temp/_github_workflow/event.json',
                GITHUB_ACTION: 'run',
                GITHUB_ACTOR: 'musketyr',
                GITHUB_REF: 'refs/heads/master',
                GITHUB_REPOSITORY: 'agorapulse/ci',
                GITHUB_SHA: '8eee24aac46ace5cd156d351062dfce68e57e49e'
        )

        assertEquals 'github-actions', serviceInfo.serviceName
        assertEquals "https://github.com/agorapulse/ci/commit/8eee24aac46ace5cd156d351062dfce68e57e49e/checks", serviceInfo.serviceBuildUrl
        assertEquals 'ABCDEF', serviceInfo.repoToken
        assertEquals 'master', serviceInfo.environment['branch']
        assertEquals '8eee24aac46ace5cd156d351062dfce68e57e49e', serviceInfo.environment['commit_sha']
    }

    @Test
    void testCreateFromEnvVarGitHubActionPullRequest() {
        // test the case of github-actions
        ServiceInfo serviceInfo = ServiceInfoFactory.createFromEnvVar(
                COVERALLS_REPO_TOKEN: 'ABCDEF',
                GITHUB_WORKFLOW: 'Java CI',
                GITHUB_WORKSPACE: '/home/runner/work/ci/ci',
                GITHUB_EVENT_NAME: 'pull_request',
                GITHUB_ACTIONS: 'true',
                GITHUB_EVENT_PATH: '/home/runner/work/_temp/_github_workflow/event.json',
                GITHUB_ACTION: 'run',
                GITHUB_ACTOR: 'musketyr',
                GITHUB_REF: 'refs/pull/1/merge',
                GITHUB_REPOSITORY: 'agorapulse/ci',
                GITHUB_SHA: '8eee24aac46ace5cd156d351062dfce68e57e49e'
        )

        assertEquals 'github-actions', serviceInfo.serviceName
        assertEquals "https://github.com/agorapulse/ci/commit/8eee24aac46ace5cd156d351062dfce68e57e49e/checks", serviceInfo.serviceBuildUrl
        assertEquals '1', serviceInfo.servicePullRequest
        assertEquals 'ABCDEF', serviceInfo.repoToken
        assertEquals '8eee24aac46ace5cd156d351062dfce68e57e49e', serviceInfo.environment['commit_sha']
    }

}
