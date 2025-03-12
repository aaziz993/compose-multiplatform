package gradle.plugins.android

import com.android.build.api.dsl.TestFixtures
import gradle.trySet
import kotlinx.serialization.Serializable

/**
 * DSL object for configuring test fixtures.
 */
@Serializable
internal data class TestFixtures(

    /**
     * Flag to enable test fixtures.
     *
     * Default value is derived from `android.experimental.enableTestFixtures` which is 'false' by
     * default.
     *
     * You can override the default for this for all projects in your build by adding the line
     *     `android.experimental.enableTestFixtures=true`
     * in the gradle.properties file at the root project of your build.
     */
    val enable: Boolean? = null,
    /**
     * Flag to enable Android resource processing in test fixtures.
     *
     * Default value is 'false'.
     */
    val androidResources: Boolean? = null,
) {

    @Suppress("UnstableApiUsage")
    fun applyTo(fixtures: TestFixtures) {
        fixtures::enable trySet enable
        fixtures::androidResources trySet androidResources
    }
}
