import io.github.z4kn4fein.semver.Version
import org.gradle.api.services.internal.RegisteredBuildServiceProvider

plugins {
    // Print suggestions for your build as you run regular tasks.
    alias(libs.plugins.doctor)
    // API documentation engine for Kotlin.
    // Used to assist in the development of Gradle plugins. It automatically applies the Java Library.
    `java-gradle-plugin`
    // Support convention plugins written in Kotlin.
    // Convention plugins are build scripts in 'src/main' that automatically become available as plugins in the main build.
    `kotlin-dsl`
    // Serialization.
    alias(libs.plugins.kotlin.serialization)
}

group = "io.github.aaziz993"

version = Version(1, 0, 0).toString()

// Configure the build-logic plugins to target JDK
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = libs.versions.java.sourceCompatibility.get().let(JavaVersion::toVersion)
    targetCompatibility = libs.versions.java.targetCompatibility.get().let(JavaVersion::toVersion)
}

kotlin {
    explicitApi()

    jvmToolchain(libs.versions.java.languageVersion.get().toInt())

    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xignore-const-optimization-errors",
            "-Xcontext-parameters",
            "-Xtype-enhancement-improvements-strict-mode=false",
        )
        optIn.addAll(
            listOf(
                "kotlinx.serialization.InternalSerializationApi",
                "kotlinx.serialization.ExperimentalSerializationApi",
                "org.jetbrains.dokka.gradle.internal.InternalDokkaGradlePluginApi",
                "org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi",
                "org.jetbrains.kotlin.gradle.ExperimentalWasmDsl",
            ),
        )
    }

}

doctor {
    enableTestCaching = true

    // Disable JAVA_HOME validation as we use "Daemon JVM discovery" feature
    // https://docs.gradle.org/current/userguide/gradle_daemon.html#sec:daemon_jvm_criteria
    javaHome {
        ensureJavaHomeIsSet = false
        ensureJavaHomeMatches = false
    }

    logger.info("Gradle Doctor task monitoring is disabled.")
    gradle.sharedServices.unregister("listener-service")
}

fun BuildServiceRegistry.unregister(name: String) {
    val registration = registrations.getByName(name)
    registrations.remove(registration)
    (registration.service as RegisteredBuildServiceProvider<*, *>).maybeStop()
}

