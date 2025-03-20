package gradle.plugins.kmp.web


import gradle.api.trySet
import gradle.plugins.kmp.HasBinaries
import gradle.plugins.kotlin.KotlinCompilation
import org.gradle.api.Named

internal interface KotlinJsCompilation : KotlinCompilation, HasBinaries<KotlinJsBinaryContainer> {

    val outputModuleName: String?
    val packageJson: PackageJson?

        context(Project)
    override fun applyTo(recipient: T) {
        super.applyTo(named)

        named as org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJsCompilation

        binaries.applyTo(named.binaries)
        named::outputModuleName trySet outputModuleName

        packageJson?.let { packageJson ->
            named.packageJson {
                packageJson.applyTo(this)
            }
        }
    }
}
