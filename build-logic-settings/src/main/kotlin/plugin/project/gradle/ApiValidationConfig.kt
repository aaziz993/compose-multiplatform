package plugin.project.gradle

import gradle.libs
import kotlinx.validation.ApiValidationExtension
import kotlinx.validation.ExperimentalBCVApi
import kotlinx.validation.KotlinApiBuildTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.named

internal fun Project.configureApiValidation() {
    // The tool allows dumping binary API of a JVM part of a Kotlin library that is public in the sense of Kotlin visibilities and ensures that the public binary API wasn't changed in a way that makes this change binary incompatible.
    apply(plugin = libs.plugins.binary.compatibility.validator.get().pluginId)

    extensions.configure<ApiValidationExtension>(::configureApiValidationExtension)
}

private fun Project.configureApiValidationExtension(extension: ApiValidationExtension) {

    extension.apply {
        /**
         * Packages that are excluded from public API dumps even if they
         * contain public API.
         */
        ignoredPackages.addAll(
            providers.gradleProperty("api.validation.ignore.packages").get()
                .ifBlank { null }?.split(",").orEmpty().map(String::trim),
        )

        /**
         * Sub-projects that are excluded from API validation
         */
        ignoredProjects.addAll(
            providers.gradleProperty("api.validation.ignore.projects").get()
                .ifBlank { null }?.split(",").orEmpty().map(String::trim),
        )

        /**
         * Classes (fully qualified) that are excluded from public API dumps even if they
         * contain public API.
         */
        ignoredClasses.addAll(
            providers.gradleProperty("api.validation.ignore.classes").get()
                .ifBlank { null }?.split(",").orEmpty().map(String::trim),
        )

        /**
         * Set of annotations that exclude API from being public.
         * Typically, it is all kinds of `@InternalApi` annotations that mark
         * effectively private API that cannot be actually private for technical reasons.
         */
        nonPublicMarkers.addAll(
            providers.gradleProperty("api.validation.non-public-markers").get()
                .ifBlank { null }?.split(",").orEmpty().map(String::trim),
        )

        /**
         * Flag to programmatically disable compatibility validator
         */
        validationDisabled = providers.gradleProperty("api.validation.enable").get().toBoolean()

        /**
         * A path to a subdirectory inside the project root directory where dumps should be stored.
         */
        apiDumpDirectory = providers.gradleProperty("api.validation.api-dump-directory").get()

        /**
         * The KLib validation support is experimental and is a subject to change (applies to both an API and the ABI dump format). A project has to use Kotlin 1.9.20 or newer to use this feature.
         */
        @OptIn(ExperimentalBCVApi::class)
        klib {
            enabled = providers.gradleProperty("api.validation.klib.enable").get().toBoolean()
            // treat a target being unsupported on a host as an error
            strictValidation = providers.gradleProperty("api.validation.klib.strict-validation").get().toBoolean()
        }

        tasks {
            apiBuild {
                // "jar" here is the name of the default Jar task producing the resulting jar file
                // in a multiplatform project it can be named "jvmJar"
                // if you applied the shadow plugin, it creates the "shadowJar" task that produces the transformed jar
                inputJar.value(jar.flatMap { it.archiveFile })
            }
        }
    }
}

private val TaskContainer.apiBuild: TaskProvider<KotlinApiBuildTask>
    get() = named<KotlinApiBuildTask>("apiBuild")

private val TaskContainer.jar: TaskProvider<Jar>
    get() = named<Jar>("jar")
