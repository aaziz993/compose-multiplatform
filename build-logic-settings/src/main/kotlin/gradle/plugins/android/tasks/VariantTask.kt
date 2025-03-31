package gradle.plugins.android.tasks

import com.android.build.gradle.internal.tasks.VariantTask
import gradle.reflect.trySet

/** Represents a variant-specific task. */
internal interface VariantTask<T : VariantTask> {

    /** the name of the variant */
    val variantName: String?

    fun applyTo(receiver: VariantTask) {
        receiver::variantName trySet variantName
    }
}