// Having a plugin in plugins { ... } block with apply false has only one use. That is to add that plugin to the build script classpath. So the according action with a convention plugin would be to declare those plugins as runtimeOnly` dependencies for that convention plugin build. The plugin itself would then not do any actions but will just be applied so that its dependencies are dragged into the classpath too.
dependencies {
    // Gradle
    // plugin
    compileOnly(libs.kotlin.gradle.plugin)
    // Opentelemetry
    implementation(libs.bundles.opentelemetry)
    // Intellij
    implementation(libs.bundles.intellij.platform)
    // Configuration and build tool. Its goal is to provide a great and smooth user experience and IDE support
    implementation(libs.bundles.amper)
    // print suggestions for your build as you run regular tasks
    implementation(libs.plugins.doctor.toDep())
    // gives the data to speed up your build, improve build reliability and accelerate build debugging.
    implementation(libs.plugins.develocity.toDep())
    // enhances published build scans by adding a set of tags, links and custom values that have proven to be useful for many projects building with Develocity.
    implementation(libs.plugins.develocityCommonCustomUserData.toDep())
    // provides a repository for downloading JVMs
    implementation(libs.plugins.foojayResolverConvention.toDep())
    // software Composition Analysis (SCA) tool that attempts to detect publicly disclosed vulnerabilities contained within a project's dependencies. It does this by determining if there is a Common Platform Enumeration (CPE) identifier for a given dependency. If found, it will generate a report linking to the associated CVE entries.
    implementation(libs.plugins.dependencycheck.toDep())
    // creates fat/uber JARs with support for package relocation
    implementation(libs.plugins.shadow.toDep()) // conflict io.ktor.plugin:io.ktor.plugin.gradle.plugin:3.0.0 > io.ktor.plugin:plugin:3.0.0 > com.github.johnrengelman.shadow:com.github.johnrengelman.shadow.gradle.plugin:7.1.2
    // set of solutions for collecting test coverage of Kotlin code compiled for JVM and Android platforms.
    implementation(libs.plugins.kover.toDep())
    // format <antlr | c | c# | c++ | css | flow | graphql | groovy | html | java | javascript | json | jsx | kotlin | less | license headers | markdown | objective-c | protobuf | python | scala | scss | shell | sql | typeScript | vue | yaml | anything> using <gradle | maven | sbt | anything>.
    implementation(libs.plugins.spotless.toDep())
    // help developers deliver high-quality, efficient code standards that benefit the entire team or organization.
    implementation(libs.plugins.sonarqube.toDep())
    // an API documentation engine for Kotlin.
    // for html
    implementation(libs.plugins.dokka.toDep())
    // for javadoc
    implementation(libs.plugins.dokkaJavadoc.toDep())
    // for versioning
    implementation(libs.dokka.versioning)
    // allows dumping binary API of a JVM part of a Kotlin library that is public in the sense of Kotlin visibilities and ensures that the public binary API wasn't changed in a way that makes this change binary incompatible.
    implementation(libs.plugins.binaryCompatibilityValidator.toDep())
    // produces Kotlin source example files and tests from markdown documents with embedded snippets of Kotlin code
    implementation(libs.plugins.knit.toDep())
    // generating BuildConstants for any kind of Gradle projects: Java, Kotlin, Android, Groovy, etc. Designed for KTS scripts, with experimental support for Kotlin's multi-platform plugin
    implementation(libs.plugins.buildconfig.toDep())
    // string and image resource generation
    implementation(libs.plugins.libres.toDep())
    // pre-commit hooks
    implementation(libs.plugins.gradlePreCommitGitHooks.toDep())
    // publishing
    implementation(libs.plugins.vanniktech.maven.publish.toDep())

    // Java
    // scans and indexes your project's classpath metadata, allowing reverse transitive query of the type system on runtime.
    implementation(libs.reflections)
    // parse toml
    implementation(libs.tomlj)
    // parse yaml
    implementation(libs.snakeyaml)

    // Kotlin
    // scripting
    implementation(libs.kotlin.scripting.common)
    implementation(libs.kotlin.scripting.jvm)
    implementation(libs.kotlin.scripting.jvm.host)
    implementation(kotlin("script-runtime"))
    // api that you can use to develop lightweight compiler plugins. KSP provides a simplified compiler plugin API that leverages the power of Kotlin while keeping the learning curve at a minimum. Compared to KAPT, annotation processors that use KSP can run up to 2x faster.
    implementation(libs.plugins.ksp.toDep())
    // generates an additional zero-argument constructor for classes with a specific annotation.
    implementation(libs.plugins.noarg.toDep())
    // adapts Kotlin to the requirements of those frameworks and makes classes annotated with a specific annotation and their members open.
    implementation(libs.plugins.allopen.toDep())
    // # date and time
    implementation(libs.kotlinx.datetime)
    // biginteger and bigdecimal
    implementation(libs.bignum)
    // charset
    implementation(libs.bundles.fleeksoft.charset)
    // hex, base32, base45 and base64 encoding
    implementation(libs.encoding)
    // kotlin multiplatform syntax highlighting engine.
    implementation(libs.highlights)
    // a nice parser combinator library for Kotlin JVM, JS, and Multiplatform projects
    implementation(libs.better.parse)
    // provide a lingua franca of interfaces and abstractions across Kotlin libraries. For this, it includes the most popular data types such as Option, and Either, functional operators such as zipOrAccumulate, and computation blocks to empower users to write pure FP apps and libraries built atop higher order abstractions.
    implementation(libs.arrow.core)
    // compiler plugin, that generates visitor code for serializable classes, runtime library with core serialization API and support libraries with various serialization formats.
    // official serialization
    implementation(libs.plugins.kotlin.serialization.toDep())
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.csv)
    // community-supported serialization
    implementation(libs.xmlutil.serialization)
    implementation(libs.kaml)
    implementation(libs.tomlkt)
    // xml and html parser
    implementation(libs.bundles.ksoup)
    // a lightweight Kotlin Multiplatform library that provides a unified and type-safe interface for key-value storage across different platforms. It simplifies the process of storing and retrieving data by abstracting the underlying storage mechanisms.
    implementation(libs.klibs.kstorage)
    // file system
    implementation(libs.kotlinx.io.core)
    // sqlite jdbc driver
    implementation(libs.sqlite.jdbc)
    // toolkit for running benchmarks for multiplatform code written in Kotlin. It is designed to work with Kotlin/JVM, Kotlin/JS, Kotlin/Native, and Kotlin/WasmJs (experimental) targets.
    implementation(libs.plugins.kotlinx.benchmark.toDep())
    // multiplatform
    implementation(libs.plugins.kotlin.multiplatform.toDep())
    // jvm
    runtimeOnly(libs.plugins.kotlin.jvm.toDep())
    // data pipeline processing
