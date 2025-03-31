package gradle.plugins.android.split

import com.android.build.api.dsl.Split
import gradle.reflect.trySet
import gradle.reflect.trySet

/**
 *  Base data representing how an APK should be split for a given dimension (density, abi).
 */
internal interface Split<T : Split> {

    /** Whether to split in this dimension. */
    val isEnable: Boolean?

    /** Includes some values */
    val includes: Set<String>?

    /** Excludes some values */
    val excludes: Set<String>?

    /**
     * Resets the list of included split configuration.
     *
     * Use this before calling include, in order to manually configure the list of configuration
     * to split on, rather than excluding from the default list.
     */
    val reset: Boolean?

    fun applyTo(receiver: T) {
        receiver::isEnable trySet isEnable
        receiver::include trySet includes
        receiver::exclude trySet excludes
        receiver::reset trySet reset
    }
}
