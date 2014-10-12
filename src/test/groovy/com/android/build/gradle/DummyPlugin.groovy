package com.android.build.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

// this plugin does NOT extend BasePlugin
class DummyPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
	}

}
