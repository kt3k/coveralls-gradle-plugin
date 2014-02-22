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

		// it now supports travis only.
		if (env.get('TRAVIS') == 'true' && env.get('TRAVIS_JOB_ID') != null) {
			if (env.get('COVERALLS_REPO_TOKEN') != null) {
				return new ServiceInfo('travis-pro', env.get('TRAVIS_JOB_ID'), env.get('COVERALLS_REPO_TOKEN'))
			} else {
				return new ServiceInfo('travis-ci', env.get('TRAVIS_JOB_ID'), null)
			}
		}

		// cannot create service info from environmental variables.
		return null

	}

}
