package gradle.plugins.android.sourceset

import com.android.build.api.dsl.AndroidSourceSet
import gradle.api.ProjectNamed
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * An AndroidSourceSet represents a logical group of Java, aidl and RenderScript sources
 * as well as Android and non-Android (Java-style) resources.
 */
@Serializable
internal data class AndroidSourceSet(
    /** Returns the name of this source set. */
    override val name: String? = null,,
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
) : ProjectNamed<AndroidSourceSet> {

    context(Project)
    @Suppress("UnstableApiUsage")
    override fun applyTo(recipient: AndroidSourceSet) {
        java?.applyTo(recipient.java)
        kotlin?.applyTo(recipient.kotlin)
        resources?.applyTo(recipient.resources)
        manifest?.applyTo(recipient.manifest)
        res?.applyTo(recipient.res)
        assets?.applyTo(recipient.assets)
        aidl?.applyTo(recipient.aidl)
        renderscript?.applyTo(recipient.renderscript)
        baselineProfiles?.applyTo(recipient.baselineProfiles)
        jniLibs?.applyTo(recipient.jniLibs)
        shaders?.applyTo(recipient.shaders)
        mlModels?.applyTo(recipient.mlModels)
        root?.let(recipient::setRoot)
    }
}
