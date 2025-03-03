import io.github.z4kn4fein.semver.Version
import java.util.*
import kotlinx.validation.ExperimentalBCVApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Print suggestions for your build as you run regular tasks
    alias(libs.plugins.doctor)
    // Software Composition Analysis (SCA) tool that attempts to detect publicly disclosed vulnerabilities contained within a project's dependencies. It does this by determining if there is a Common Platform Enumeration (CPE) identifier for a given dependency. If found, it will generate a report linking to the associated CVE entries.
    alias(libs.plugins.dependencycheck)
    // Allows dumping binary API of a JVM part of a Kotlin library that is public in the sense of Kotlin visibilities and ensures that the public binary API wasn't changed in a way that makes this change binary incompatible.
    alias(libs.plugins.binary.compatibility.validator)
    // API documentation engine for Kotlin.
    // for html
    alias(libs.plugins.dokka)
    // for javadoc
    alias(libs.plugins.dokkaJavadoc)
    // Used to assist in the development of Gradle plugins. It automatically applies the Java Library.
    `java-gradle-plugin`
    // Support convention plugins written in Kotlin.
    // Convention plugins are build scripts in 'src/main' that automatically become available as plugins in the main build.
    `kotlin-dsl`
    // Serialization
    alias(libs.plugins.kotlin.serialization)
}

val gradleProperties: Properties = Properties().apply {
    val file = file("../gradle.properties")
    if (file.exists()) {
        load(file.reader())
    }
}

group = gradleProperties["project.group"]!!.toString()

version = Version(
    libs.versions.buildLigicSettings.version.major.get().toInt(),
    libs.versions.buildLigicSettings.version.minor.get().toInt(),
    libs.versions.buildLigicSettings.version.patch.get().toInt(),
    libs.versions.buildLigicSettings.version.preRelase.get(),
    "${
        gradleProperties["github.actions.versioning.ref.name"]!!.toString().toBoolean().takeIf { it }?.let {
            // The GITHUB_REF_NAME provide the reference name.
            System.getenv()["GITHUB_REF_NAME"]?.let { "-$it" }
        }.orEmpty()
    }${
        gradleProperties["github.actions.versioning.run.number"]!!.toString().toBoolean().takeIf { it }?.let {
            // The GITHUB_RUN_NUMBER A unique number for each run of a particular workflow in a repository.
            // This number begins at 1 for the workflow's first run, and increments with each new run.
            // This number does not change if you re-run the workflow run.
            System.getenv()["GITHUB_RUN_NUMBER"]?.let { "-$it" }
        }.orEmpty()
    }${
        gradleProperties["jetbrains.space.automation.versioning.ref.name"]!!.toString().toBoolean().takeIf { it }?.let {
            // The JB_SPACE_GIT_BRANCH provide the reference  as "refs/heads/repository_name".
            System.getenv()["JB_SPACE_GIT_BRANCH"]?.let { "-$it" }
        }.orEmpty()
    }${
        gradleProperties["jetbrains.space.automation.versioning.run.number"]!!.toString().toBoolean().takeIf { it }
            ?.let {
                System.getenv()["JB_SPACE_EXECUTION_NUMBER"]?.let { "-$it" }
            }.orEmpty()
    }",
)

// Configure the build-logic plugins to target JDK
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = libs.versions.java.sourceCompatibility.get().let(JavaVersion::toVersion)
    targetCompatibility = libs.versions.java.targetCompatibility.get().let(JavaVersion::toVersion)
}

kotlin {
    explicitApi()

    compilerOptions {
        freeCompilerArgs.addAll("-Xcontext-parameters")
        optIn.addAll(
            listOf(
                "org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi",
                "org.jetbrains.kotlin.gradle.ExperimentalWasmDsl",
            ) +
                gradleProperties["kotlin.opt-ins"]?.toString()?.split(",")?.map(String::trim).orEmpty(),
        )
    }

    jvmToolchain(libs.versions.java.toolchain.compileJdk.get().toInt())
}

