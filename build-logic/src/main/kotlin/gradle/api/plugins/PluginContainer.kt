package gradle.api.plugins

import gradle.plugin.use.PluginDependenciesSpecScope
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.api.initialization.dsl.ScriptHandler
import org.gradle.api.plugins.PluginAware
import org.gradle.api.plugins.PluginContainer
import org.gradle.kotlin.dsl.apply
import org.gradle.plugin.use.PluginDependency

public fun Settings.plugins(block: PluginDependenciesSpecScope.() -> Unit): Unit = plugins(buildscript, block)

public fun Project.plugins(block: PluginDependenciesSpecScope.() -> Unit): Unit = plugins(buildscript, block)

private fun PluginAware.plugins(
    buildscript: ScriptHandler,
    block: PluginDependenciesSpecScope.() -> Unit
): Unit = PluginDependenciesSpecScope().apply(block).pluginDependenciesSpec.forEach { pluginDependencySpec ->
    if (pluginDependencySpec.apply) {
        pluginManager.apply(pluginDependencySpec.pluginId)
    }

    if (pluginDependencySpec.version != null)
        buildscript.dependencies.add(
            "classpath",
            pluginDependencySpec.toString(),
        )
}

context(settings: Settings)
public fun PluginContainer.id(id: String): Unit = settings.plugins {
    id(id)
}

context(settings: Settings)
public fun PluginContainer.alias(notation: PluginDependency, apply: Boolean = true): Unit = settings.plugins {
    alias(settings.providers.provider { notation }) apply apply
}

context(project: Project)
public fun PluginContainer.id(id: String): Unit = project.plugins {
    id(id)
}

context(project: Project)
public fun PluginContainer.alias(notation: PluginDependency, apply: Boolean = true): Unit = project.plugins {
    alias(project.provider { notation }) apply apply
}
