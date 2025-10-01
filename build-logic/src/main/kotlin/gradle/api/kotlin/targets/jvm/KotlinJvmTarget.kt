package gradle.api.kotlin.targets.jvm

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

/**
 * This is an intermediate source set to make sure that we do not have expect/actual
 *in the same Kotlin module
 */
context(project: Project)
public fun KotlinJvmTarget.createJavaCodegenTest() {
    val baseTestCompilation = compilations.getByName("test")
    val testCompileClasspath = project.configurations.getByName(baseTestCompilation.compileDependencyConfigurationName)
    val testRuntimeClasspath = project.configurations.getByName(baseTestCompilation.runtimeDependencyConfigurationName)

    val javaCodegenCompilation = compilations.create("javaCodegenTest").apply {
        configurations.compileDependencyConfiguration.extendsFrom(testCompileClasspath)
        configurations.runtimeDependencyConfiguration?.extendsFrom(testRuntimeClasspath)

        compileJavaTaskProvider?.configure { classpath += testCompileClasspath }
    }

    val testRun = testRuns.create("javaCodegen").apply {
        setExecutionSourceFrom(javaCodegenCompilation)
        executionTask.configure { useJUnitPlatform() }
    }

    project.tasks.matching { task -> task.name == "check" }.configureEach {
        dependsOn(testRun.executionTask)
    }
}

public fun String.toJvmTarget(): JvmTarget = JvmTarget.fromTarget(if (this == "8") "1.8" else this)
