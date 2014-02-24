package org.kt3k.gradle.plugin

import org.junit.Test
import static org.junit.Assert.*

import static org.mockito.Mockito.*
import org.mockito.ArgumentCaptor

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder

class CoverallsPluginTest {

	@Test
	void testApply() {

		// build dummy project
		Project project = ProjectBuilder.builder().build()

		new CoverallsPlugin().apply(project)

		Task task = project.tasks.getByName('coveralls')

		// execute task action
		task.getActions().first().execute(task)
	}

}
