package gradle.plugins.kmp.web


import gradle.api.trySet
import gradle.plugins.kmp.HasBinaries
import gradle.plugins.kotlin.KotlinCompilation

internal interface KotlinJsCompilation : KotlinCompilation, HasBinaries<KotlinJsBinaryContainer> {

    val outputModuleName: String?
    val packageJson: PackageJson?

        context(project: Project)
    override fun applyTo(receiver: T) {
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
