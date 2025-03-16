.PHONY: chmod-scrips test format format-check quality-check signatures-check full-check build-config coverage doc \
doc-samples generate signing-gpg list-signing-gpg clean-signing-gpg dist-signing-gpg publish-local publish-github \
publish-space publish-maven publish clean

chmod-scripts: # 🔓 Give permission to execute gradlew.
	git update-index --chmod=+x gradlew && chmod -R 777 scripts/

test: # 🧪 Run all tests.
	./gradlew check

format: # 📝 Format code with spotless.
	./gradlew spotlessApply

format-check: # 🔬 Check code format with spotless.
	./gradlew spotlessCheck

quality-check: # 🔬 Check code quality with sonar.
	./gradlew sonar

signatures-check: # 🔬 Check source code compatibility with jdk and android signatures
	./gradlew animalsnifferRelease

full-check: test format quality-check signatures-check  # ✅ Code format, test and quality check.

coverage: # 📊 Generate code coverage report.
	./gradlew koverReport

doc: # 📄 Generate documentation
	./gradlew dokkaGenerate

check-doc-samples: # 🔬 Generate documentation and code samples from documentation.
	./gradlew knitCheck

doc-samples: # 📜 Generate documentation and code samples from documentation.
	./gradlew knitPrepare

build-config: # 📜 Generate build properties.
	./gradlew generateBuildConfig

kotlin-ts: # 📜 Convert of TypeScript declaration files to Kotlin declarations.
	./gradlew gerateKarakumExternals

jar:
	./gradlew shadowJar

generate: coverage doc doc-samples build-config kotlin-ts  # 🔨 Generate code coverage, documentation and code samples from documentation

signing-gpg: # 🔑 Generate gpg key.
	./gradlew generateSigningGPGKey

list-signing-gpg:
	./gradlew listSigningGPGKey

clean-signing-gpg: # 🧹 Clean all gpg keys.
	./gradlew cleanSigningGPGKey

dist-signing-gpg: # 🌐 Distribute signing gpg key
	./gradlew distributeSigningGPGKey

publish-local: full-check # 📦🚀 Publish to GitHub Packages.
	./scripts/publish/publish-local.sh

publish-github: full-check # 📦🚀 Publish to GitHub Packages.
	./gradlew publishAllPublicationsToGithubPackagesRepository

publish-space: full-check # 📦🚀 Publish to Space Packages.
	./gradlew publishAllPublicationsToSpacePackagesRepository

publish-maven: full-check # 📦🚀 Publish to Maven.
	./gradlew publishAllPublicationsToMavenRepository

publish: full-check # 📦🚀 Publish to Space Packages, GitHub Packages and Maven.
	./scripts/publish/publish.sh

warmup: # 🔥 Warmup for jetbrains development environment
	./gradlew assemble

server-auto-reload: # 🔄 Server application hot reload
  ./gradlew -t autoreload-engine-main:build

clean: # 🧹 Clean all.
	./gradlew clean
