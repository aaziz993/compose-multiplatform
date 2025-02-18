package plugin.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.file.Directory
import org.gradle.api.file.FileCollection

@Serializable
internal sealed class Dependency {

    abstract val notation: String

    @Serializable
    @SerialName("file")
    internal data class File(override val notation: String) : Dependency() {

        override fun toDependencyNotation(directory: Directory) = directory.files(notation)
    }

    @Serializable
    @SerialName("maven")
    internal class Maven(override val notation: String) : Dependency() {

        override fun toDependencyNotation(directory: Directory) = notation
    }

    @Serializable
    @SerialName("catalog")
    internal class Catalog(override val notation: String) : Dependency() {

        override fun toDependencyNotation(directory: Directory) = NotImplementedError()
    }

    abstract fun toDependencyNotation(directory: Directory): Any
}
