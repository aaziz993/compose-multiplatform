package gradle.plugins.kotlin.targets.nat.apple

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.extraProperties

public class ApplePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // ios Compose uses UiKit, so we need to explicitly enable it, since it is experimental.
            extraProperties["org.jetbrains.compose.experimental.uikit.enabled"] = "true"
            extraProperties.set("generateBuildableXcodeproj.skipKotlinFrameworkDependencies", "true")
        }
    }
}
