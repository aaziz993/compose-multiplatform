package gradle.plugins.develocity

import com.gradle.develocity.agent.gradle.test.DevelocityTestConfiguration
import com.gradle.develocity.agent.gradle.test.TestRetryConfiguration
import gradle.api.project.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.develocity

internal class DevelocityPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        with(target) {
            // Apply develocity properties.
            projectProperties.develocity?.applyTo()

            pluginManager.withPlugin("com.gradle.develocity") {
                buildCache {
                    remote(target.develocity.buildCache)
                }
            }
        }
    }

    companion object {

        // Docs: https://docs.gradle.com/develocity/gradle-plugin/current/#test_retry
        context(Test)
        fun testRetry(configure: TestRetryConfiguration.() -> Unit) {
            (extensions.findByName("develocity") as DevelocityTestConfiguration?)?.testRetry(configure)
        }
    }
}
