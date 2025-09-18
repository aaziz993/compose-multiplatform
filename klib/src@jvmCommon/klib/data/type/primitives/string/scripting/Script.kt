package klib.data.type.primitives.string.scripting

import com.charleskorn.kaml.Yaml
import com.github.ajalt.colormath.model.Ansi16
import java.io.File
import java.lang.reflect.Modifier
import klib.data.cache.Cache
import klib.data.cache.emptyCache
import klib.data.type.ansi.Attribute
import klib.data.type.ansi.ansiSpan
import klib.data.type.ansi.buildStringAnsi
import klib.data.type.collections.deepGet
import klib.data.type.collections.deepGetOrNull
import klib.data.type.collections.deepMap
import klib.data.type.collections.deepPlus
import klib.data.type.collections.deepRunOnPenultimate
import klib.data.type.collections.deepSubstitute
import klib.data.type.collections.flatten
import klib.data.type.collections.getOrPut
import klib.data.type.collections.list.asList
import klib.data.type.collections.list.dropLast
import klib.data.type.collections.map.asMapOrNull
import klib.data.type.collections.map.asStringNullableMap
import klib.data.type.collections.set
import klib.data.type.collections.toNewMutableCollection
import klib.data.type.collections.toTreeString
import klib.data.type.primitives.string.addSuffix
import klib.data.type.primitives.string.addSuffixIfNotEmpty
import klib.data.type.primitives.string.highlight
import klib.data.type.reflection.declaredMemberExtensionFunction
import klib.data.type.reflection.declaredMemberExtensionFunctions
import klib.data.type.reflection.declaredMemberExtensionProperty
import klib.data.type.reflection.memberFunction
import klib.data.type.reflection.memberFunctions
import klib.data.type.reflection.memberProperty
import klib.data.type.reflection.packageExtensions
import klib.data.type.reflection.toGetterName
import klib.data.type.reflection.toSetterName
import klib.data.type.serialization.IMPORTS_KEY
import klib.data.type.serialization.coders.tree.deserialize
import klib.data.type.serialization.decodeFile
import klib.data.type.serialization.json.decodeAnyFromString
import klib.data.type.serialization.properties.Properties
import klib.data.type.serialization.serializers.any.SerializableAny
import klib.data.type.serialization.yaml.decodeAnyFromString
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.isSubclassOf
import kotlin.script.experimental.api.ResultValue
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.ScriptEvaluationConfiguration
import kotlin.script.experimental.api.SourceCode
import kotlin.script.experimental.api.valueOrThrow
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

public const val SCRIPT_KEY: String = "script"

public val EXPLICIT_OPERATION_RECEIVERS: Set<KClass<out Any>> = setOf(
    Array::class,
    MutableCollection::class,
    MutableMap::class,
)

@Serializable
public abstract class Script {

    public abstract val config: ScriptConfig

    public abstract val script: List<SerializableAny>

    public abstract val fileTree: Map<String, List<String>>

