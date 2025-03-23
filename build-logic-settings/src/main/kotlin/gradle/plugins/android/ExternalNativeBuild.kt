package gradle.plugins.android

import com.android.build.api.dsl.ExternalNativeBuild
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/** See [com.android.build.api.dsl.ExternalNativeBuild]  */
@Serializable
internal data class ExternalNativeBuild(
    val ndkBuild: NdkBuildOptions? = null,
    val cmake: CmakeOptions? = null,
) {

    context(Project)
    fun applyTo(receiver: ExternalNativeBuild) {
        ndkBuild?.applyTo(receiver.ndkBuild)
        cmake?.applyTo(receiver.cmake)
    }
}
