package gradle.plugins.android

import com.android.build.api.dsl.Split
import gradle.api.trySet

/**
 *  Base data representing how an APK should be split for a given dimension (density, abi).
 */
internal interface Split {

    /** Whether to split in this dimension. */
    val isEnable: Boolean?

    /** Includes some values */
    val includes: List<String>?

    /** Excludes some values */
    val excludes: List<String>?

    /**
     * Resets the list of included split configuration.
     *
     * Use this before calling include, in order to manually configure the list of configuration
     * to split on, rather than excluding from the default list.
     */
    val reset: Boolean?

    fun applyTo(split: Split) {
        split::isEnable trySet isEnable

        includes?.let { includes ->
            split.include(* includes.toTypedArray())
        }

        excludes?.let { excludes ->
            split.exclude(* excludes.toTypedArray())
        }

        reset?.takeIf { it }?.run { split.reset() }
    }
}
