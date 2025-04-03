package gradle.plugins.kotlin.targets.web

import gradle.plugins.kotlin.KotlinCompilation
import gradle.plugins.kotlin.mpp.HasBinaries
import gradle.plugins.kotlin.targets.web.tasks.Kotlin2JsCompile
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJsCompilation

internal abstract class KotlinJsCompilation<T : KotlinJsCompilation>
    : KotlinCompilation<T>,
    HasBinaries<KotlinJsBinaryContainer> {

    abstract override val compileTaskProvider: Kotlin2JsCompile<out org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile>?

    abstract val packageJsonHandlers: List<PackageJson>?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        binaries?.applyTo(receiver.binaries)

        packageJsonHandlers?.forEach { packageJsonHandler ->
            receiver.packageJson {
                packageJsonHandler.applyTo(this)
            }
        }
    }
}
