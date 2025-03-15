.PHONY: chmod-scrips test format format-check quality-check full-check gen-coverage gen-doc gen-doc-samples gen-all \
gen-gpg clean-gpg publish-github publish-space publish-maven publish clean

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

full-check: test format quality-check  # ✅ Code format, test and quality check.

gen-kotlin-ts: # 📜 Convert of TypeScript declaration files to Kotlin declarations.
	./gradlew gerateKarakumExternals

gen-coverage: # 📊 Generate code coverage report.
	./gradlew generateKoverReport

gen-doc: # 📄 Generate documentation
	./gradlew dokkaGenerate

gen-doc-samples: # 📜 Generate documentation and code samples from documentation.
	./gradlew knitPrepare

gen-all: gen-coverage gen-doc-samples # 🔨 Generate code coverage, documentation and code samples from documentation

gen-gpg: # 🔑 Generate gpg key.
	./scripts/gpg/gen-gpg.sh

clean-gpg: # 🧹 Clean all gpg keys.
	./scripts/gpg/clean-gpg.sh

distribute-gpg: # 🌐 Distribute signing gpg key
	./gradlew distributeSigningGPGKey

publish-maven-local: full-check # 📦 Publish to GitHub Packages.
	./scripts/publish/publish-maven-local.sh

publish-github-packages: full-check # 📦 Publish to GitHub Packages.
	./scripts/publish/publish-github-packages.sh

publish-github-packages: full-check # 📦 Publish to GitHub Packages.
	./scripts/publish/publish-github-packages.sh

publish-space-packages: full-check # 📦 Publish to Space Packages.
	./scripts/publish/publish-space-packages.sh

publish-maven: full-check # 📦 Publish to Maven.
	./scripts/publish/publish-maven.sh

publish: full-check # 📦 Publish to Space Packages, GitHub Packages and Maven.
	./scripts/publish/publish-github.sh && ./scripts/publish/publish-space.sh && ./scripts/publish/publish-maven.sh

warmup: # 🔥 Warmup for jetbrains development environment
	./gradlew assemble

server-auto-reload: # 🔄 Server application hot reload
  ./gradlew -t autoreload-engine-main:build

clean: # 🧹 Clean all.
	./gradlew clean
