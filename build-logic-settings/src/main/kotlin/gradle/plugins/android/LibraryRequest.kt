package gradle.plugins.android

import kotlinx.serialization.Serializable

@Serializable
internal data class LibraryRequest(
    val name: String,
    val required: Boolean = false
) {

    fun toLibraryRequest() = com.android.builder.core.LibraryRequest(
        name,
        required,
    )
}
