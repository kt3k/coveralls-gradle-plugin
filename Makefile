.PHONY: release
release-github:
	git clone git@github.com:kt3k/repository.git
	MVN_REPO=file://`pwd`/repository/maven/release gradle uploadArchives
	cd repository ; git add . ; git commit -m 'Automated release' ; git checkout gh-pages ; git merge master ; git push origin master gh-pages
	rm -rf repository

release-snapshot:
	MVN_REPO=https://oss.sonatype.org/content/repositories/snapshots/ ./gradlew uploadArchives

release-maven:
	MVN_REPO=https://oss.sonatype.org/service/local/staging/deploy/maven2/ ./gradlew clean uploadArchives

release-bintray:
	./gradlew clean bintray
