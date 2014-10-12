package com.android.build.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

// this plugin extends the BasePlugin and it should be detected
class DummyAndroidPlugin extends BasePlugin implements Plugin<Project> {
}
