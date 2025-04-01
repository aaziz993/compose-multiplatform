package gradle.plugins.android.sourceset

import gradle.api.NamedObjectTransformingSerializer
import gradle.api.ProjectNamed
import klib.data.type.reflection.trySet
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * An AndroidSourceSet represents a logical group of Java, aidl and RenderScript sources
 * as well as Android and non-Android (Java-style) resources.
 */
@KeepGeneratedSerializer
@Serializable(with = AndroidSourceSetObjectTransformingSerializer::class)
internal data class AndroidSourceSet(
    /** Returns the name of this source set. */
    override val name: String? = null,
    /** The Java source for this source-set */
    val java: AndroidSourceDirectorySet? = null,
    /** The Kotlin source for this source-set */
    val kotlin: AndroidSourceDirectorySet? = null,
    /** The Java-style resources for this source-set */
    val resources: AndroidSourceDirectorySet? = null,
    /** The Android Manifest file for this source-set. */
    val manifest: AndroidSourceFile? = null,
    /** The Android Resources directory for this source-set. */
    val res: AndroidSourceDirectorySet? = null,
    /** The Android Assets directory for this source set.*/
    val assets: AndroidSourceDirectorySet? = null,
    /** The Android AIDL source directory for this source set. */
    val aidl: AndroidSourceDirectorySet? = null,
    /** The Android RenderScript source directory for this source set. */
    val renderscript: AndroidSourceDirectorySet? = null,
    /** The Android Baseline Profiles source directory for this source set. */
    val baselineProfiles: AndroidSourceDirectorySet? = null,
    /** The Android JNI libs directory for this source-set */
    val jniLibs: AndroidSourceDirectorySet? = null,
    /** The Android shaders directory for this source set. */
    val shaders: AndroidSourceDirectorySet? = null,
    /** The machine learning models directory for this source set. */
    val mlModels: AndroidSourceDirectorySet? = null,
    /**
     * Sets the root of the source sets to a given path.
     *
     * All entries of the source-set are located under this root directory.
     *
     * This method has a return value for legacy reasons.
     *
     * @param path the root directory path to use.
     */
    val root: String? = null,
) : ProjectNamed<com.android.build.api.dsl.AndroidSourceSet> {

    context(Project)
    @Suppress("UnstableApiUsage")
    override fun applyTo(receiver: com.android.build.api.dsl.AndroidSourceSet) {
        java?.applyTo(receiver.java)
        kotlin?.applyTo(receiver.kotlin)
        resources?.applyTo(receiver.resources)
        manifest?.applyTo(receiver.manifest)
        res?.applyTo(receiver.res)
        assets?.applyTo(receiver.assets)
        aidl?.applyTo(receiver.aidl)
        renderscript?.applyTo(receiver.renderscript)
        baselineProfiles?.applyTo(receiver.baselineProfiles)
        jniLibs?.applyTo(receiver.jniLibs)
        shaders?.applyTo(receiver.shaders)
        mlModels?.applyTo(receiver.mlModels)
        receiver::setRoot trySet root
    }
}

private object AndroidSourceSetObjectTransformingSerializer
    : NamedObjectTransformingSerializer<AndroidSourceSet>(AndroidSourceSet.generatedSerializer())
