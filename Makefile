.PHONY: chmod dependencies-check build-config format-check format quality-check \
coverage-verify coverage doc doc-samples-check doc-samples test full-check kotlin-ts jar generate signing-gpg \
list-signing-gpg clean-signing-gpg dist-signing-gpg publish-local publish-github publish-maven publish\
server-auto-reload clean_apple_app clean_files clean clean-mac

chmod: # 🔓 Give permission to execute gradlew.
	git update-index --chmod=+x gradlew && chmod -R 777 scripts/

dependencies-check:  # 🔬 Monitor dependent libraries for known, published vulnerabilities.
	./gradlew dependencyCheckAnalyze

format-check: # 🔬 # Format <antlr | c | c# | c++ | css | flow | graphql | groovy | html | java | javascript | json | jsx | kotlin | less | license headers | markdown | objective-c | protobuf | python | scala | scss | shell | sql | typeScript | vue | yaml | anything> using <gradle | maven | sbt | anything>.
	./gradlew spotlessCheck

format: # 📝 # Format <antlr | c | c# | c++ | css | flow | graphql | groovy | html | java | javascript | json | jsx | kotlin | less | license headers | markdown | objective-c | protobuf | python | scala | scss | shell | sql | typeScript | vue | yaml | anything> using <gradle | maven | sbt | anything>.
	./gradlew spotlessApply

quality-check: # 🔬Help developers deliver high-quality, efficient code standards that benefit the entire team or organization.
	./gradlew sonar

test: # 🧪 Run all tests.
	./gradlew check

coverage-verify: # ✅ Set of solutions for collecting test coverage of Kotlin code compiled for JVM and Android platforms.
	./gradlew koverVerify

coverage: # 📊 Set of solutions for collecting test coverage of Kotlin code compiled for JVM and Android platforms.
	./gradlew koverReport

doc: # 📄 Api documentation engine for Kotlin.
	./gradlew dokkaGenerate

doc-samples-check: # 🔬 Produces Kotlin source example files and tests from markdown documents with embedded snippets of Kotlin code.
	./gradlew knitCheck

doc-samples: # 📜 Produces Kotlin source example files and tests from markdown documents with embedded snippets of Kotlin code.
	./gradlew knitPrepare

full-check: test format quality-check  # ✅ Code format, test and quality check.

build-config: # 📜 Generating BuildConstants for any kind of Gradle projects: Java, Kotlin, Android, Groovy, etc. Designed for KTS scripts, with experimental support for Kotlin's multi-platform plugin.
	./gradlew generateBuildConfig

kotlin-ts: # 📜 Converter of TypeScript declaration files to Kotlin declarations.
	./gradlew gerateKarakumExternals

jar: # 📦 Creates fat/uber JARs with support for package relocation.
	./gradlew shadowJar

generate: coverage doc doc-samples build-config kotlin-ts  # 🔨 Generate code coverage, documentation and code samples from documentation.

signing-gpg: # 🔑 Generates signing gpg key.
	./gradlew generateSigningGPGKey

list-signing-gpg: # 📋 Lists signing gpg keys.
	./gradlew listSigningGPGKey

clean-signing-gpg: # 🧹 Cleans all signing gpg keys.
	./gradlew cleanSigningGPGKey

dist-signing-gpg: # 🌐 Distributes signing gpg key.
	./gradlew distributeSigningGPGKey

publish-local: full-check # 📦🚀 Publish to Maven Local.
	@. project.sh && publish_local

publish-github: full-check # 📦🚀 Publish to GitHub Packages.
	./gradlew publishAllPublicationsToGithubPackagesRepository

publish-maven: full-check # 📦🚀 Publish to Maven Repository.
	./gradlew publishAllPublicationsToMavenRepository

publish: full-check # 📦🚀 Publish to Maven Local, Space Packages, GitHub Packages and Maven Repository.
	@. project.sh && publish

warmup: # 🔥 Warmup for jetbrains development environment.
	./gradlew assemble

server-auto-reload: # 🔄 Server application hot reload.
  ./gradlew -t autoreload-engine-main:build

clean-apple-app: # 🧹 Clean apple-app.
	@. project.sh && clean_apple_app

clean-files: # 🧹 Clean files.
	@. project.sh && clean_files

clean: # 🧹 Clean all.
	@. project.sh && clean

clean-mac: # 🧹 Clean Mac memory.
	@. project.sh && clean_mac
