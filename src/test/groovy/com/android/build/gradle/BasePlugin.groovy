package com.android.build.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

// This plugin and other plugins in this package
// are fake classes for the real Android plugins(com.android.tools.build:gradle).
// The real one checks runtime Gradle version in the apply method,
// which makes this project incompatible.
class BasePlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
		if (project.extensions.findByName('android') == null) {
			project.extensions.create('android', DummyAndroidExtension)
		}
	}

}
