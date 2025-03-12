package gradle.plugins.android

import com.android.build.api.dsl.Optimization
import kotlinx.serialization.Serializable

/**
 *  DSL object for configurations aimed for optimizing build process(e.g. speed, correctness). This
 *  DSL object is applicable to buildTypes and productFlavors.
 */
@Serializable
internal data class Optimization(
    /**
     * Configure keep rules inherited from external library dependencies
     */
    val keepRules: KeepRules? = null,
    /**
     * Configure baseline profile properties
     */
    val baselineProfile: BaselineProfile? = null
) {

    @Suppress("UnstableApiUsage")
    fun applyTo(optimization: Optimization) {
        keepRules?.let { keepRules ->
            optimization.keepRules(keepRules::applyTo)
        }

        baselineProfile?.let { baselineProfile ->
            optimization.baselineProfile(baselineProfile::applyTo)
        }
    }
}
