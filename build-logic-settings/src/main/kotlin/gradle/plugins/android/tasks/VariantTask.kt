package gradle.plugins.android.tasks

import com.android.build.gradle.internal.tasks.VariantTask
import gradle.api.trySet

/** Represents a variant-specific task. */
internal interface VariantTask<T : VariantTask> {

    /** the name of the variant */
    val variantName: String?

    fun applyTo(recipient: VariantTask) {
        recipient::variantName trySet variantName
    }
}
