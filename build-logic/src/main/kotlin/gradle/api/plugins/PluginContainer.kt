package gradle.api.plugins

import gradle.plugin.use.PluginDependenciesSpecScope
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.api.plugins.PluginAware
import org.gradle.api.plugins.PluginContainer
import org.gradle.kotlin.dsl.apply
import org.gradle.plugin.use.PluginDependency

private fun PluginAware.plugins(block: PluginDependenciesSpecScope.() -> Unit): Unit =
    PluginDependenciesSpecScope().apply(block).pluginDependenciesSpec.forEach { pluginDependencySpec ->
        if (pluginDependencySpec.apply) pluginManager.apply(pluginDependencySpec.pluginId)
    }

context(settings: Settings)
public fun PluginContainer.id(id: String): Unit = settings.plugins {
    id(id)
}

context(settings: Settings)
public fun PluginContainer.alias(notation: PluginDependency, apply: Boolean = true): Unit = settings.plugins {
    val notationProvider = settings.providers.provider { notation }
    settings.pluginManagement.plugins.alias(notationProvider)
    alias(notationProvider) apply apply
}

context(project: Project)
public fun PluginContainer.id(id: String): Unit = project.plugins {
    id(id)
}