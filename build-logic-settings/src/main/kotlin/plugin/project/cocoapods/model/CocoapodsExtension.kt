package plugin.apple.cocoapods.model

import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import plugin.project.cocoapods.model.Pod
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

    val ios: CocoapodsExtension.PodspecPlatformSettings?

    val osx: CocoapodsExtension.PodspecPlatformSettings?

    val tvos: CocoapodsExtension.PodspecPlatformSettings?

    val watchos: CocoapodsExtension.PodspecPlatformSettings?

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
        val source: String?=null,
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

    )

    //
    @Serializable
    data class PodspecPlatformSettings(
        val deploymentTarget: String? = null
    )
}
