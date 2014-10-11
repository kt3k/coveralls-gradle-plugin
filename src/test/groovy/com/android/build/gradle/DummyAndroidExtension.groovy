package com.android.build.gradle

// this extension is made for avoiding compile error in
// project.android.sourceSets.main.java.getSrcDirs()
class DummyAndroidExtension {

	DummyAndroidExtension getSourceSets() {
		return this
	}

	DummyAndroidExtension getMain() {
		return this
	}

	DummyAndroidExtension getJava() {
		return this
	}

	List<File> getSrcDirs() {
		// to check if this plugin is detected,
		// this method should return some files
		List<File> dirs = new ArrayList<File>()
		dirs += new File("whatever")
	}

}
