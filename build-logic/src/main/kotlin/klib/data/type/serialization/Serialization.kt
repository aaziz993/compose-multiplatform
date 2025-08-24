package klib.data.type.serialization

import klib.data.type.cast
import klib.data.type.collections.deepGetOrNull
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

@Suppress("UNCHECKED_CAST")
public fun <T : Any> decodeFile(
    file: String,
    imports: Any.() -> List<String>? = { deepGetOrNull("imports").second?.cast() },
    decoder: (file: String) -> T,
    merger: T.(mergedImports: List<T>) -> T,
): T = DeepRecursiveFunction<DecodeFileArgs<T>, T> { (file, mergedFiles) ->
    mergedFiles[file] = null

    val decodedFile = decoder(file)

    decodedFile.merger(decodedFile.imports().orEmpty().map { importFile ->
        if (importFile in mergedFiles) mergedFiles[importFile] ?: error("Cyclic import: $file -> $importFile")
        else callRecursive(
            DecodeFileArgs(
                importFile,
                mergedFiles,
            )
        )
    }).also { mergedFile -> mergedFiles[file] = mergedFile }
}(DecodeFileArgs(file, mutableMapOf()))

private data class DecodeFileArgs<T>(
    val file: String,
    val mergedFiles: MutableMap<String, T?>,
)

public inline fun <reified T : Any> T.plus(
    vararg values: T,
    serializersModule: SerializersModule = EmptySerializersModule()
): T = serializer<T>().plus(this, *values, serializersModule = serializersModule)

public inline fun <reified T : Any> T.deepPlus(
    vararg values: T,
    serializersModule: SerializersModule = EmptySerializersModule()
): T = serializer<T>().deepPlus(this, *values, serializersModule = serializersModule)

public inline fun <reified T : Any> T.deepCopy(
    serializersModule: SerializersModule = EmptySerializersModule(),
    transform: Any.() -> Any = { this }
): T = serializer<T>().deepCopy(this, serializersModule, transform)