doctor {
    enableTestCaching = gradleProperties["doctor.enable-test-caching"]!!.toString().toBoolean()

    // Disable JAVA_HOME validation as we use "Daemon JVM discovery" feature
    // https://docs.gradle.org/current/userguide/gradle_daemon.html#sec:daemon_jvm_criteria
    javaHome {
        ensureJavaHomeIsSet = gradleProperties["doctor.java-home.ensure-java-home-is-set"]!!.toString().toBoolean()
        ensureJavaHomeMatches = gradleProperties["doctor.java-home.ensure-java-home-matches"]!!.toString().toBoolean()
    }
}

apiValidation {
    /**
     * Packages that are excluded from public API dumps even if they
     * contain public API.
     */
    ignoredPackages.addAll(
        gradleProperties.getProperty("api.validation.ignore.packages")
            .ifBlank { null }?.split(",").orEmpty().map(String::trim),
    )

    /**
     * Classes (fully qualified) that are excluded from public API dumps even if they
     * contain public API.
     */
    ignoredClasses.addAll(
        gradleProperties.getProperty("api.validation.ignore.classes")
            .ifBlank { null }?.split(",").orEmpty().map(String::trim),
    )

    /**
     * Set of annotations that exclude API from being public.
     * Typically, it is all kinds of `@InternalApi` annotations that mark
     * effectively private API that cannot be actually private for technical reasons.
     */
    nonPublicMarkers.addAll(
        gradleProperties.getProperty("api.validation.non-public-markers")
            .ifBlank { null }?.split(",").orEmpty().map(String::trim),
    )

    /**
     * Flag to programmatically disable compatibility validator
     */
    validationDisabled = gradleProperties.getProperty("api.validation.enable").toBoolean()

    /**
     * A path to a subdirectory inside the project root directory where dumps should be stored.
     */
    apiDumpDirectory = gradleProperties.getProperty("api.validation.api-dump-directory")

    /**
     * The KLib validation support is experimental and is a subject to change (applies to both an API and the ABI dump format). A project has to use Kotlin 1.9.20 or newer to use this feature.
     */
    @OptIn(ExperimentalBCVApi::class)
    klib {
        enabled = gradleProperties.getProperty("api.validation.klib.enable").toBoolean()
        // treat a target being unsupported on a host as an error
        strictValidation = gradleProperties.getProperty("api.validation.klib.strict-validation").toBoolean()
    }
}

tasks {
    apiBuild {
        // "jar" here is the name of the default Jar task producing the resulting jar file
        // in a multiplatform project it can be named "jvmJar"
        // if you applied the shadow plugin, it creates the "shadowJar" task that produces the transformed jar
        inputJar.value(jar.flatMap { it.archiveFile })
    }
}

