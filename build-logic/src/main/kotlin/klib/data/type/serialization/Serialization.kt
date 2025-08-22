package klib.data.type.serialization

import klib.data.type.collections.deepGetOrNull
import klib.data.type.collections.deepMap
import klib.data.type.collections.list.asList
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

@Suppress("UNCHECKED_CAST")
public fun <T : Any> decodeFile(
    file: String,
    importToFile: (file: String, import: String) -> String = { _, import -> import },
    importsPath: List<Any?> = listOf("imports"),
    getter: Any.(path: List<Any?>) -> List<String>? = { path -> deepGetOrNull(*path.toTypedArray()) as List<String>? },
    decoder: (file: String) -> T,
    merger: T.(mergedImports: List<T>) -> T,
): T = DeepRecursiveFunction<DecodeFileArgs<T>, T> { (
                                                         file,
                                                         importToFile,
                                                         importsPath,
                                                         getter,
                                                         decoder,
                                                         merger,
                                                         mergedFiles
                                                     ) ->
    mergedFiles[file] = null

    val decodedFile = decoder(file)

    decodedFile.merger(
        decodedFile.getter(importsPath)
            .orEmpty()
            .map { import -> importToFile(file, import) }
            .map { importFile ->
                if (importFile in mergedFiles) mergedFiles[importFile] ?: error("Cyclic import: $file -> $importFile")
                else callRecursive(
                    DecodeFileArgs(
                        importFile,
                        importToFile,
                        importsPath,
                        getter,
                        decoder,
                        merger,
                        mergedFiles,
                    )
                )
            })
        .also { merged ->
            mergedFiles[file] = merged
        }
}(
    DecodeFileArgs(
        file,
        importToFile,
        importsPath,
        getter,
        decoder,
        merger,
        mutableMapOf()
    )
)

private data class DecodeFileArgs<T>(
    val file: String,
    val importToFile: (file: String, import: String) -> String,
    val importsPath: List<Any?>,
    val getter: Any.(path: List<Any?>) -> List<String>?,
    val decoder: (file: String) -> T,
    val merger: T.(others: List<T>) -> T,
    val mergedFiles: MutableMap<String, T?>,
)

public inline fun <reified T : Any> T.plus(
    vararg values: T,
    serializersModule: SerializersModule = EmptySerializersModule()
): T = (this::class.serializer() as KSerializer<T>).plus(*values, serializersModule = serializersModule)

public inline fun <reified T : Any> T.deepPlus(
    vararg values: T,
    serializersModule: SerializersModule = EmptySerializersModule()
): T = (values.first()::class.serializer() as KSerializer<T>)
    .deepPlus(this, *values, serializersModule = serializersModule)

public inline fun <T : Any> T.deepCopy(
    serializersModule: SerializersModule = EmptySerializersModule(),
    transform: Any.() -> Any = { this }
): T = (this::class.serializer() as KSerializer<T>).deepCopy(this, serializersModule, transform)

