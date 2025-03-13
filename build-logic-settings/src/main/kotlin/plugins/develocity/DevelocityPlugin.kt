package plugins.develocity

import com.gradle.develocity.agent.gradle.test.DevelocityTestConfiguration
import com.gradle.develocity.agent.gradle.test.TestRetryConfiguration
import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.getByName
import plugins.develocity.model.DevelocitySettings

internal class DevelocityPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        with(target) {
            projectProperties.plugins.develocity.takeIf(DevelocitySettings::enabled)?.let { develocity ->
                // Gives the data to speed up your build, improve build reliability and accelerate build debugging.
                plugins.apply(settings.libs.plugins.plugin("develocity").id)

                // Enhances published build scans by adding a set of tags, links and custom values that have proven to be useful for many projects building with Develocity.
                plugins.apply(settings.libs.plugins.plugin("develocityCommonCustomUserData").id)

                develocity.applyTo()
            }
        }
    }


    companion object {
        // Docs: https://docs.gradle.com/develocity/gradle-plugin/current/#test_retry
        context(Test)
        fun testRetry(configure: TestRetryConfiguration.() -> Unit) {
            extensions.getByName<DevelocityTestConfiguration>("develocity").testRetry(configure)
        }
    }
}
