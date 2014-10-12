package com.android.build.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

// this plugin extends a plugin but not the BasePlugin
class DummyExtendedPlugin extends DummyPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
	}

}
