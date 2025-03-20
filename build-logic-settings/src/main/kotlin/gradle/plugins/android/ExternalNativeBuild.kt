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
    fun applyTo(recipient: ExternalNativeBuild) {
        ndkBuild?.applyTo(recipient.ndkBuild)
        cmake?.applyTo(recipient.cmake)
    }
}
