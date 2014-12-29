package org.kt3k.gradle.plugin.coveralls.domain

/**
 * ServiceInfoFactory is factory class of ServiceInfo.
 */
class ServiceInfoFactory {

    /**
     * Create ServiceInfo instance from environmental variables.
     *
     * @param env environmental variables
     * @return service information of current environment
     */
    public static ServiceInfo createFromEnvVar(Map<String, String> env) {

        if (repoTokenIsSet(env)) {
            if (envIsTravis(env)) {
                return new ServiceInfo(
                        serviceName: 'travis-pro',
                        serviceJobId: env.get('TRAVIS_JOB_ID'),
                        repoToken: env.get('COVERALLS_REPO_TOKEN'),
                        environment: [
                                'travis_job_id'      : env.get('TRAVIS_JOB_ID'),
                                'travis_pull_request': env.get('TRAVIS_PULL_REQUEST')]
                )
            } else if (envIsCircleci(env)) {
                return new ServiceInfo(
                        serviceName: 'circleci',
                        serviceNumber: env.get('CIRCLE_BUILD_NUM'),
                        repoToken: env.get('COVERALLS_REPO_TOKEN'),
                        environment: [
                                'circleci_build_num': env.get('CIRCLE_BUILD_NUM'),
                                'branch'            : env.get('CIRCLE_BRANCH'),
                                'commit_sha'        : env.get('CIRCLE_SHA1')]
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
            }
        }

        // cannot create service info from environmental variables. (no repo_token, not travis)
        return null

    }

    private static boolean envIsTravis(Map<String, String> env) {
        env.get('TRAVIS') == 'true' && env.get('TRAVIS_JOB_ID') != null
    }

    private static boolean envIsCircleci(Map<String, String> env) {
        env.get('CIRCLECI') == 'true'
    }

    private static boolean repoTokenIsSet(Map<String, String> env) {
        env.get('COVERALLS_REPO_TOKEN') != null
    }

}
