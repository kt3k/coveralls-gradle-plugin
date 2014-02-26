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

	private static envIsTravis(Map<String, String> env) {
		return env.get('TRAVIS') == 'true' && env.get('TRAVIS_JOB_ID') != null
	}

	private static repoTokenIsSet(env) {
		return env.get('COVERALLS_REPO_TOKEN') != null
	}

}
