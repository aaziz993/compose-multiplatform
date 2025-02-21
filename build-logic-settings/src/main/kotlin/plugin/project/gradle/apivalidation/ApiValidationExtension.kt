package plugin.project.gradle.apivalidation

import gradle.apiValidation
import gradle.moduleProperties
import gradle.trySet
import kotlinx.validation.BinaryCompatibilityValidatorPlugin
import kotlinx.validation.ExperimentalBCVApi
import kotlinx.validation.KotlinApiBuildTask
import kotlinx.validation.api.klib.KlibSignatureVersion
import org.gradle.api.Project
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType

@OptIn(ExperimentalBCVApi::class)
internal fun Project.configureApiValidationExtension() =
    plugins.withType<BinaryCompatibilityValidatorPlugin> {
        moduleProperties.settings.gradle.apiValidation.let { apiValidation ->
            apiValidation {
                ::validationDisabled trySet apiValidation.validationDisabled
                ::ignoredPackages trySet apiValidation.ignoredPackages?.toMutableSet()
                ::ignoredProjects trySet apiValidation.ignoredProjects?.toMutableSet()
                ::nonPublicMarkers trySet apiValidation.nonPublicMarkers?.toMutableSet()
                ::ignoredClasses trySet apiValidation.ignoredClasses?.toMutableSet()
                ::publicMarkers trySet apiValidation.publicMarkers?.toMutableSet()
                ::publicPackages trySet apiValidation.publicPackages?.toMutableSet()
                ::publicClasses trySet apiValidation.publicClasses?.toMutableSet()
                ::additionalSourceSets trySet apiValidation.additionalSourceSets?.toMutableSet()
                ::apiDumpDirectory trySet apiValidation.apiDumpDirectory

                apiValidation.klib?.let { klib ->
                    klib {
                        ::enabled trySet klib.enabled
                        ::signatureVersion trySet klib.signatureVersion?.let(KlibSignatureVersion::of)
                        ::strictValidation trySet klib.strictValidation
                    }
                }
            }
        }

        tasks {
//            apiBuild {
//                // "jar" here is the name of the default Jar task producing the resulting jar file
//                // in a multiplatform project it can be named "jvmJar"
//                // if you applied the shadow plugin, it creates the "shadowJar" task that produces the transformed jar
//                inputJar.value(jar.flatMap { it.archiveFile })
//            }
        }
    }

private val TaskContainer.apiBuild: TaskProvider<KotlinApiBuildTask>
    get() = named<KotlinApiBuildTask>("apiBuild")

private val TaskContainer.jar: TaskProvider<Jar>
    get() = named<Jar>("jar")
