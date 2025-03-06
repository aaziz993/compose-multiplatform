package gradle.model.android

import kotlinx.serialization.Serializable

/**
 * Options for data binding
 */
@Serializable
internal data class DataBindingOptions(
    /**
     * The version of data binding to use.
     */
    val version: String? = null,
    /** Whether to add the default data binding adapters. */
    val addDefaultAdapters: Boolean? = null,
    /**
     * Whether we want tests to be able to use data binding as well.
     * <p>
     * Data Binding classes generated from the application can always be accessed in the test code
     * but test itself cannot introduce new Data Binding layouts, bindables etc unless this flag
     * is turned on.
     * <p>
     * This settings help with an issue in older devices where class verifier throws an exception
     * when the application class is overwritten by the test class. It also makes it easier to run
     * proguarded tests.
     */
    val isEnabledForTests: Boolean? = null
)
