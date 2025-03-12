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
    fun applyTo(build: ExternalNativeBuild) {
        ndkBuild?.applyTo(build.ndkBuild)
        cmake?.applyTo(build.cmake)
    }
}
