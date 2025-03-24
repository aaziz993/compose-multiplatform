package gradle.plugins.android

import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.VariantDimension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import gradle.accessors.android
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import gradle.plugins.android.compile.JavaCompileOptions
import org.gradle.api.Project

/**
 * Shared properties between DSL objects that contribute to a variant.
 *
 * That is, [BuildType] and [gradle.plugins.android.flavor.ProductFlavorDsl] and [gradle.plugins.android.defaultconfig.DefaultConfigDsl].
 */
internal interface VariantDimension<T : VariantDimension> {

    /**
     * Text file with additional ProGuard rules to be used to determine which classes are compiled
     * into the main dex file.
     *
     * If set, rules from this file are used in combination with the default rules used by the
     * build system.
     */
    val multiDexKeepProguard: String?

    /** Encapsulates per-variant configurations for the NDK, such as ABI filters.  */
    val ndk: Ndk?

    /**
     * Specifies the ProGuard configuration files that the plugin should use.
     *
     * There are two ProGuard rules files that ship with the Android plugin and are used by
     * default:
     *
     *  * proguard-android.txt
     *  * proguard-android-optimize.txt
     *
     * `proguard-android-optimize.txt` is identical to `proguard-android.txt`,
     * except with optimizations enabled. You can use [getDefaultProguardFile(String)]
     * to return the full path of the files.
     *
     * @return a non-null collection of files.
     */
    val proguardFiles: List<String>?

    /**
     * Replaces the ProGuard configuration files.
     *
     * This method has a return value for legacy reasons.
     */
    val setProguardFiles: List<String>?

    val defaultProguardFiles: List<String>?

    val setDefaultProguardFiles: List<String>?

    /**
     * The collection of proguard rule files to be used when processing test code.
     *
     * Test code needs to be processed to apply the same obfuscation as was done to main code.
     */
    val testProguardFiles: List<String>?

    /**
     * The manifest placeholders.
     *
     * See
     * [Inject Build Variables into the Manifest](https://developer.android.com/studio/build/manifest-build-variables.html).
     */
    val manifestPlaceholders: SerializableAnyMap?

    /** Options for configuring Java compilation. */
    val javaCompileOptions: JavaCompileOptions?

    /** Options for configuring the shader compiler.  */
    val shaders: Shaders?

    /**
     * Encapsulates per-variant CMake and ndk-build configurations for your external native build.
     *
     * To learn more, see
     * [Add C and C++ Code to Your Project](http://developer.android.com/studio/projects/add-native-code.html#).
     */

    val externalNativeBuild: ExternalNativeBuildFlags?

    /**
     * Adds a new field to the generated BuildConfig class.
     *
     *
     * The field is generated as: `<type> <name> = <value>;`
     *
     *
     * This means each of these must have valid Java content. If the type is a String, then the
     * value should include quotes.
     *
     * @param type the type of the field
     * @param name the name of the field
     * @param value the value of the field
     */
    val buildConfigFields: List<BuildConfigField>?

    /**
     * Adds a new generated resource.
     *
     *
     * This is equivalent to specifying a resource in res/values.
     *
     *
     * See [Resource Types](http://developer.android.com/guide/topics/resources/available-resources.html).
     *
     * @param type the type of the resource
     * @param name the name of the resource
     * @param value the value of the resource
     */
    val resValues: List<ResValue>?

    val optimization: Optimization?

    context(project: Project)
    @Suppress("UnstableApiUsage")
    fun applyTo(receiver: T) {
        receiver::multiDexKeepProguard trySet multiDexKeepProguard?.let(project::file)

        ndk?.applyTo(receiver.ndk)

        defaultProguardFiles
            ?.mapNotNull { defaultProguardFile -> project.getDefaultProguardFile(defaultProguardFile) }
            ?.toTypedArray()?.let(receiver::proguardFiles)

        setProguardFiles?.let(receiver::setProguardFiles)

        setDefaultProguardFiles
            ?.mapNotNull { defaultProguardFile -> project.getDefaultProguardFile(defaultProguardFile) }
            ?.let(receiver::setProguardFiles)

        testProguardFiles?.toTypedArray()?.let(receiver::testProguardFiles)

        manifestPlaceholders?.let(receiver.manifestPlaceholders::putAll)

        javaCompileOptions?.let { javaCompileOptions ->
            receiver.javaCompileOptions(javaCompileOptions::applyTo)
        }

        shaders?.let { shaders ->
            receiver.shaders(shaders::applyTo)
        }

        externalNativeBuild?.let { externalNativeBuild ->
            receiver.externalNativeBuild(externalNativeBuild::applyTo)
        }

        buildConfigFields?.forEach { (type, name, value) ->
            receiver.buildConfigField(type, name, value)
        }

        resValues?.forEach { (type, name, value) ->
            receiver.resValue(type, name, value)
        }

        receiver.optimization { }

        optimization?.let { optimization ->
            receiver.optimization(optimization::applyTo)
        }
    }

    private fun Project.getDefaultProguardFile(name: String) = when (android) {
        is BaseAppModuleExtension -> android.getDefaultProguardFile(name)
        is LibraryExtension -> android.getDefaultProguardFile(name)
        else -> null
    }
}




