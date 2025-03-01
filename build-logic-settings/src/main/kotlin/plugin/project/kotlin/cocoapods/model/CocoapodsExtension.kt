package plugin.project.kotlin.cocoapods.model

import gradle.moduleName
import gradle.projectProperties
import gradle.trySet
import java.net.URI
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import plugin.model.dependency.PodDependency
import plugin.project.kotlin.kmp.model.nat.Framework

internal interface CocoapodsExtension {

    /**
     * Configure version of the pod
     */
    val version: String?

    /**
     * Configure authors of the pod built from this project.
     */
    val authors: String?

    /**
     * Configure existing file `Podfile`.
     */
    val podfile: String?

    /**
     * Setup plugin not to produce podspec file for cocoapods section
     */
    val needPodspec: Boolean?

    /**
     * Configure name of the pod built from this project.
     */
    val name: String?

    /**
     * Configure license of the pod built from this project.
     */
    val license: String?

    /**
     * Configure description of the pod built from this project.
     */
    val summary: String?

    /**
     * Configure homepage of the pod built from this project.
     */
    val homepage: String?

    /**
     * Configure location of the pod built from this project.
     */
    val source: String?

    /**
     * Configure other podspec attributes
     */
    val extraSpecAttributes: Map<String, String>?

    /**
     * Configurre framework of the pod built from this project.
     */
    val framework: Framework?

    val ios: PodspecPlatformSettings?

    val osx: PodspecPlatformSettings?

    val tvos: PodspecPlatformSettings?

    val watchos: PodspecPlatformSettings?

    /**
     * Configure custom Xcode Configurations to Native Build Types mapping
     */
    val xcodeConfigurationToNativeBuildType: Map<String, NativeBuildType>?

    /**
     * Configure output directory for pod publishing
     */
    val publishDir: String?

    val specRepos: Set<String>?

    /**
     * Add a CocoaPods dependency to the pod built from this project.
     *
     * @param linkOnly designates that the pod will be used only for dynamic framework linking and not for the cinterops. Code from it won't
     * be accessible for referencing from Kotlin but its native symbols will be visible while linking the framework.
     */
    val pods: List<Pod>?

    val podDependencies: List<CocoapodsDependency>?

    context(Project)
    fun applyTo(extension: CocoapodsExtension) {
        extension::version trySet version
        extension::authors trySet authors
        extension::podfile trySet podfile?.let(::file)
        needPodspec?.takeIf { it }?.run { extension.noPodspec() }

        extension.name = name ?: moduleName
        extension::license trySet license
        extension::summary trySet summary
        extension::homepage trySet homepage
        extension::source trySet source
        extraSpecAttributes?.let(extension.extraSpecAttributes::putAll)

        framework?.let { framework ->
            extension.framework {
                framework.applyTo(this)
            }
        }

        xcodeConfigurationToNativeBuildType?.let(extension.xcodeConfigurationToNativeBuildType::putAll)
        extension::publishDir trySet publishDir?.let(::file)

        specRepos?.let { specRepos ->
            extension.specRepos {
                specRepos.forEach(::url)
            }
        }

        pods?.forEach { pod ->
            extension.pod(
                pod.name,
                pod.version,
                pod.path?.let(::file),
                pod.moduleName,
                pod.headers,
                pod.linkOnly,
            )
        }


        podDependencies?.forEach { podDependency ->
            extension.pod(podDependency.name) {
                podDependency.applyTo(this)
            }
        }

        projectProperties.dependencies
            ?.filterIsInstance<PodDependency>()
            ?.map { it.toCocoapodsDependency() }
            ?.forEach { podDependency ->
                extension.pod(podDependency.name) {
                    podDependency.applyTo(this)
                }
            }

        ios?.applyTo(extension.ios)
        osx?.applyTo(extension.osx)
        tvos?.applyTo(extension.tvos)
        watchos?.applyTo(extension.watchos)
    }

    @Serializable
    data class CocoapodsDependency(
        val name: String,
        val moduleName: String? = null,
        val headers: String? = null,
        val version: String? = null,
        val source: PodLocation? = null,
        val extraOpts: List<String>? = null,
        val packageName: String? = null,
        /**
         * Designates that the pod will be used only for dynamic framework linking and not for the cinterops. Code from it won't be
         * accessible for referencing from Kotlin but its native symbols will be visible while linking the framework.
         *
         * For static frameworks adding this flag is equivalent to removing the pod dependency entirely (because pods are not used for
         * static framework linking).
         */
        val linkOnly: Boolean? = null,
        /**
         * Contains a list of dependencies to other pods. This list will be used while building an interop Kotlin-binding for the pod.
         *
         * @see useInteropBindingFrom
         */
        val interopBindingDependencies: List<String>? = null,
        /**
         * Path to local pod
         */
        val podspecDirectory: String? = null,
    ) {

        context(Project)
        fun applyTo(dependency: CocoapodsExtension.CocoapodsDependency) {
            dependency::moduleName trySet moduleName
            dependency::headers trySet headers
            dependency::version trySet version
            dependency::source trySet source?.toPodLocation()
            dependency::extraOpts trySet extraOpts
            dependency::packageName trySet packageName
            dependency::linkOnly trySet linkOnly
            interopBindingDependencies?.let(dependency.interopBindingDependencies::addAll)
            podspecDirectory?.let(dependency::path)
        }

        @Serializable(with = PodLocationSerializer::class)
        sealed class PodLocation {

            @Serializable
            data class Path(
                val dir: String
            ) : PodLocation() {

                context(Project)
                override fun toPodLocation(): CocoapodsExtension.CocoapodsDependency.PodLocation =
                    CocoapodsExtension.CocoapodsDependency.PodLocation.Path(file(dir))
            }

            @Serializable
            data class Git(
                val url: String,
                val branch: String? = null,
                val tag: String? = null,
                val commit: String? = null
            ) : PodLocation() {

                context(Project)
                override fun toPodLocation(): CocoapodsExtension.CocoapodsDependency.PodLocation =
                    CocoapodsExtension.CocoapodsDependency.PodLocation.Git(
                        URI(url),
                        branch,
                        commit,
                    )
            }

            context(Project)
            abstract fun toPodLocation(): CocoapodsExtension.CocoapodsDependency.PodLocation
        }

        object PodLocationSerializer : JsonContentPolymorphicSerializer<PodLocation>(PodLocation::class) {

            override fun selectDeserializer(element: JsonElement) = when {
                element.jsonObject["dir"] != null -> PodLocation.Path.serializer()
                else -> PodLocation.Git.serializer()
            }
        }
    }

    //
    @Serializable
    data class PodspecPlatformSettings(
        val deploymentTarget: String? = null
    ) {

        fun applyTo(settings: CocoapodsExtension.PodspecPlatformSettings) {
            settings::deploymentTarget trySet deploymentTarget
        }
    }
}
