package org.jetbrains.kotlin.gradle.plugin

import org.codehaus.groovy.runtime.InvokerHelper
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.Convention
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSet

// This plugin and other plugins in this package
// are fake classes for the real Kotlin plugins(org.jetbrains.kotlin.jvm).
class KotlinPluginWrapper implements Plugin<Project> {

	private class KotlinSourceSet {
		SourceSet kotlin = new SourceSet()

		class SourceSet {
			List<File> getSrcDirs() {
				return [new File("src/main/kotlin")]
			}
		}
	}

	@Override
	void apply(Project project) {
		project.getPluginManager().apply(JavaPlugin.class)
		((JavaPluginConvention)project.getConvention().getPlugin(JavaPluginConvention.class)).getSourceSets().all(new Action<SourceSet>() {
			void execute(SourceSet sourceSet) {
				Convention sourceSetConvention = (Convention)InvokerHelper.getProperty(sourceSet, "convention")
				sourceSetConvention.getPlugins().put("kotlin", new KotlinSourceSet())
			}
		})
	}
}
