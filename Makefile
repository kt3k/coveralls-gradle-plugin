.PHONY: release-maven
release-maven:
	./gradlew clean uploadArchives

# Note release-portal needs to be excuted after `uploadArchives` without cleaning build
# because publishPlugins task is unable to create .asc signature files.
.PHONY: relase-portal
release-portal:
	./gradlew publishPlugins
