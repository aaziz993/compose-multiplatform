package gradle.plugins.kotlin.cocoapods

import gradle.accessors.cocoapods
import gradle.accessors.kotlin
import gradle.accessors.moduleName
import gradle.accessors.settings
import klib.data.type.collection.tryAddAll
import klib.data.type.collection.tryPutAll
import klib.data.type.collection.trySet
import klib.data.type.reflection.trySet
import gradle.plugins.kotlin.targets.nat.FrameworkSettings
import klib.data.type.reflection.tryPlus
import java.net.URI
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonObject
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

@Serializable
internal data class CocoapodsExtension(
    /**
     * Configure version of the pod
     */
    val version: String? = null,
    /**
     * Configure authors of the pod built from this project.
     */
    val authors: String? = null,
    /**
     * Configure existing file `Podfile`.
     */
    val podfile: String? = null,
    /**
     * Setup plugin not to produce podspec file for cocoapods section
     */
    val needPodspec: Boolean? = null,
    /**
     * Configure name of the pod built from this project.
     */
    val name: String? = null,
    /**
     * Configure license of the pod built from this project.
     */
    val license: String? = null,
    /**
     * Configure description of the pod built from this project.
     */
    val summary: String? = null,
    /**
     * Configure homepage of the pod built from this project.
     */
    val homepage: String? = null,
    /**
     * Configure location of the pod built from this project.
     */
    val source: String? = null,
    /**
     * Configure other podspec attributes
     */
    val extraSpecAttributes: Map<String, String>? = null,
    val setExtraSpecAttributes: Map<String, String>? = null,
    /**
     * Configurre framework of the pod built from this project.
     */
    val framework: FrameworkSettings? = null,
    val ios: PodspecPlatformSettings? = null,
    val osx: PodspecPlatformSettings? = null,
    val tvos: PodspecPlatformSettings? = null,
    val watchos: PodspecPlatformSettings? = null,
    /**
     * Configure custom Xcode Configurations to Native Build Types mapping
     */
    val xcodeConfigurationToNativeBuildType: Map<String, NativeBuildType>? = null,
    val setXcodeConfigurationToNativeBuildType: Map<String, NativeBuildType>? = null,
    /**
     * Configure output directory for pod publishing
     */
    val publishDir: String? = null,
    val specRepos: CocoapodsDependency.SpecRepos? = null,
    /**
     * Add a CocoaPods dependency to the pod built from this project.
     *
     * @param linkOnly designates that the pod will be used only for dynamic framework linking and not for the cinterops. Code from it won't
     * be accessible for referencing from Kotlin but its native symbols will be visible while linking the framework.
     */
    val pods: Set<Pod>? = null,
    val podDependencies: Set<CocoapodsDependency>? = null,
) {

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin("org.jetbrains.kotlin.native.cocoapods") {
            project.kotlin.cocoapods::version trySet (version
                ?: project.settings.libs.versionOrNull("kotlin.cocoapods.version"))
            project.kotlin.cocoapods::authors trySet authors
            project.kotlin.cocoapods::podfile trySet podfile?.let(project::file)
            project.kotlin.cocoapods::noPodspec trySet needPodspec
            project.kotlin.cocoapods.name = this@CocoapodsExtension.name ?: project.moduleName
            project.kotlin.cocoapods::license trySet license
            project.kotlin.cocoapods::summary trySet summary
            project.kotlin.cocoapods::homepage trySet homepage
            project.kotlin.cocoapods::source trySet source
            project.kotlin.cocoapods.extraSpecAttributes tryPutAll extraSpecAttributes
            project.kotlin.cocoapods.extraSpecAttributes trySet setExtraSpecAttributes

            framework?.let { framework ->
                project.kotlin.cocoapods.framework {
                    framework.applyTo(this)
                }
            }

            project.kotlin.cocoapods.xcodeConfigurationToNativeBuildType tryPutAll xcodeConfigurationToNativeBuildType
            project.kotlin.cocoapods.xcodeConfigurationToNativeBuildType trySet setXcodeConfigurationToNativeBuildType
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

            ios?.applyTo(project.kotlin.cocoapods.ios, project.settings.libs.versionOrNull("kotlin.cocoapods.iosDeploymentTarget"))
            osx?.applyTo(project.kotlin.cocoapods.osx, project.settings.libs.versionOrNull("kotlin.cocoapods.osxDeploymentTarget"))
            tvos?.applyTo(project.kotlin.cocoapods.tvos, project.settings.libs.versionOrNull("kotlin.cocoapods.tvosDeploymentTarget"))
            watchos?.applyTo(project.kotlin.cocoapods.watchos, project.settings.libs.versionOrNull("kotlin.cocoapods.watchosDeploymentTarget"))
        }

    @KeepGeneratedSerializer
    @Serializable(with = CocoapodsDependencyObjectTransformingSerializer::class)
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
                (project.settings.allLibs.resolveDependency(notation, project.layout.projectDirectory) as? String
                    ?: throw UnsupportedOperationException("Couldn't resolve cocoapods dependency"))
                    .removePrefix("cocoapods:")
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
                if (PodLocation.Path::dir.name in element.jsonObject) PodLocation.Path.serializer()
                else PodLocation.Git.serializer()
        }
    }

    object CocoapodsDependencyObjectTransformingSerializer : JsonTransformingSerializer<CocoapodsDependency>(
        CocoapodsDependency.generatedSerializer(),
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