    @Transient
    public var cache: Cache<String, String> = emptyCache()

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
        if (this is Throwable) throw this else Unit
    }

    override fun toString(): String = buildStringAnsi {
        attribute(
            fileTree.entries.first().key.toTreeString(
                children = {
                    fileTree[this].orEmpty()
                },
            ) { value, visited ->
                if (visited) "${
                    "File:".ansiSpan {
                        attribute(Attribute.INTENSITY_BOLD)
                        attribute(Ansi16(33))
                    }
                } $value ↑"
                else "${
                    "File:".ansiSpan {
                        attribute(Attribute.INTENSITY_BOLD)
                        attribute(Ansi16(32))
                    }
                } $value"
            },
        )
        attribute('\n')
        attribute(
            config.imports.sorted().joinToString("\n", postfix = "\n") { import -> "import $import" }
                .addSuffixIfNotEmpty("\n")
                .addSuffix(compiled).highlight(),
        )
    }

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
                        error(
                            "Unresolved reference '${
                                dropLast()
                                    .joinToString(".", transform = Pair<*, String>::second)
                                    .addSuffixIfNotEmpty("->")
                            }${last().second}' on '${last().first}' with imports ${config.imports}",
                        )

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
                        ?.let { kClass -> implicitOperation(kClass, value) }
                }
            } ?: value
    }

    public companion object {

        public inline operator fun <reified T : Script> invoke(
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
            cache: Cache<String, String> = emptyCache(),
            explicitOperationReceivers: Set<KClass<*>> = emptySet(),
            noinline implicitOperation: (valueClass: KClass<*>, value: Any?) -> String? = { _, _ -> null },
            config: ScriptConfig.() -> Unit = { },
        ): T {
            val fileTree = mutableMapOf<String, List<String>>()

            return T::class.serializer().deserialize(
                decodeFile(
                    file,
                    { file, decodedFile ->
                        decodedFile.deepGetOrNull(IMPORTS_KEY).second?.asList<String>()?.map { import ->
                            importToFile(file, import)
                        }.also { imports -> fileTree[file] = imports.orEmpty() }
                    },
                    decoder = decoder,
                ) { decodedFile, decodedImports ->
                    val substitutedFile =
                        (decodedFile - listOf(IMPORTS_KEY, SCRIPT_KEY)).deepSubstitute(unescapeDollars = false)

                    val decodedFileScript: List<Any?> = decodedFile[SCRIPT_KEY]?.let { script ->
                        script.asMapOrNull?.map { (key, value) -> mapOf(key to value) } ?: script.asList
                    } ?: emptyList()

                    if (decodedImports.isEmpty()) substitutedFile + (SCRIPT_KEY to decodedFileScript)
                    else {
                        val mergedImports = decodedImports
                            .map { decodedImport -> decodedImport - SCRIPT_KEY }
                            .first()
                            .deepPlus(
                                *decodedImports.toTypedArray(),
                                destinationGetter = { source ->
                                    last().first.getOrPut(last().second, source::toNewMutableCollection)
                                },
                            )

                        val mergedImportScripts = decodedImports
                            .flatMap { decodedImport -> decodedImport[SCRIPT_KEY]!!.asList }

                        substitutedFile.deepSubstitute(
                            getter = { path -> mergedImports.deepGet(*path.toTypedArray()).second },
                        ).deepMap(
                            mergedImports,
                            destinationGetter = { source ->
                                last().first.getOrPut(last().second, source::toNewMutableCollection)
                            },
                        ) + (SCRIPT_KEY to (mergedImportScripts + decodedFileScript))
                    }
                }.apply {
                    this["fileTree"] = fileTree
                },
            ).apply {
                this.cache = cache
                this.explicitOperationReceivers = EXPLICIT_OPERATION_RECEIVERS + explicitOperationReceivers
                this.implicitOperation = implicitOperation
                this.config.config()
            }
        }
    }
}

/**
 * Consumes only public implicit receivers
 */
public operator fun SourceCode.invoke(
    compilationConfiguration: ScriptCompilationConfiguration,
    evaluationConfiguration: ScriptEvaluationConfiguration?,
): Any? = BasicJvmScriptingHost()
    .eval(this, compilationConfiguration, evaluationConfiguration)
    .valueOrThrow()
    .returnValue.let { returnValue ->
        when (returnValue) {
            is ResultValue.Unit -> Unit
            is ResultValue.Value -> returnValue.value
            is ResultValue.Error -> returnValue.error
            ResultValue.NotEvaluated -> returnValue.scriptInstance
        }
    }

public operator fun SourceCode.invoke(config: ScriptConfig): Any? =
    this(
        config.toScriptCompilationConfiguration(),
        config.toScriptEvaluationConfiguration(),
    )

public operator fun String.invoke(
    compilationConfiguration: ScriptCompilationConfiguration,
    evaluationConfiguration: ScriptEvaluationConfiguration?,
): Any? = toScriptSource()(compilationConfiguration, evaluationConfiguration)

public operator fun String.invoke(config: ScriptConfig): Any? =
    this(
        config.toScriptCompilationConfiguration(),
        config.toScriptEvaluationConfiguration(),
    )
