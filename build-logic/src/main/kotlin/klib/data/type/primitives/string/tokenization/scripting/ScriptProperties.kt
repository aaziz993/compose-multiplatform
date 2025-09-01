package klib.data.type.primitives.string.tokenization.scripting

import com.charleskorn.kaml.Yaml
import klib.data.cache.Cache
import klib.data.cache.NoCache
import klib.data.type.collections.*
import klib.data.type.collections.deepGetOrNull
import klib.data.type.collections.list.asList
import klib.data.type.collections.list.asMutableList
import klib.data.type.collections.map.asMapOrNull
import klib.data.type.collections.map.asStringNullableMap
import klib.data.type.collections.map.asNullableMutableMap
import klib.data.type.primitives.string.tokenization.substitution.SubstituteOption
import klib.data.type.reflection.declaredMemberExtensionFunction
import klib.data.type.reflection.declaredMemberExtensionFunctions
import klib.data.type.reflection.declaredMemberExtensionProperty
import klib.data.type.reflection.memberFunction
import klib.data.type.reflection.memberFunctions
import klib.data.type.reflection.memberProperty
import klib.data.type.reflection.packageExtensions
import klib.data.type.reflection.toGetterName
import klib.data.type.reflection.toSetterName
import klib.data.type.serialization.coders.tree.deserialize
import klib.data.type.serialization.decodeFile
import klib.data.type.serialization.json.decodeAnyFromString
import klib.data.type.serialization.properties.Properties
import klib.data.type.serialization.serializers.any.SerializableAny
import klib.data.type.serialization.yaml.decodeAnyFromString
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.io.File
import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.isSubclassOf

@Serializable
public abstract class ScriptProperties {
    public abstract val config: ScriptConfig

    public abstract val script: List<SerializableAny>

    @Transient
    public var cache: Cache<String, String> = NoCache()

    @Transient
    public var explicitOperationReceivers: Set<KClass<*>> = emptySet()

    @Transient
    public var implicitOperation: (valueClass: KClass<*>, value: Any?) -> String? = { _, _ -> null }

    public val compiled: String by lazy {
        val cacheKeys = mutableSetOf<String>()

        script.flatten().joinToString("\n") { (path, value) ->
            val referencePath = path.filter { (source, _) -> source !is List<*> }.map { (_, key) -> key!!.toString() }
            val reference = referencePath.joinToString(".")
            val cacheKey = "$reference:$value"

            cacheKeys.add(cacheKey)

            cache[cacheKey] ?: "$reference${tryAssign(referencePath.toTypedArray(), value)}"
                .also { operation -> cache[cacheKey] = operation }
        }.also {
            (cache.asMap().keys - cacheKeys).forEach(cache::remove)
        }
    }

    public operator fun invoke(): Unit = compiled(config).run {
        if (this is Throwable) throw this
    }

    override fun toString(): String = compiled

    private fun tryAssign(path: Array<String>, value: Any?): Any? {
        val packages = config.imports.filter { import -> import.endsWith(".*") }.map { import ->
            import.removeSuffix(".*")
        }.toSet()

        return config.compilationImplicitReceivers
            .firstNotNullOfOrNull { implicitReceiver ->
                implicitReceiver.deepRunOnPenultimate(
                    *path,
                    getter = {
                        val kClass = last().first as KClass<*>

                        val memberName = last().second
                        val getterName = memberName.toGetterName()

                        (kClass.memberProperty(memberName)
                            ?: kClass.declaredMemberExtensionProperty(memberName)
                            ?: kClass.memberFunction(getterName)
                            ?: kClass.declaredMemberExtensionFunction(getterName))
                            ?.takeIf { property -> property.visibility == KVisibility.PUBLIC }
                            ?.returnType?.classifier as KClass<*>?
                            ?: kClass.packageExtensions(getterName, packages).singleOrNull { method ->
                                Modifier.isPublic(method.modifiers)
                            }?.returnType?.kotlin
                    },
                ) {
                    if (size < path.size)
                        error("Unresolved reference '${path.joinToString(".")}'")

                    val receiver = last().first

                    if (receiver !is KClass<*> || explicitOperationReceivers.any(receiver::isSubclassOf))
                        return@deepRunOnPenultimate null

                    val memberName = last().second

                    val getterName = memberName.toGetterName()
                    val setterName = memberName.toSetterName()

                    (receiver.memberProperty(memberName) ?: receiver.declaredMemberExtensionProperty(memberName))
                        ?.takeIf { property -> property.visibility == KVisibility.PUBLIC }
                        ?.let { property ->
                            return@deepRunOnPenultimate if (property is KMutableProperty<*>) " = $value"
                            else implicitOperation(property.returnType.classifier as KClass<*>, value)
                        }

                    if (receiver.memberFunctions(setterName).any { setter ->
                            setter.visibility == KVisibility.PUBLIC
                        } ||
                        receiver.declaredMemberExtensionFunctions(setterName).any { setter ->
                            setter.visibility == KVisibility.PUBLIC
                        } ||
                        receiver.packageExtensions(setterName, packages).any { method ->
                            Modifier.isPublic(method.modifiers)
                        }) {
                        return@deepRunOnPenultimate " = $value"
                    }

                    ((receiver.memberFunction(getterName)
                        ?: receiver.declaredMemberExtensionFunction(getterName))?.takeIf { property ->
                        property.visibility == KVisibility.PUBLIC
                    }?.returnType?.classifier as KClass<*>?
                        ?: receiver.packageExtensions(getterName, packages).singleOrNull { property ->
                            Modifier.isPublic(property.modifiers)
                        }?.returnType?.kotlin)
                        ?.let { kClass ->
                            implicitOperation(kClass, value)
                        }
                }
            } ?: value
    }

