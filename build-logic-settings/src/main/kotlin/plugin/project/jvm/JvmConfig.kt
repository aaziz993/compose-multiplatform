package plugin.project.jvm

import gradle.libs
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest

internal fun Project.configureJvm() {
    configureJar()
    configureJvmTest()
}

private fun Project.configureJvmTest() {
    val jvmTest = tasks.withType<KotlinJvmTest>().onEach(::configureKotlinJvmTest)

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
