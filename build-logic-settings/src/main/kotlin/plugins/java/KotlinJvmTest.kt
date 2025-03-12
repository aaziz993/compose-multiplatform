package plugins.java

import gradle.accessors.kotlin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

//package plugin.project.jvm
//
//import gradle.amperModuleExtraProperties
//import gradle.accessors.libs
//import org.gradle.api.Project
//import org.gradle.api.provider.Provider
//import org.gradle.api.tasks.testing.Test
//import org.gradle.api.tasks.testing.logging.TestExceptionFormat
//import org.gradle.api.tasks.testing.logging.TestLogEvent
//import org.gradle.jvm.toolchain.JavaLanguageVersion
//import org.gradle.jvm.toolchain.JavaToolchainService
//import org.gradle.accessors.kotlin.dsl.assign
//import org.gradle.accessors.kotlin.dsl.register
//import org.gradle.accessors.kotlin.dsl.the
//import org.gradle.accessors.kotlin.dsl.withType
//import org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest
//
//internal fun Project.configureKotlinJvmTest() =
//    amperModuleExtraProperties.settings.jvm.let { jvm ->
//        val jvmTest = tasks.withType<KotlinJvmTest>()
//
//        jvmTest.configureEach {
//            maxHeapSize = "2g"
//            exclude("**/*StressTest*")
//            useJUnitPlatform()
//            configureJavaToolchain(
//                JavaLanguageVersion.of(jvm.compileSdk),
//                JavaLanguageVersion.of(jvm.testSdk),
//            )
//
//            filter {
//                isFailOnNoMatchingTests = false
//            }
//            testLogging {
//                showExceptions = true
//                showStandardStreams = true
//                events = setOf(
//                    TestLogEvent.FAILED,
//                    TestLogEvent.PASSED,
//                )
//                exceptionFormat = TestExceptionFormat.FULL
//            }
//        }
//
//        if (jvmTest.isNotEmpty()) {
//            tasks.register<Test>("stressTest") {
//                classpath = files(jvmTest.map { it.classpath })
//                testClassesDirs = files(jvmTest.map { it.testClassesDirs })
//
//                maxHeapSize = "2g"
//                jvmArgs("-XX:+HeapDumpOnOutOfMemoryError")
//                setForkEvery(1)
//                systemProperty("enable.stress.tests", "true")
//                include("**/*StressTest*")
//                useJUnitPlatform()
//                configureJavaToolchain(
//                    JavaLanguageVersion.of(jvm.compileSdk),
//                    JavaLanguageVersion.of(jvm.testSdk),
//                )
//            }
//        }
//    }
//
///** Configure tests against different JDK versions. */
//internal fun Test.configureJavaToolchain(
//    compileJdk: JavaLanguageVersion,
//    testJdk: JavaLanguageVersion,
//) {
//    val testJdkVersion = testJdk.asInt()
//    onlyIf("only if testJdk is not lower than compileJdk") { testJdkVersion >= compileJdk.asInt() }
//
//    val javaToolchains = project.the<JavaToolchainService>()
//    javaLauncher = javaToolchains.launcherFor {
//        languageVersion = testJdk
//    }
//
//    if (testJdkVersion >= 16) {
//        // Allow reflective access from tests
//        jvmArgs(
//            "--add-opens=java.base/java.net=ALL-UNNAMED",
//            "--add-opens=java.base/java.time=ALL-UNNAMED",
//            "--add-opens=java.base/java.util=ALL-UNNAMED",
//        )
//    }
//
//    if (testJdkVersion >= 21) {
//        // coroutines-debug use dynamic agent loading under the hood.
//        // Remove as soon as the issue is fixed: https://youtrack.jetbrains.com/issue/KT-62096/
//        jvmArgs("-XX:+EnableDynamicAgentLoading")
//    }
//}

/**
 * Registers a new testRun that substitutes the Kotlin models by the Java models.
 * Because they have the same JVM API, this is transparent to all tests that are in commonTest that work the same for Kotlin
 * & Java models.
 *
 * - For Java models, we create a separate compilation (and therefore sourceSet graph). The generated models are wired to the separate
 * commonJavaCodegenTest source set. Then the contents of commonTest/kotlin is sourced directly
 *
 * This breaks IDE support because now commonTest/kotlin is used from 2 different places so clicking a model there, it's impossible
 * to tell which model it is. We could expect/actual all of the model APIs but that'd be a lot of very manual work
 */
private fun Project.registerJavaCodegenTestTask() {
    check(kotlin is KotlinMultiplatformExtension) {
        "Only multiplatform projects can register a javaCodegenTest task"
    }
    val jvmTarget = kotlin.targets.getByName("jvm") as KotlinJvmTarget
    jvmTarget.withJava()

    /**
     * This is an intermediate source set to make sure that we do not have expect/actual
     * in the same Kotlin module
     */
    val commonJavaCodegenTest = kotlin.sourceSets.create("commonJavaCodegenTest") {
        this.kotlin.srcDir("src/commonTest/kotlin")
    }
    val javaCodegenCompilation = jvmTarget.compilations.create("javaCodegenTest")

    val testRun = jvmTarget.testRuns.create("javaCodegen")
    testRun.setExecutionSourceFrom(javaCodegenCompilation)

    javaCodegenCompilation.compileJavaTaskProvider?.configure {
        classpath += configurations.getByName("jvmTestCompileClasspath")
    }
    javaCodegenCompilation.configurations.compileDependencyConfiguration.extendsFrom(configurations.getByName("jvmTestCompileClasspath"))
    javaCodegenCompilation.configurations.runtimeDependencyConfiguration?.extendsFrom(configurations.getByName("jvmTestRuntimeClasspath"))
    javaCodegenCompilation.defaultSourceSet.dependsOn(commonJavaCodegenTest)
}


//tasks.withType<Test>().configureEach {
//    useJUnitPlatform()
//
//    maxHeapSize = "1G"
//    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
//
//    javaLauncher = javaToolchains.launcherFor {
//        languageVersion = dokkaBuild.testJavaLauncherVersion
//    }
//}
