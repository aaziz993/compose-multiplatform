package gradle.plugins.kotlin.cocoapods

import gradle.accessors.allLibs
import gradle.accessors.cocoapods
import gradle.accessors.id
import gradle.accessors.kotlin
import gradle.accessors.libs
import gradle.accessors.moduleName
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.resolveLibraryRef
import gradle.accessors.settings
import gradle.accessors.version
import gradle.accessors.versions
import gradle.act
import gradle.api.tryAddAll
import gradle.api.tryPlus
import gradle.api.trySet
import gradle.ifTrue
import gradle.plugins.kotlin.targets.nat.FrameworkSettings
import java.net.URI
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonObject
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

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
    val setExtraSpecAttributes: Map<String, String>?

    /**
     * Configurre framework of the pod built from this project.
     */
    val framework: FrameworkSettings?

    val ios: PodspecPlatformSettings?

    val osx: PodspecPlatformSettings?

    val tvos: PodspecPlatformSettings?

    val watchos: PodspecPlatformSettings?

    /**
     * Configure custom Xcode Configurations to Native Build Types mapping
     */
    val xcodeConfigurationToNativeBuildType: Map<String, NativeBuildType>?
    val setXcodeConfigurationToNativeBuildType: Map<String, NativeBuildType>?

    /**
     * Configure output directory for pod publishing
     */
    val publishDir: String?

    val specRepos: CocoapodsDependency.SpecRepos?

    /**
     * Add a CocoaPods dependency to the pod built from this project.
     *
     * @param linkOnly designates that the pod will be used only for dynamic framework linking and not for the cinterops. Code from it won't
     * be accessible for referencing from Kotlin but its native symbols will be visible while linking the framework.
     */
    val pods: Set<Pod>?

    val podDependencies: Set<@Serializable(with = CocoapodsDependencyKeyTransformingSerializer::class) CocoapodsDependency>?

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("cocoapods").id) {
            project.kotlin.cocoapods::version trySet (version
                ?: project.settings.libs.versions.version("kotlin.cocoapods.version"))
            project.kotlin.cocoapods::authors trySet authors
            project.kotlin.cocoapods::podfile trySet podfile?.let(project::file)
            needPodspec?.ifTrue(project.kotlin.cocoapods::noPodspec)
            project.kotlin.cocoapods.name = this@CocoapodsExtension.name ?: project.moduleName
            project.kotlin.cocoapods::license trySet license
            project.kotlin.cocoapods::summary trySet summary
            project.kotlin.cocoapods::homepage trySet homepage
            project.kotlin.cocoapods::source trySet source
            extraSpecAttributes?.let(project.kotlin.cocoapods.extraSpecAttributes::putAll)
            setExtraSpecAttributes
                ?.act(project.kotlin.cocoapods.extraSpecAttributes::clear)
                ?.let(project.kotlin.cocoapods.extraSpecAttributes::putAll)

            framework?.let { framework ->
                project.kotlin.cocoapods.framework {
                    framework.applyTo(this)
                }
            }

            xcodeConfigurationToNativeBuildType?.let(project.kotlin.cocoapods.xcodeConfigurationToNativeBuildType::putAll)
            setXcodeConfigurationToNativeBuildType
                ?.act(project.kotlin.cocoapods.xcodeConfigurationToNativeBuildType::clear)
                ?.let(project.kotlin.cocoapods.xcodeConfigurationToNativeBuildType::putAll)
            project.kotlin.cocoapods::publishDir trySet publishDir?.let(project::file)

            specRepos?.let { specRepos ->
                project.kotlin.cocoapods.specRepos(specRepos::applyTo)
            }

            pods?.forEach { pod ->
                project.kotlin.cocoapods.pod(
                    pod.name,
                    pod.version,
                    pod.path?.let(project::file),
                    pod.moduleName,
                    pod.headers,
                    pod.linkOnly,
                )
            }


            podDependencies?.forEach { podDependency ->
                podDependency.resolve()
                project.kotlin.cocoapods.pod(podDependency.name!!) {
                    podDependency.applyTo(this)
                }
            }

            ios?.applyTo(project.kotlin.cocoapods.ios, project.settings.libs.versions.version("kotlin.cocoapods.iosDeploymentTarget"))
            osx?.applyTo(project.kotlin.cocoapods.osx, project.settings.libs.versions.version("kotlin.cocoapods.osxDeploymentTarget"))
            tvos?.applyTo(project.kotlin.cocoapods.tvos, project.settings.libs.versions.version("kotlin.cocoapods.tvosDeploymentTarget"))
            watchos?.applyTo(project.kotlin.cocoapods.watchos, project.settings.libs.versions.version("kotlin.cocoapods.watchosDeploymentTarget"))
        }

    @Serializable
    data class CocoapodsDependency(
        var name: String? = null,
        val moduleName: String? = null,
        val headers: String? = null,
        var version: String? = null,
        val source: PodLocation? = null,
        val extraOpts: List<String>? = null,
        val setExtraOpts: List<String>? = null,
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
        val interopBindingDependencies: Set<String>? = null,
        val setInteropBindingDependencies: Set<String>? = null,
        /**
         * Path to local pod
         */
        val podspecDirectory: String? = null,
        val notation: String? = null,
    ) {

        context(Project)
        fun resolve() {
            notation?.let { notation ->
                if (notation.startsWith("$")) project.settings.allLibs.resolveLibraryRef(notation).removePrefix("cocoapods:")
                else notation
            }?.let { notation ->
                name = notation.substringBefore(":")
                version = notation.substringAfter(":", "").ifEmpty { null }
            }
        }

        context(Project)
        fun applyTo(receiver: CocoapodsExtension.CocoapodsDependency) {
            receiver::moduleName trySet moduleName
            receiver::headers trySet headers
            receiver::version trySet version
            receiver::source trySet source?.toPodLocation()
            receiver::extraOpts tryPlus extraOpts
            receiver::extraOpts trySet setExtraOpts
            receiver::packageName trySet packageName
            receiver::linkOnly trySet linkOnly
            receiver.interopBindingDependencies tryAddAll interopBindingDependencies
            receiver.interopBindingDependencies trySet setInteropBindingDependencies
            podspecDirectory?.let(receiver::path)
        }

        @Serializable
        data class SpecRepos(
            val urls: LinkedHashSet<String>? = null,
        ) {

            fun applyTo(receiver: CocoapodsExtension.SpecRepos) {
                urls?.forEach(receiver::url)
            }
        }

        @Serializable(with = PodLocationContentPolymorphicSerializer::class)
        sealed class PodLocation {

            @Serializable
            data class Path(
                val dir: String
            ) : PodLocation() {

                context(Project)
                override fun toPodLocation(): CocoapodsExtension.CocoapodsDependency.PodLocation =
                    CocoapodsExtension.CocoapodsDependency.PodLocation.Path(project.file(dir))
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

        object PodLocationContentPolymorphicSerializer : JsonContentPolymorphicSerializer<PodLocation>(PodLocation::class) {

            override fun selectDeserializer(element: JsonElement) =
                if (element.jsonObject.containsKey("dir")) PodLocation.Path.serializer()
                else PodLocation.Git.serializer()
        }
    }

    object CocoapodsDependencyKeyTransformingSerializer : JsonTransformingSerializer<CocoapodsDependency>(
        CocoapodsDependency.serializer(),
    ) {

        override fun transformDeserialize(element: JsonElement): JsonElement =
            element as? JsonObject
                ?: JsonObject(
                    mapOf(
                        "notation" to element,
                    ),
                )
    }

    @Serializable
    data class PodspecPlatformSettings(
        val deploymentTarget: String? = null
    ) {

        fun applyTo(settings: CocoapodsExtension.PodspecPlatformSettings, deploymentTarget: String?) {
            settings::deploymentTarget trySet (this@PodspecPlatformSettings.deploymentTarget ?: deploymentTarget)
        }
    }
}
