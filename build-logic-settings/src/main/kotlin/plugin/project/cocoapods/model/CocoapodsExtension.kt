package plugin.project.cocoapods.model

import java.net.URI
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import plugin.project.kotlinnative.model.Framework

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
//        /**
//         * Configure pod from git repository. The podspec file is expected to be in the repository root.
//         */
//        @JvmOverloads
//        fun git(url: String, configure: (Git.() -> Unit)?): PodLocation {
//            val git = Git(URI(url))
//            if (configure != null) {
//                git.configure()
//            }
//            return git
//        }
//
//        /**
//         * Configure pod from git repository. The podspec file is expected to be in the repository root.
//         */
//        fun git(url: String, configure: Action<Git>) = git(url) {
//            configure.execute(this)
//        }

    ) {

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
    )
}
