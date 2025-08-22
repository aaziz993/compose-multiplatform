package gradle.api.initialization.dsl

import gradle.api.artifacts.VersionConstraint
import klib.data.type.collections.list.asList
import klib.data.type.collections.map.asMap
import klib.data.type.serialization.serializers.transform.TransformingSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@KeepGeneratedSerializer
@Serializable(with = VersionCatalogTransformingSerializer::class)
public data class VersionCatalog(
    private val versions: Map<String, VersionConstraint>,
    private val libraries: Map<String, Library>,
    private val plugins: Map<String, Plugin>,
    private val bundles: Map<String, List<Library>>
) {

    public fun versions(alias: String): VersionConstraint? = versions[alias]

    public fun libraries(alias: String): Library = libraries[alias]!!

    public fun plugins(alias: String): Plugin = plugins[alias]!!

    public fun bundles(alias: String): List<Library> = bundles[alias]!!

    public operator fun invoke(alias: String): String = libraries[alias]!!.toString()
}

private object VersionCatalogTransformingSerializer :
    TransformingSerializer<VersionCatalog>(VersionCatalog.generatedSerializer()) {

    @Suppress("UNCHECKED_CAST")
    override fun transformDeserialize(value: Any?): Any? {
        value as Map<String, Any>

        val versions = value["versions"]!!.asMap

        val libraries = value["libraries"]!!.asMap.toAliasMap().resolveVersions(versions).mapValues { (_, value) ->
            if (value.containsKey("module"))
                (value["module"] as String).split(":").let { (group, name) ->
                    value - "module" + mapOf(
                        "group" to group,
                        "name" to name,
                    )
                }
            else value
        }

        return value + mapOf(
            "versions" to versions.toAliasMap(),
            "libraries" to libraries,
            "bundles" to value["bundles"]!!.asMap.toAliasMap().mapValues { (_, references) ->
                references.asList<String>()
                    .map(String::asVersionCatalogAlias)
                    .map(libraries::get)
            },
            "plugins" to value["plugins"]!!.asMap.toAliasMap().resolveVersions(versions),
        )
    }

    private fun Map<String, Any>.toAliasMap() = mapKeys { (key, _) -> key.asVersionCatalogAlias }

    @Suppress("UNCHECKED_CAST")
    private fun Map<String, Any>.resolveVersions(versions: Map<String, Any>) =
        mapValues { (_, value) ->
            value.asMap.let { map ->
                when (val version = map["version"]) {
                    null, is String -> map
                    else -> map + ("version" to versions[version.asMap["ref"] as String])
                }
            }
        }
}

private val String.asVersionCatalogAlias
    get() = replace("-", ".")
