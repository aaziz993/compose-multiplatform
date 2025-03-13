package plugins.java

import gradle.accessors.java
import gradle.accessors.kotlin
import gradle.accessors.projectProperties
import gradle.api.all
import gradle.decapitalized
import gradle.file.replace
import gradle.plugins.kmp.android.KotlinAndroidTarget
import gradle.plugins.kmp.jvm.KotlinJvmTarget
import gradle.plugins.kotlin.sourceSets
import gradle.project.ProjectLayout
import gradle.project.ProjectType
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.testing.Test
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin
import org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest

internal class JavaPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.kotlin.targets.none { target -> target is KotlinJvmTarget }) {
                return@with
            }



            tryRegisterJvmStressTest()

            if (projectProperties.kotlin.enabledKMP) {
                tryRegisterJavaCodegenTestTask()
            }

            if (projectProperties.kotlin.targets.any { target -> target is KotlinAndroidTarget }) {
                return@with
            }

            plugins.apply(JavaPlugin::class.java)

            projectProperties.java?.applyTo()

            // Apply java application plugin.
            if (projectProperties.type == ProjectType.APP && !projectProperties.compose.enabled) {
                plugins.apply(ApplicationPlugin::class.java)

                projectProperties.application?.applyTo()
            }

            if (!projectProperties.kotlin.enabledKMP) {
                plugins.apply(KotlinPlatformJvmPlugin::class.java)

                adjustSourceSets()
            }
        }
    }

    private fun Project.adjustSourceSets() {
        when (projectProperties.layout) {
            ProjectLayout.FLAT -> {
                java.sourceSets.all { sourceSet ->
                    val (srcPrefixPart, resourcesPrefixPart) = if (SourceSet.isMain(sourceSet))
                        "src" to "resources"
                    else sourceSet.name to "${sourceSet.name}Resources"


                    sourceSet.java.replace("src/${sourceSet.name}/java", "$srcPrefixPart@jvm")
                    sourceSet.resources.replace("src/${sourceSet.name}/resources", "$resourcesPrefixPart@jvm")
                }
            }

            else -> Unit
        }

        projectProperties.kotlin.sourceSets<KotlinJvmTarget>()?.forEach { sourceSet ->
            val compilationPrefix =
                if (sourceSet.name.endsWith(SourceSet.TEST_SOURCE_SET_NAME, true)) "test" else ""

            sourceSet.dependencies?.let { dependencies ->
                dependencies {
                    dependencies.forEach { dependency ->
                        add(
                            "$compilationPrefix${dependency.configuration.capitalized()}"
                                .decapitalized(),
                            dependency.resolve(),
                        )
                    }
                }
            }
        }
    }

    private fun Project.tryRegisterJvmStressTest() {
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
    private fun Project.tryRegisterJavaCodegenTestTask() =
        kotlin.targets.withType<org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget>().all { jvmTarget ->

            /**
             * This is an intermediate source set to make sure that we do not have expect/actual
             * in the same Kotlin module
             */
            val commonJavaCodegenTest = kotlin.sourceSets.create("commonJavaCodegenTest") {
                when (projectProperties.layout) {
                    ProjectLayout.FLAT -> kotlin.srcDir("test")
                    else -> kotlin.srcDir("src/commonTest/kotlin")
                }
            }

            val javaCodegenCompilation = jvmTarget.compilations.create("javaCodegenTest")

            jvmTarget.testRuns.create("javaCodegen").setExecutionSourceFrom(javaCodegenCompilation)

            javaCodegenCompilation.compileJavaTaskProvider?.configure {
                classpath += configurations.getByName("jvmTestCompileClasspath")
            }

            javaCodegenCompilation.configurations.compileDependencyConfiguration.extendsFrom(configurations.getByName("jvmTestCompileClasspath"))
            javaCodegenCompilation.configurations.runtimeDependencyConfiguration?.extendsFrom(configurations.getByName("jvmTestRuntimeClasspath"))
            javaCodegenCompilation.defaultSourceSet.dependsOn(commonJavaCodegenTest)
        }
}
