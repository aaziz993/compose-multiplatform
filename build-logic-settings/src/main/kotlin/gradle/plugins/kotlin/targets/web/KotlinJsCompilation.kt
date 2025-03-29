package gradle.plugins.kotlin.targets.web

import gradle.plugins.kotlin.KotlinCompilation
import gradle.plugins.kotlin.mpp.HasBinaries
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJsCompilation

internal interface KotlinJsCompilation<T : KotlinJsCompilation>
    : KotlinCompilation<T>,
    HasBinaries<KotlinJsBinaryContainer> {

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
