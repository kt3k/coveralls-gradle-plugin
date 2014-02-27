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
				return new ServiceInfo('travis-pro', env.get('TRAVIS_JOB_ID'), env.get('COVERALLS_REPO_TOKEN'))
			} else {
				return new ServiceInfo('other', null, env.get('COVERALLS_REPO_TOKEN'))
			}

		} else {
			if (envIsTravis(env)) {
				return new ServiceInfo('travis-ci', env.get('TRAVIS_JOB_ID'), null)
			}
		}

		// cannot create service info from environmental variables. (no repo_token, not travis)
		return null

	}

	private static boolean envIsTravis(Map<String, String> env) {
		if (env.get('TRAVIS') == 'true' && env.get('TRAVIS_JOB_ID') != null) {
			return true
		}

		return false
	}

	private static boolean repoTokenIsSet(Map<String, String> env) {
		if (env.get('COVERALLS_REPO_TOKEN') != null) {
			return true
		}

		return false
	}

}
