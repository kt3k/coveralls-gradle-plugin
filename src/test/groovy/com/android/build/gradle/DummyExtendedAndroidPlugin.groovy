package com.android.build.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

// this plugin extends a subclass of BasePlugin and it should be detected
class DummyExtendedAndroidPlugin extends DummyAndroidPlugin implements Plugin<Project> {
}
