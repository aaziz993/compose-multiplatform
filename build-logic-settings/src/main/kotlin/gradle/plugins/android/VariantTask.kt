package gradle.plugins.android

import com.android.build.gradle.internal.tasks.VariantTask
import gradle.api.trySet

/** Represents a variant-specific task. */
internal interface VariantTask {

    /** the name of the variant */
    val variantName: String?

    fun applyTo(recipient: VariantTask) {
        task::variantName trySet variantName
    }
}
