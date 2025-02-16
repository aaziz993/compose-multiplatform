package plugin.project.gradle

import gradle.libs
import kotlinx.atomicfu.plugin.gradle.AtomicFUPluginExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.apply

internal fun Project.configureAtomicFUPlugin(){
    apply(plugin=libs.plugins.atomicfu.get().pluginId)

    extensions.configure<AtomicFUPluginExtension>(::configureAtomicFUPluginExtension)
}


private fun Project.configureAtomicFUPluginExtension(extension: AtomicFUPluginExtension): AtomicFUPluginExtension = extension.apply {
}
