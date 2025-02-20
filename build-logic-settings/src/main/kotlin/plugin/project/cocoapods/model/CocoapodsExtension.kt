//package plugin.apple.cocoapods.model
//
//import kotlinx.serialization.Serializable
//import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
//import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
//
//internal interface CocoapodsExtension  {
//    /**
//     * Configure version of the pod
//     */
//    var version: String?
//
//    /**
//     * Configure authors of the pod built from this project.
//     */
//    var authors: String?
//
//    /**
//     * Configure existing file `Podfile`.
//     */
//    var podfile: String?
//
//    /**
//     * Setup plugin not to produce podspec file for cocoapods section
//     */
//    var needPodspec: Boolean?
//
//    /**
//     * Configure name of the pod built from this project.
//     */
//    var name: String?
//
//    /**
//     * Configure license of the pod built from this project.
//     */
//    var license: String?
//
//    /**
//     * Configure description of the pod built from this project.
//     */
//    var summary: String?
//
//    /**
//     * Configure homepage of the pod built from this project.
//     */
//    var homepage: String?
//
//    /**
//     * Configure location of the pod built from this project.
//     */
//    var source: String?
//
//    /**
//     * Configure other podspec attributes
//     */
//    var extraSpecAttributes: MutableMap<String, String> = mutableMapOf()
//
//    /**
//     * Configure framework of the pod built from this project.
//     */
//    fun framework(configure: Framework.() -> Unit) {
//        forAllPodFrameworks(configure)
//    }
//
//    /**
//     * Configure framework of the pod built from this project.
//     */
//    fun framework(configure: Action<Framework>) {
//        forAllPodFrameworks(configure)
//    }
//
//    val ios: PodspecPlatformSettings = PodspecPlatformSettings("ios")
//
//    val osx: PodspecPlatformSettings = PodspecPlatformSettings("osx")
//
//    val tvos: PodspecPlatformSettings = PodspecPlatformSettings("tvos")
//
//    val watchos: PodspecPlatformSettings = PodspecPlatformSettings("watchos")
//
//
//     val podFrameworkName: String?
//     val podFrameworkIsStatic: Boolean?
//
//    /**
//     * Configure custom Xcode Configurations to Native Build Types mapping
//     */
//    val xcodeConfigurationToNativeBuildType: Map<String, NativeBuildType> ?
//
//    /**
//     * Configure output directory for pod publishing
//     */
//    val publishDir: String?
//
//    val specRepos:SpecRepos?
//
//    private val _pods = project.container(CocoapodsDependency::class.java)
//
//    val podsAsTaskInput: List<CocoapodsDependency>
//        get() = _pods.toList()
//
//    /**
//     * Returns a list of pod dependencies.
//     */
//    val pods: NamedDomainObjectSet<CocoapodsDependency>
//        get() = _pods
//
//    /**
//     * Add a CocoaPods dependency to the pod built from this project.
//     *
//     * @param linkOnly designates that the pod will be used only for dynamic framework linking and not for the cinterops. Code from it won't
//     * be accessible for referencing from Kotlin but its native symbols will be visible while linking the framework.
//     */
//    @JvmOverloads
//    fun pod(
//        name: String,
//        version: String?,
//        path: File?,
//        moduleName: String = name.asModuleName(),
//        headers: String?,
//        linkOnly: Boolean = false,
//    ) {
//        // Empty string will lead to an attempt to create two podDownload tasks.
//        // One is original podDownload and second is podDownload + pod.name
//        require(name.isNotEmpty()) { "Please provide not empty pod name to avoid ambiguity" }
//        addToPods(
//            project.objects.newInstance(
//                CocoapodsDependency::class.java,
//                name,
//                moduleName
//            ).apply {
//                this.headers = headers
//                this.version = version
//                source = path?.let(::Path)
//                this.linkOnly = linkOnly
//            }
//        )
//    }
//
//
//    /**
//     * Add a CocoaPods dependency to the pod built from this project.
//     */
//    fun pod(name: String, configure: CocoapodsDependency.() -> Unit) {
//        // Empty string will lead to an attempt to create two podDownload tasks.
//        // One is original podDownload and second is podDownload + pod.name
//        require(name.isNotEmpty()) { "Please provide not empty pod name to avoid ambiguity" }
//        val dependency = project.objects.newInstance(CocoapodsDependency::class.java, name, name.asModuleName())
//        dependency.configure()
//        addToPods(dependency)
//    }
//
//    /**
//     * Add a CocoaPods dependency to the pod built from this project.
//     */
//    fun pod(name: String, configure: Action<CocoapodsDependency>) = pod(name) {
//        configure.execute(this)
//    }
//
//    private fun addToPods(dependency: CocoapodsDependency) {
//        val name = dependency.name
//        check(_pods.findByName(name) == null) { "Project already has a CocoaPods dependency with name $name" }
//        _pods.add(dependency)
//    }
//
//    /**
//     * Add spec repositories (note that spec repository is different from usual git repository).
//     * Please refer to <a href="https://guides.cocoapods.org/making/private-cocoapods.html">cocoapods documentation</a>
//     * for additional information.
//     * Default sources (cdn.cocoapods.org) implicitly included.
//     */
//    fun specRepos(configure: SpecRepos.() -> Unit) = specRepos.configure()
//
//    /**
//     * Add spec repositories (note that spec repository is different from usual git repository).
//     * Please refer to <a href="https://guides.cocoapods.org/making/private-cocoapods.html">cocoapods documentation</a>
//     * for additional information.
//     * Default sources (cdn.cocoapods.org) implicitly included.
//     */
//    fun specRepos(configure: Action<SpecRepos>) = specRepos {
//        configure.execute(this)
//    }
//
//
//
//    data class CocoapodsDependency(
//        private val name: String,
//        @get:Input var moduleName: String
//    ) : Named {
//
//        val headers: String?
//
//        val version: String?
//
//        val source: PodLocation?
//
//
//        val extraOpts: List<String>?
//
//        val packageName: String ?
//
//        /**
//         * Designates that the pod will be used only for dynamic framework linking and not for the cinterops. Code from it won't be
//         * accessible for referencing from Kotlin but its native symbols will be visible while linking the framework.
//         *
//         * For static frameworks adding this flag is equivalent to removing the pod dependency entirely (because pods are not used for
//         * static framework linking).
//         */
//        val linkOnly: Boolean?
//
//        /**
//         * Contains a list of dependencies to other pods. This list will be used while building an interop Kotlin-binding for the pod.
//         *
//         * @see useInteropBindingFrom
//         */
//        val interopBindingDependencies: MutableList<String>?
//
//        /**
//         * Specify that the pod depends on another pod **podName** and a Kotlin-binding for **podName** should be used while building
//         * a binding for the pod. This is necessary if you need to operate entities from **podName** and from the pod together, for
//         * instance pass an object from **podName** to the pod in Kotlin.
//         *
//         * A pod with the exact name must be declared before calling this function.
//         *
//         * @see interopBindingDependencies
//         */
//        fun useInteropBindingFrom(podName: String) {
//            interopBindingDependencies.add(podName)
//        }
//
//        @Input
//        override fun getName(): String = name
//
//        /**
//         * Path to local pod
//         */
//        fun path(podspecDirectory: String): PodLocation = Path(File(podspecDirectory))
//
//        /**
//         * Path to local pod
//         */
//        fun path(podspecDirectory: File): PodLocation = Path(podspecDirectory)
//
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
//
//        sealed class PodLocation {
//            data class Path(
//                val dir: String
//            )
//
//            data class Git(
//                val url: String,
//                var branch: String?,
//                val tag: String?,
//                val commit: String?
//            )
//        }
//    }
//
//    @Serializable
//    data class PodspecPlatformSettings(
//        val name: String,
//        val deploymentTarget: String?
//    )
//
//    @Serializable
//    data class SpecRepos(
//         val specRepos:Set<String>?
//    )
//}
