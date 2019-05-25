.PHONY: release-maven
release-maven:
	./gradlew clean uploadArchives

# When release to portal, we need to comment out com.vanniktech.maven.publish plugin
# in build.gradle
.PHONY: relase-portal
release-portal:
	./gradlew clean publishPlugins