    public companion object {
        @PublishedApi
        internal const val IMPORTS_KEY: String = "imports"

        @PublishedApi
        internal const val SCRIPT_KEY: String = "script"

        @PublishedApi
        internal val EXPLICIT_OPERATION_RECEIVERS: Set<KClass<out Any>> = setOf(
            Array::class,
            MutableCollection::class,
            MutableMap::class,
        )

        public inline operator fun <reified T : ScriptProperties> invoke(
            file: String,
            noinline importToFile: (file: String, import: String) -> String = { file, import ->
                File(file).parentFile.resolve(import).path
            },
            noinline decoder: (file: String) -> Map<String, Any?> = { file ->
                val file = File(file)
                val text = file.readText()
                when (file.extension) {
                    "yaml" -> Yaml.default.decodeAnyFromString(text)
                    "json" -> Json.decodeAnyFromString(text)
                    "properties" -> Properties.decodeAnyFromString(text)

                    else -> error("Unsupported file extension ${file.extension}")
                }!!.asStringNullableMap
            },
            cache: Cache<String, String> = NoCache(),
            explicitOperationReceivers: Set<KClass<*>> = emptySet(),
            noinline implicitOperation: (valueClass: KClass<*>, value: Any?) -> String? = { _, _ -> null },
            config: ScriptConfig.() -> Unit = { },
        ): T = T::class.serializer().deserialize(
            decodeFile(
                file,
                { file, decodedFile ->
                    decodedFile.deepGetOrNull("imports").second?.asList<String>()?.map { import ->
                        importToFile(file, import)
                    }.orEmpty()
                },
                decoder = decoder,
            ) { decodedFile, decodedImports ->
                val substitutedFile = (decodedFile - IMPORTS_KEY).let { decodedFile ->
                    val script = decodedFile[SCRIPT_KEY]

                    if (script is Map<*, *>)
                        decodedFile + (SCRIPT_KEY to script.map { (key, value) -> mapOf(key to value) })
                    else decodedFile
                }.substitute(SubstituteOption.DEEP_INTERPOLATION)

                val mergedImports = decodedImports.fold(mutableMapOf<String, Any?>()) { mergedImports, decodedImport ->
                    decodedImport.deepMap(mergedImports)
                }

                substitutedFile.substitute { path ->
                    mergedImports.deepGetOrNull(*path.toTypedArray()).second
                }.deepMap(mergedImports)
            }.also {

                val t = it
                val f = 0
            }).apply {
            this.cache = cache
            this.explicitOperationReceivers = EXPLICIT_OPERATION_RECEIVERS + explicitOperationReceivers
            this.implicitOperation = implicitOperation
            this.config.config()
        }

        @PublishedApi
        internal fun Map<String, Any?>.deepMap(other: MutableMap<String, Any?>): MutableMap<String, Any?> =
            deepMap(
                sourceIteratorOrNull = { value ->
                    if (firstOrNull()?.second == SCRIPT_KEY) null else value.iteratorOrNull()
                },
                destination = other,
                destinationGetter = { source ->
                    last().first.getOrPut(last().second, source::toNewMutableCollection)
                }
            ) { value ->
                if (first().second == SCRIPT_KEY)
                    first().first
                        .asNullableMutableMap.getOrPut(first().second as String) { mutableListOf<Any>() }!!
                        .asMutableList.addAll(value?.asList.orEmpty())
                else last().first.put(last().second, value)
            }
    }
}

