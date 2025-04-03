package gradle.api.catalog

import gradle.accessors.settings
import klib.data.type.primitive.addPrefix
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.initialization.Settings

@KeepGeneratedSerializer
@Serializable(with = NotationTransformingSerializer::class)
internal data class DependencyNotation(
    @SerialName("version")
    override val _version: @Serializable(with = VersionContentPolymorphicSerializer::class) Any? = null,
    @SerialName("group")
    private val _group: String? = null,
    @SerialName("name")
    private val _name: String? = null,
    @SerialName("module")
    private val _module: String? = null,
    @SerialName("notation")
    private val _notation: String? = null,
) : VersionCatalogNotation() {

    @Transient
    override lateinit var versionCatalog: VersionCatalog

    @Transient
    val group = _group ?: _module!!.substringBefore(":")

    @Transient
    val name = _name ?: _module!!.substringAfter(":")

    @Transient
    val module = _module ?: "$_group:$_name"

    context(Settings)
    @Suppress("UnstableApiUsage")
    override val notation: Any
        get() = settings.allLibs.notation(settings.layout.settingsDirectory)

    context(Project)
    override val notation: Any
        get() = project.settings.allLibs.notation(project.layout.projectDirectory)

    private fun Set<VersionCatalog>.notation(directory: Directory) = _notation?.let {
        resolveDependency(
            _notation,
            directory,
        )
    } ?: "$module${version?.addPrefix(":")}"
}

private object NotationTransformingSerializer :
    JsonTransformingSerializer<DependencyNotation>(DependencyNotation.generatedSerializer()) {

    override fun transformDeserialize(element: JsonElement): JsonElement =
        if (element is JsonPrimitive) JsonObject(
            mapOf("notation" to element),
        )
        else element
}
