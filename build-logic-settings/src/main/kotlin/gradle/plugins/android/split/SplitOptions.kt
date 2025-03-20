package gradle.plugins.android.split

import com.android.build.gradle.internal.dsl.SplitOptions

/** Base data representing how an FULL_APK should be split for a given dimension (density, abi).  */
internal abstract class SplitOptions<T : SplitOptions> : Split<T>
