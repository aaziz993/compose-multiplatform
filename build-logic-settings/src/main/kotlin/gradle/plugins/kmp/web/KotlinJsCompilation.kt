package gradle.plugins.kmp.web

import gradle.api.trySet
import gradle.plugins.kmp.HasBinaries
import gradle.plugins.kotlin.KotlinCompilation
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJsCompilation

internal interface KotlinJsCompilation<T : KotlinJsCompilation>
    : KotlinCompilation<T>,
    HasBinaries<@Serializable(with = KotlinJsBinaryContainerTransformingSerializer::class) KotlinJsBinaryContainer> {

    val packageJson: PackageJson?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        binaries?.applyTo(receiver.binaries)

        packageJson?.let { packageJson ->
            receiver.packageJson {
                packageJson.applyTo(this)
            }
        }
    }
}
