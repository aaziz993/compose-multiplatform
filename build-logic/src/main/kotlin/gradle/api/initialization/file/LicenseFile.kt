package gradle.api.initialization.file

import gradle.api.initialization.settingsScript
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.gradle.api.initialization.Settings

@Serializable
@SerialName("LicenseFile")
public data class LicenseFile(
    val source: String? = null,
    override val into: String = "LICENSE",
    override val resolution: FileResolution = FileResolution.NEWER,
    val year: String? = null,
    val yearPlaceholder: String = "[yyyy]",
    val owner: String? = null,
    val ownerPlaceholder: String = "[name of copyright owner]"
) : ProjectFile() {

    @Transient
    override val from: MutableList<String> = mutableListOf()

    @Transient
    override val replace: MutableMap<String, String> = mutableMapOf()

    context(settings: Settings)
    override fun sync() {
        from.add(source ?: settings.settingsScript.remote.url!!)
        replace[yearPlaceholder] = year ?: settings.settingsScript.year
        replace[ownerPlaceholder] = owner ?: settings.settingsScript.developer.name!!

        super.sync()
    }
}
