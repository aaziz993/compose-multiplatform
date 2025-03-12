package gradle.plugins.kmp.web

import gradle.plugins.HasBinaries
import gradle.plugins.kotlin.KotlinCompilation
import gradle.trySet
import org.gradle.api.Named
import org.gradle.api.Project

internal interface KotlinJsCompilation : KotlinCompilation, HasBinaries<KotlinJsBinaryContainer> {

    val outputModuleName: String?
    val packageJson: PackageJson?

    context(Project)
    override fun applyTo(named: Named) {
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
