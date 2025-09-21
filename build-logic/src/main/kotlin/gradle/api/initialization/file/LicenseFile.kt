package gradle.api.initialization.file

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.gradle.api.initialization.Settings

@Serializable
public data class LicenseFile(
    val source: String,
    override val resolution: FileResolution = FileResolution.NEWER,
    val year: String? = null,
    val yearPlaceholder: String = "[yyyy]",
    val owner: String? = null,
    val ownerPlaceholder: String = "[name of copyright owner]"
) : ProjectFile() {

    @Transient
    override val from: MutableList<String> = mutableListOf()

    @Transient
    override val into: String = "LICENSE"

    @Transient
    override val replace: MutableMap<String, String> = mutableMapOf()

    context(settings: Settings)
    override fun sync() {
        from.add(source)
        year?.let { year -> replace[yearPlaceholder] = year }
        owner?.let { owner -> replace[ownerPlaceholder] = owner }

        super.sync()
    }
}
