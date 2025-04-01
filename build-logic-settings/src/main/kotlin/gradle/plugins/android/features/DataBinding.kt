package gradle.plugins.android.features

import com.android.build.api.dsl.DataBinding
import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable

/**
 * DSL object for configuring databinding options.
 */
@Serializable
internal data class DataBinding(
    /** The version of data binding to use. */
    val version: String? = null,
    /** Whether to add the default data binding adapters. */
    val addDefaultAdapters: Boolean? = null,
    /**
     * Whether to add the data binding KTX features.
     * A null value means that the user hasn't specified any value in the DSL.
     * The default value can be tweaked globally using the
     * `android.defaults.databinding.addKtx` gradle property.
     */
    val addKtx: Boolean? = null,
    /** Whether to run data binding code generation for test projects. */
    val enableForTests: Boolean? = null,
    /** Whether to enable data binding. */
    val enable: Boolean? = null,
) {

    fun applyTo(receiver: DataBinding) {
        receiver::version trySet version
        receiver::addDefaultAdapters trySet addDefaultAdapters
        receiver::addKtx trySet addKtx
        receiver::enableForTests trySet enableForTests
        receiver::enable trySet enable
    }
}
