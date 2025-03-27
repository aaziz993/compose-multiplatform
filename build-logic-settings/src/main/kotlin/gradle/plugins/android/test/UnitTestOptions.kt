package gradle.plugins.android.test

import com.android.build.api.dsl.UnitTestOptions
import gradle.api.trySet
import kotlinx.serialization.Serializable

/** Options for controlling unit tests execution. */
@Serializable
internal data class UnitTestOptions(
    /**
     * Whether unmocked methods from android.jar should throw exceptions or return default
     * values (i.e. zero or null).
     *
     * See [Test Your App](https://developer.android.com/studio/test/index.html) for details.
     *
     * since 1.1.0
     */
    val isReturnDefaultValues: Boolean? = null,
    /**
     * Enables unit tests to use Android resources, assets, and manifests.
     *
     * If you set this property to <code>true</code>, the plugin performs resource, asset,
     * and manifest merging before running your unit tests. Your tests can then inspect a file
     * called `com/android/tools/test_config.properties` on the classpath, which is a Java
     * properties file with the following keys:
     *
     * `android_resource_apk`: the path to the APK-like zip file containing merged resources, which
     * includes all the resources from the current subproject and all its dependencies.
     *
     * `android_merged_assets`: the path to the directory containing merged assets. For app
     * subprojects, the merged assets directory contains assets from the current subproject and its
     * dependencies. For library subprojects, the merged assets directory contains only assets from
     * the current subproject.
     *
     * `android_merged_manifest`: the path to the merged manifest file. Only app subprojects have
     * the manifest merged from their dependencies. Library subprojects do not include manifest
     * components from their dependencies.
     *
     * `android_custom_package`: the package name (namespace) of the final R class.
import gradle.accessors.files
import gradle.api.trySet
     *
     * Note that the paths above are relative paths (relative to the current project directory, not
     * the root project directory).
     *
     * since 3.0.0
     */
    val isIncludeAndroidResources: Boolean? = null,
) {

    fun applyTo(receiver: UnitTestOptions) {
        receiver::isReturnDefaultValues trySet isReturnDefaultValues
        receiver::isIncludeAndroidResources trySet isIncludeAndroidResources
    }
}
