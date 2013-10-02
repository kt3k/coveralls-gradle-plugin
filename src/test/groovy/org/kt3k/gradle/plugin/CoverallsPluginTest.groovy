package org.kt3k.gradle.plugin

import org.junit.Test
import static org.junit.Assert.*

import static org.mockito.Mockito.*
import org.mockito.ArgumentCaptor

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.Task

class CoverallsPluginTest {

	@Test
	void testApply() {
		ArgumentCaptor<Closure> captor = ArgumentCaptor.forClass Closure
		Task task = mock(Task.class)

		Project project = mock(Project.class)
		when(project.task('coveralls')).thenReturn(task)

		Plugin plugin = new CoverallsPlugin()
		plugin.apply(project)

		// verify coveralls task is registered
		verify(project).task('coveralls')
		verify(task).leftShift(captor.capture())

		captor.getValue().call()
	}

}
