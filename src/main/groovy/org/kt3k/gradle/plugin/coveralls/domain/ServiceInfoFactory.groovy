package org.kt3k.gradle.plugin.coveralls.domain


class ServiceInfoFactory {

    public static createFromEnvVar() {

        if (System.getenv('TRAVIS') == 'true') {
            return new ServiceInfo('travis-ci', System.getenv('TRAVIS_JOB_ID'))
        }

        // cannot create service info from env var
        return null

    }

}
