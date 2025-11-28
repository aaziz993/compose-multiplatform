package klib.data.type.primitives.string.scripting

import com.charleskorn.kaml.Yaml
import com.github.ajalt.colormath.model.Ansi16
import java.io.File
import java.lang.reflect.Modifier
import klib.data.cache.Cache
import klib.data.cache.SqliteCache
import klib.data.cache.emptyCache
import klib.data.type.collections.deepGet
import klib.data.type.collections.deepGetOrNull
import klib.data.type.collections.deepMap
import klib.data.type.collections.deepPlus
import klib.data.type.collections.deepRunOnPenultimate
import klib.data.type.collections.deepSubstitute
import klib.data.type.collections.flatten
import klib.data.type.collections.getOrPut
import klib.data.type.collections.list.asList
import klib.data.type.collections.map.asMapOrNull
import klib.data.type.collections.map.asStringNullableMap
import klib.data.type.collections.set
import klib.data.type.collections.toNewMutableCollection
import klib.data.type.collections.toTreeString
import klib.data.type.primitives.string.addSuffix
import klib.data.type.primitives.string.addSuffixIfNotEmpty
import klib.data.type.primitives.string.ansi.Attribute
import klib.data.type.primitives.string.ansi.ansiSpan
import klib.data.type.primitives.string.ansi.buildStringAnsi
import klib.data.type.primitives.string.highlight
import klib.data.type.reflection.classifierOrUpperBound
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
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

public const val SCRIPT_KEY: String = "script"

public val DECLARATION_KEYWORDS: Set<String> = setOf(
    "val",
    "var",
    "fun",
    "class",
    "interface",
    "object",
    "enum",
    "annotation",
    "typealias",
    "abstract",
    "data",
    "sealed",
    "open",
    "private",
    "public",
    "internal",
    "inline",
    "tailrec",
    "suspend",
    "operator",
    "infix",
    "const",
    "lateinit",
)

@Serializable
public abstract class Script {

    public abstract val config: ScriptConfig

    public abstract val script: List<SerializableAny>

    public abstract val fileTree: Map<String, List<String>>

    @Transient
    public var cache: Cache<String, String> = emptyCache()

    @Transient
    public var assignOperation: (valueClass: KClass<*>, value: Any?) -> String? = { _, _ -> null }

    public val compiled: String by lazy {
        val cacheKeys = mutableSetOf<String>()

        script.flatten().joinToString("\n") { (path, value) ->
            val referencePath = path.filter { (source, _) -> source !is List<*> }.map { (_, key) -> key!!.toString() }
            val reference = referencePath.joinToString(".")
            val cacheKey = "$reference:$value".also(cacheKeys::add)

            cache[cacheKey] ?: "$reference${
                if (reference in DECLARATION_KEYWORDS) " $value"
                else tryAssign(referencePath.toTypedArray(), value)
            }".also { operation -> cache[cacheKey] = operation }
        }.also {
            (cache.asMap().keys - cacheKeys).forEach(cache::remove)
        }
    }

    public operator fun invoke(transform: (compiled: String) -> String = { it }): Unit =
        transform(compiled)(config).run {
            if (this is Throwable) throw this
        }

    override fun toString(): String = buildStringAnsi {
        attribute(
            fileTree.keys.first().toTreeString(fileTree) { visited ->
                if (visited) "${
                    "File:".ansiSpan {
                        attribute(Attribute.INTENSITY_BOLD)
                        attribute(Ansi16(31))
                    }
                } ${last()} ↑"
                else "${
                    "File:".ansiSpan {
                        attribute(Attribute.INTENSITY_BOLD)
                        attribute(Ansi16(31 + size))
                    }
                } ${last()} "
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
        val packages = config.imports.map { import -> import.substringBeforeLast(".") }.toSet()

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
                            ?.takeIf { member -> member.visibility == KVisibility.PUBLIC }
                            ?.returnType?.classifierOrUpperBound()
                            ?: kClass.packageExtensions(getterName, packages).find { method ->
                                Modifier.isPublic(method.modifiers)
                            }?.returnType?.kotlin
                    },
                ) {
                    if (size < path.size) return@deepRunOnPenultimate null

                    val receiver = last().first as KClass<*>
                    val memberName = last().second

                    // Check for property.
                    (receiver.memberProperty(memberName)
                        ?: receiver.declaredMemberExtensionProperty(memberName))
                        ?.takeIf { property -> property.visibility == KVisibility.PUBLIC }
                        ?.let { property ->
                            if (property is KMutableProperty<*>) return@deepRunOnPenultimate " = $value"
                            else assignOperation(property.returnType.classifierOrUpperBound()!!, value)
                                ?.let { operation ->
                                    return@deepRunOnPenultimate operation
                                }
                        }

                    val getterName = memberName.toGetterName()

                    // Check for property getter function.
                    ((receiver.memberFunction(getterName)
                        ?: receiver.declaredMemberExtensionFunction(getterName))
                        ?.takeIf { member -> member.visibility == KVisibility.PUBLIC }
                        ?.returnType?.classifierOrUpperBound()
                        ?: receiver.packageExtensions(getterName, packages).find { method ->
                            Modifier.isPublic(method.modifiers)
                        }?.returnType?.kotlin)
                        ?.let { kClass ->
                            if (kClass.isSubclassOf(KMutableProperty::class))
                                return@deepRunOnPenultimate " = $value"
                            else assignOperation(kClass, value)?.let { operation ->
                                return@deepRunOnPenultimate operation
                            }
                        }

                    val setterName = memberName.toSetterName()

                    // Check for property setter function.
                    if (receiver.memberFunctions(setterName).any { function ->
                            function.visibility == KVisibility.PUBLIC
                        } || receiver.declaredMemberExtensionFunctions(setterName).any { function ->
                            function.visibility == KVisibility.PUBLIC
                        } || receiver.packageExtensions(setterName, packages).any { method ->
                            Modifier.isPublic(method.modifiers)
                        }) " = $value"
                    else null
                }
            } ?: value
    }

    public companion object {

        public inline operator fun <reified T : Script> invoke(
            file: String,
            noinline importToFile: (file: String, import: String) -> String = { file, import ->
                File(file).parentFile.resolve(import).canonicalPath
            },
            noinline decoder: (file: String) -> Map<String, Any?> = { file ->
                val file = File(file)
                val text = file.readText()
                when (file.extension) {
                    "yaml" -> Yaml.default.decodeAnyFromString(text)
                    "json" -> Json.decodeAnyFromString(text)
                    "properties" -> Properties.decodeAnyFromString(text)

                    else -> throw IllegalArgumentException("Unsupported file extension ${file.extension}")
                }!!.asStringNullableMap
            },
            cache: Cache<String, String> = SqliteCache(
                File(
                    {}::class.java.protectionDomain.codeSource.location.toURI(),
                ).parentFile.resolve("${file.substringAfterLast(File.pathSeparator)}.db"),
                String.serializer(),
                String.serializer(),
            ),
            noinline assignOperation: (valueClass: KClass<*>, value: Any?) -> String? = { _, _ -> null },
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
                this.assignOperation = assignOperation
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
