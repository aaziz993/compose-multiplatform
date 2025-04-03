package gradle.plugins.kotlin.targets.jvm

import gradle.accessors.kotlin
import gradle.api.project.projectProperties
import gradle.api.configureEach
import gradle.api.project.ProjectLayout
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest

internal class JvmPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            registerJvmStressTest()
            registerJavaCodegenTestTask()

            // Apply java properties.
            projectProperties.java?.applyTo()

            // Apply java application properties.
            projectProperties.application?.applyTo()
        }
    }

    private fun Project.registerJvmStressTest() {
        val jvmTest = tasks.withType<KotlinJvmTest>()

        if (jvmTest.isNotEmpty()) {
            tasks.register<Test>("stressTest") {
                classpath = files(jvmTest.map { it.classpath })
                testClassesDirs = files(jvmTest.map { it.testClassesDirs })
                maxHeapSize = "2g"
                jvmArgs("-XX:+HeapDumpOnOutOfMemoryError")
                forkEvery = 1
                systemProperty("enable.stress.tests", "true")
                include("**/*StressTest*")
                useJUnitPlatform()
            }

            jvmTest.configureEach {
                exclude("**/*StressTest*")
            }
        }
    }

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
        /**
         * This is an intermediate source set to make sure that we do not have expect/actual
         *in the same Kotlin module
         */
        kotlin.targets.withType<org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget>()
            .takeIf(NamedDomainObjectCollection<*>::isNotEmpty)?.let { jvmTargets ->
                val commonJavaCodegenTest = kotlin.sourceSets.create("commonJavaCodegenTest") {
                    when (projectProperties.layout) {
                        is ProjectLayout.Flat -> kotlin.srcDir("test")
                        else -> kotlin.srcDir("src/commonTest/kotlin")
                    }
                }

                jvmTargets.configureEach { jvmTarget ->
                    val javaCodegenCompilation = jvmTarget.compilations.create("javaCodegenTest")

                    val testRun = jvmTarget.testRuns.create("javaCodegen")

                    testRun.setExecutionSourceFrom(javaCodegenCompilation)

                    val testCompileClasspath = configurations.getByName("${jvmTarget.targetName}TestCompileClasspath")

                    javaCodegenCompilation.compileJavaTaskProvider?.configure {
                        classpath += testCompileClasspath
                    }

                    javaCodegenCompilation.configurations.compileDependencyConfiguration.extendsFrom(testCompileClasspath)
                    javaCodegenCompilation.configurations.runtimeDependencyConfiguration?.extendsFrom(configurations.getByName("${jvmTarget.targetName}TestRuntimeClasspath"))
                    javaCodegenCompilation.defaultSourceSet.dependsOn(commonJavaCodegenTest)
                }
            }
    }
}
