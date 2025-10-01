package gradle.plugin.use

import org.gradle.kotlin.dsl.GradleDsl
import org.gradle.plugin.use.PluginDependenciesSpec

@GradleDsl
public open class PluginDependenciesSpecScope : PluginDependenciesSpec {

    internal val pluginDependenciesSpec = mutableSetOf<PluginDependencySpec>()

    public override fun id(id: String): PluginDependencySpec =
        PluginDependencySpec(id).also(pluginDependenciesSpec::add)
}
