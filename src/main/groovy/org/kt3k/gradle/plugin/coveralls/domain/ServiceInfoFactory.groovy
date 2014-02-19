package org.kt3k.gradle.plugin.coveralls.domain

class ServiceInfoFactory {

    public static createFromEnvVar(Map<String, String> env) {

        if (env.get('TRAVIS') == 'true' && env.get('TRAVIS_JOB_ID') != null) {
            if (env.get('COVERALLS_REPO_TOKEN') != null) {
                return new ServiceInfo('travis-pro', env.get('TRAVIS_JOB_ID'), env.get('COVERALLS_REPO_TOKEN'))
            } else {
                return new ServiceInfo('travis-ci', env.get('TRAVIS_JOB_ID'), null)
            }
        }

        // cannot create service info from env var
        return null

    }

}
