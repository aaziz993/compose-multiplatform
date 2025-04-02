package gradle.plugins.android

import com.android.build.api.dsl.Optimization
import gradle.reflection.tryApply
import klib.data.type.reflection.tryApply
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
    fun applyTo(receiver: Optimization) {
        receiver::keepRules tryApply keepRules?.let { keepRules -> keepRules::applyTo }
        receiver::baselineProfile tryApply baselineProfile?.let { baselineProfile -> baselineProfile::applyTo }
    }
}
