package plugin.project.gradle

import com.github.gmazzo.gradle.plugins.BuildConfigExtension
import gradle.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.apply

internal fun Project.configureBuildConfig(){
    apply(plugin=libs.plugins.build.config.get().pluginId)

    extensions.configure<BuildConfigExtension>(::configureBuildConfigExtension)
}

private fun Project.configureBuildConfigExtension(extension: BuildConfigExtension): BuildConfigExtension =
    extension.apply {

    }
