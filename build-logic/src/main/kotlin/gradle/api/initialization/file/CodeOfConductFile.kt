package gradle.api.initialization.file

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.gradle.api.initialization.Settings

@Serializable
@SerialName("CodeOfConductFile")
public data class CodeOfConductFile(
    val source: String,
    override val resolution: FileResolution = FileResolution.NEWER,
    val email: String? = null,
    val emailPlaceholder: String
) : ProjectFile() {

    @Transient
    override val from: List<String> = listOf(source)

    @Transient
    override val into: String = "CODE_OF_CONDUCT.md"

    @Transient
    override val replace: MutableMap<String, String> = mutableMapOf()

    context(settings: Settings)
    override fun sync() {
        email?.let { email -> replace[emailPlaceholder] = email }

        super.sync()
    }
}
