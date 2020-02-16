package org.kt3k.gradle.plugin.coveralls.domain

import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * ServiceInfoFactory is factory class of ServiceInfo.
 */
class ServiceInfoFactory {

    private static final Pattern GITHUB_ACTIONS_PULL_REQUEST_PATTERN = Pattern.compile("refs/pull/(\\d+)/merge")
    private static final Pattern GITHUB_ACTIONS_BRANCH_PATTERN = Pattern.compile("refs/.*?/(.*)")

    /**
     * Create ServiceInfo instance from environmental variables.
     *
     * @param env environmental variables
     * @return service information of current environment
     */
    public static ServiceInfo createFromEnvVar(Map<String, String> env) {

        /** the logger */
        Logger logger = Logging.getLogger('coveralls-logger')

        if (repoTokenIsSet(env)) {
            if (envIsTravis(env)) {
                return new ServiceInfo(
                        serviceName: env.get('CI_NAME') ?: 'travis-pro',
                        serviceJobId: env.get('TRAVIS_JOB_ID'),
                        serviceBranch: env.get('TRAVIS_BRANCH'),
                        repoToken: env.get('COVERALLS_REPO_TOKEN'),
                        environment: [
                                'travis_job_id'      : env.get('TRAVIS_JOB_ID'),
                                'travis_pull_request': env.get('TRAVIS_PULL_REQUEST'),
                                'branch'             : env.get('TRAVIS_BRANCH')]
                )
            } else if (envIsCircleci(env)) {
                String prId = env.get('CI_PULL_REQUEST')
                if (prId != null) {
                   // CircleCI CI_PULL_REQUEST is the full URL, so extract just the PR number
                   int lastSlashIdx = prId.lastIndexOf('/')
                   if (lastSlashIdx > -1 && lastSlashIdx + 1 < prId.length()) {
                       prId = prId.substring(lastSlashIdx + 1)
                   }
                }
                return new ServiceInfo(
                        serviceName: 'circleci',
                        serviceNumber: env.get('CIRCLE_BUILD_NUM'),
                        repoToken: env.get('COVERALLS_REPO_TOKEN'),
                        servicePullRequest: prId,
                        environment: [
                                'circleci_build_num': env.get('CIRCLE_BUILD_NUM'),
                                'branch'            : env.get('CIRCLE_BRANCH'),
                                'commit_sha'        : env.get('CIRCLE_SHA1')]
                )
            } else if (envIsJenkins(env)) {
                def environment = [
                        'jenkins_build_num': env.get('BUILD_NUMBER'),
                        'jenkins_build_url': env.get('BUILD_URL'),
                        'branch'           : env.get('GIT_BRANCH')]

                def serviceInfo = new ServiceInfo(
                        serviceName: 'jenkins',
                        serviceNumber: env.get('BUILD_NUMBER'),
                        serviceBuildUrl : env.get('BUILD_URL'),
                        repoToken: env.get('COVERALLS_REPO_TOKEN'),
                        environment: environment
                )

                if (envIsJenkinsPullRequest(env)) {
                    serviceInfo.servicePullRequest = env.get('ghprbPullId');
                    environment.commit_sha = env.get('ghprbActualCommit')
                } else {
                    environment.commit_sha = env.get('GIT_COMMIT')
                }

                return serviceInfo
            } else if (envIsSnap(env)) {
                return new ServiceInfo(
                        serviceName: 'snapci',
                        serviceNumber: env.get('SNAP_PIPELINE_COUNTER'),
                        serviceBranch: env.get('SNAP_BRANCH'),
                        repoToken: env.get('COVERALLS_REPO_TOKEN'),
                        servicePullRequest: env.get('SNAP_PULL_REQUEST_NUMBER'),
                        environment: [
                                'pipeline_counter': env.get('SNAP_PIPELINE_COUNTER'),
                                'stage_name'      : env.get('SNAP_STAGE_NAME'),
                                'branch'          : env.get('SNAP_BRANCH'),
                                'commit_sha'      : env.get('SNAP_COMMIT')]
                )
            } else if (envIsBitrise(env)) {
                return new ServiceInfo(
                        serviceName: 'bitrise',
                        serviceNumber: env.get('BITRISE_BUILD_NUMBER'),
                        serviceBuildUrl: env.get('BITRISE_BUILD_URL'),
                        serviceBranch: env.get('BITRISE_GIT_BRANCH'),
                        servicePullRequest: env.get('BITRISE_PULL_REQUEST'),
                        repoToken: env.get('COVERALLS_REPO_TOKEN'),
                        environment: [
                                'branch'    : env.get('BITRISE_GIT_BRANCH'),
                                'commit_sha': env.get('BITRISE_GIT_COMMIT')]
                )
            } else if (envIsGithubActions(env)) {
                String ref = env.get('GITHUB_REF')

                Matcher pullRequestMatcher = GITHUB_ACTIONS_PULL_REQUEST_PATTERN.matcher(ref)
                String prId = null
                if (pullRequestMatcher.matches()) {
                    prId = pullRequestMatcher.group(1)
                }

                Matcher branchOrTagMatcher = GITHUB_ACTIONS_BRANCH_PATTERN.matcher(ref)
                String branchOrTag = prId ? null : ref
                if (branchOrTagMatcher.matches()) {
                    branchOrTag = branchOrTagMatcher.group(1)
                }

                return new ServiceInfo(
                    serviceName: 'github-actions',
                    servicePullRequest: prId,
                    serviceBranch: branchOrTag,
                    serviceBuildUrl: "https://github.com/${env.get('GITHUB_REPOSITORY')}/commit/${env.get('GITHUB_SHA')}/checks",
                    repoToken: env.get('COVERALLS_REPO_TOKEN'),

                    environment: [
                        'branch'            : branchOrTag,
                        'commit_sha'        : env.get('GITHUB_SHA')
                    ]
                )
            } else {
                return new ServiceInfo(
                        serviceName: env['CI_NAME'] ?: 'other',
                        serviceNumber: env['CI_BUILD_NUMBER'],
                        serviceBuildUrl: env['CI_BUILD_URL'],
                        serviceBranch: env['CI_BRANCH'],
                        servicePullRequest: env['CI_PULL_REQUEST'],
                        repoToken: env['COVERALLS_REPO_TOKEN']
                )
            }

        } else {
            if (envIsTravis(env)) {
                return new ServiceInfo(serviceName: 'travis-ci', serviceJobId: env.get('TRAVIS_JOB_ID'))
            }else {
                logger.error 'no COVERALLS_REPO_TOKEN environmental variable found'
            }
        }

        // cannot create service info from environmental variables. (no repo_token, not travis)
        return null

    }

    private static boolean envIsJenkins(Map<String, String> env) {
        env.get('JENKINS_URL') != null
    }

    private static boolean envIsJenkinsPullRequest(Map<String, String> env) {
        env.get('ghprbPullId') != null
    }

    private static boolean envIsTravis(Map<String, String> env) {
        env.get('TRAVIS') == 'true' && env.get('TRAVIS_JOB_ID') != null && (env.get('CI_NAME') ?: 'travis').startsWith('travis')
    }

    private static boolean envIsCircleci(Map<String, String> env) {
        env.get('CIRCLECI') == 'true'
    }

    private static boolean envIsSnap(Map<String, String> env) {
        env.get('SNAP_CI') == 'true'
    }

    private static boolean envIsBitrise(Map<String, String> env) {
        env.get('BITRISE_BUILD_URL') != null
    }

    private static boolean envIsGithubActions(Map<String, String> env) {
        env.get('GITHUB_ACTIONS') != null
    }

    private static boolean repoTokenIsSet(Map<String, String> env) {
        env.get('COVERALLS_REPO_TOKEN') != null
    }

}
