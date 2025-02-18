package plugin.project.jvm

import gradle.amperModuleExtraProperties
import gradle.libs
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest

internal fun Project.configureKotlinJvmTest() {
    val jvmTest = tasks.withType<KotlinJvmTest>()

    jvmTest.configureEach {
        amperModuleExtraProperties.settings.jvm
        maxHeapSize = "2g"
        exclude("**/*StressTest*")
        useJUnitPlatform()
        configureJavaToolchain(
            libs.versions.java.toolchain.compileJdk.map(JavaLanguageVersion::of),
            libs.versions.java.toolchain.testJdk.map(JavaLanguageVersion::of),
        )

        filter {
            isFailOnNoMatchingTests = false
        }
        testLogging {
            showExceptions = true
            showStandardStreams = true
            events = setOf(
                TestLogEvent.FAILED,
                TestLogEvent.PASSED,
            )
            exceptionFormat = TestExceptionFormat.FULL
        }
    }

    if(jvmTest.isNotEmpty()) {
        tasks.register<Test>("stressTest") {
            classpath = files(jvmTest.map { it.classpath })
            testClassesDirs = files(jvmTest.map { it.testClassesDirs })

            maxHeapSize = "2g"
            jvmArgs("-XX:+HeapDumpOnOutOfMemoryError")
            setForkEvery(1)
            systemProperty("enable.stress.tests", "true")
            include("**/*StressTest*")
            useJUnitPlatform()
            configureJavaToolchain(
                libs.versions.java.toolchain.compileJdk.map(JavaLanguageVersion::of),
                libs.versions.java.toolchain.testJdk.map(JavaLanguageVersion::of),
            )
        }
    }
}

/** Configure tests against different JDK versions. */
internal fun Test.configureJavaToolchain(
    compileJdk: Provider<JavaLanguageVersion>,
    testJdk: Provider<JavaLanguageVersion>,
) {
    val testJdkVersion = testJdk.get().asInt()
    onlyIf("only if testJdk is not lower than compileJdk") { testJdkVersion >= compileJdk.get().asInt() }

    val javaToolchains = project.the<JavaToolchainService>()
    javaLauncher = javaToolchains.launcherFor {
        languageVersion = testJdk
    }

    if (testJdkVersion >= 16) {
        // Allow reflective access from tests
        jvmArgs(
            "--add-opens=java.base/java.net=ALL-UNNAMED",
            "--add-opens=java.base/java.time=ALL-UNNAMED",
            "--add-opens=java.base/java.util=ALL-UNNAMED",
        )
    }

    if (testJdkVersion >= 21) {
        // coroutines-debug use dynamic agent loading under the hood.
        // Remove as soon as the issue is fixed: https://youtrack.jetbrains.com/issue/KT-62096/
        jvmArgs("-XX:+EnableDynamicAgentLoading")
    }
}