dokka {
    // used as project name in the header
    moduleName = "Build logic convention plugins"

    dokkaSourceSets.main {

        // contains descriptions for the module and the packages
        includes.from("README.md")

        // adds source links that lead to this repository, allowing readers
        // to easily find source code for inspected declarations
        sourceLink {
            localDirectory = file("src/main/kotlin")
            remoteUrl("https://github.com/aaziz993/cmp/tree/main/build-logic")
            remoteLineSuffix = "#L"
        }
    }
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
    //  provides a repository for downloading JVMs
    implementation(libs.plugins.foojay.resolver.convention.toDep())
    // creates fat/uber JARs with support for package relocation
    implementation(libs.plugins.shadow.toDep()) // conflict io.ktor.plugin:io.ktor.plugin.gradle.plugin:3.0.0 > io.ktor.plugin:plugin:3.0.0 > com.github.johnrengelman.shadow:com.github.johnrengelman.shadow.gradle.plugin:7.1.2
    // api that you can use to develop lightweight compiler plugins. KSP provides a simplified compiler plugin API that leverages the power of Kotlin while keeping the learning curve at a minimum. Compared to KAPT, annotation processors that use KSP can run up to 2x faster.
    implementation(libs.plugins.ksp.toDep())
    // set of solutions for collecting test coverage of Kotlin code compiled for JVM and Android platforms.
    implementation(libs.plugins.kover.toDep())
    // can format <antlr | c | c# | c++ | css | flow | graphql | groovy | html | java | javascript | json | jsx | kotlin | less | license headers | markdown | objective-c | protobuf | python | scala | scss | shell | sql | typeScript | vue | yaml | anything> using <gradle | maven | sbt | anything>.
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
    dokkaPlugin(libs.dokka.versioning)
    // allows dumping binary API of a JVM part of a Kotlin library that is public in the sense of Kotlin visibilities and ensures that the public binary API wasn't changed in a way that makes this change binary incompatible.
    implementation(libs.plugins.binary.compatibility.validator.toDep())
    // produces Kotlin source example files and tests from markdown documents with embedded snippets of Kotlin code
    implementation(libs.plugins.knit.toDep())
    // Generating BuildConstants for any kind of Gradle projects: Java, Kotlin, Android, Groovy, etc. Designed for KTS scripts, with experimental support for Kotlin's multi-platform plugin
    implementation(libs.plugins.build.config.toDep())
    // string and image resource generation
    implementation(libs.plugins.libres.toDep())
    // pre-commit hooks
    implementation(libs.plugins.gradle.pre.commit.git.hooks.toDep())
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
    // generates an additional zero-argument constructor for classes with a specific annotation.
    implementation(libs.plugins.noarg.toDep())
    // adapts Kotlin to the requirements of those frameworks and makes classes annotated with a specific annotation and their members open.
    implementation(libs.plugins.allopen.toDep())
    // big numbers
    implementation(libs.bignum)
    // convert strings between various case formats
    implementation(libs.kasechange)
    // a nice parser combinator library for Kotlin JVM, JS, and Multiplatform projects
    implementation(libs.better.parse)
    // provide a lingua franca of interfaces and abstractions across Kotlin libraries. For this, it includes the most popular data types such as Option, and Either, functional operators such as zipOrAccumulate, and computation blocks to empower users to write pure FP apps and libraries built atop higher order abstractions.
    implementation(libs.arrow.core)
    // compiler plugin, that generates visitor code for serializable classes, runtime library with core serialization API and support libraries with various serialization formats.
    implementation(libs.plugins.kotlin.serialization.toDep())
    implementation(libs.kotlinx.serialization.json)
    // multiplatform
    implementation(libs.plugins.kotlin.multiplatform.toDep())
    // multiplatform library that provides the idiomatic and efficient way of using atomic operations in Kotlin.
    implementation(libs.plugins.atomicfu.toDep())
    // jvm
    runtimeOnly(libs.plugins.kotlin.jvm.toDep())
    // data pipeline processing
//    runtimeOnly(libs.plugins.dataframe.toDep())
    // providing detailed failure messages with contextual information during testing.
    implementation(libs.plugins.power.assert.toDep())
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
    runtimeOnly(libs.plugins.android.toDep())
    runtimeOnly(libs.plugins.androidLibrary.toDep())
    runtimeOnly(libs.plugins.androidApplication.toDep())
    implementation(kotlin("script-runtime"))

    // IOS
    implementation(libs.plugins.apple.toDep())
    implementation(libs.plugins.cocoapods.toDep())

    // Native
    runtimeOnly(libs.plugins.kmp.nativecoroutines.toDep())

    // Web
    runtimeOnly(libs.kotlinx.html)
//    runtimeOnly(libs.kotlinx.browser)
    runtimeOnly(libs.plugins.js.plain.objects.toDep())
    runtimeOnly(libs.plugins.js.plain.objects.toDep())
    implementation(libs.plugins.seskar.toDep())
    // converter of TypeScript declaration files to Kotlin declarations.
    implementation(libs.plugins.karakum.toDep())

    // Compose multiplatform
    implementation(libs.plugins.compose.multiplatform.toDep())
    runtimeOnly(libs.plugins.compose.compiler.toDep())

    // Semantic version
    implementation(libs.semver)

    // A hack to make version catalogs accessible from buildSrc sources
    // https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

gradlePlugin {
    plugins {
        register("SettingsPlugin") {
            id = "settings.convention"
            implementationClass = "plugin.SettingsPlugin"
        }
    }
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xcontext-receivers",
//            "-Xwhen-guards",
        )
        optIn.addAll(
            "org.jetbrains.dokka.gradle.internal.InternalDokkaGradlePluginApi",
        )
//        languageVersion = KotlinVersion.KOTLIN_2_2
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

fun Provider<PluginDependency>.toDep() =
    map {
        "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}"
    }
