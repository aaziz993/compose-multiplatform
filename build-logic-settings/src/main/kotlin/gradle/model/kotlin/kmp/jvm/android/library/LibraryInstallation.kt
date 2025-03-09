package gradle.model.kotlin.kmp.jvm.android.library

import gradle.model.kotlin.kmp.jvm.android.Installation
import kotlinx.serialization.Serializable

/**
 * DSL object for configuring installation options for Library plugins.
 *
 * This is accessed via [LibraryExtension.installation]
 */
@Serializable
internal data class LibraryInstallation(
    override var timeOutInMs: Int? = null,
    override val installOptions: List<String>? = null,
) : Installation