//    runtimeOnly(libs.plugins.dataframe.toDep())
    // providing detailed failure messages with contextual information during testing.
    implementation(libs.plugins.powerAssert.toDep())
    // generates typesafe Kotlin APIs from your SQL statements. It verifies your schema, statements, and migrations at compile-time and provides IDE features like autocomplete and refactoring which make writing and maintaining SQL simple.
    implementation(libs.plugins.sqldelight.toDep())
    // Abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.
    implementation(libs.plugins.room.toDep())
    // ktor
    runtimeOnly(libs.plugins.ktor.toDep())
    // kotlin library for adding asynchronous Remote Procedure Call (RPC) services to your applications. Build your RPC with already known language constructs and nothing more!
    implementation(libs.plugins.kotlinx.rpc.toDep())
    // HTTP client/Kotlin Symbol Processor for Kotlin Multiplatform ( Android, iOS, Js, Jvm, Linux) using KSP and Ktor clients inspired by Retrofit
    implementation(libs.plugins.ktorfit.toDep())
    // strongly typed GraphQL client that generates Kotlin models for your GraphQL operations.
    implementation(libs.plugins.apollo3.toDep())
    // test
    runtimeOnly(libs.plugins.kotest.multiplatform.toDep())

    // Android
    compileOnly(libs.android.gradle.plugin)
    runtimeOnly(libs.plugins.android.kotlin.multiplatform.library.toDep())
    runtimeOnly(libs.plugins.android.application.toDep())

    // IOS
    implementation(libs.plugins.apple.toDep())
    implementation(libs.plugins.cocoapods.toDep())

    // Native
    runtimeOnly(libs.plugins.kmp.nativecoroutines.toDep())

    // Web
    runtimeOnly(libs.kotlinx.html)
    runtimeOnly(libs.plugins.js.plain.objects.toDep())
    implementation(libs.plugins.seskar.toDep())
    // converter of TypeScript declaration files to Kotlin declarations.
    implementation(libs.plugins.karakum.toDep())

    // Compose multiplatform
    implementation(libs.plugins.compose.multiplatform.toDep())
    implementation(libs.plugins.compose.hotReload.toDep())
    runtimeOnly(libs.plugins.compose.compiler.toDep())

    // Semantic version
    implementation(libs.semver)

    // A hack to make version catalogs accessible from buildSrc sources
    // https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

fun Provider<PluginDependency>.toDep() =
    map { plugin -> "${plugin.pluginId}:${plugin.pluginId}.gradle.plugin:${plugin.version}" }

gradlePlugin {
    plugins {
        register("SettingsPlugin") {
            id = "settings.convention"
            implementationClass = "gradle.plugins.initialization.SettingsPlugin"
        }
    }
}
