package klib.data.type.serialization

import klib.data.type.collections.deepGetOrNull
import klib.data.type.collections.list.asList
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

@Suppress("UNCHECKED_CAST")
public fun <T : Any> decodeFile(
    file: String,
    imports: (file: String, decodedFile: T) -> List<String> = { file, decodedFile ->
        decodedFile.deepGetOrNull("imports").second?.asList<String>().orEmpty()
    },
    decoder: (file: String) -> T,
    merger: (decodedFile: T, mergedImports: List<T>) -> T,
): T = DeepRecursiveFunction<DecodeFileArgs<T>, T> { (file, mergedFiles) ->
    mergedFiles[file] = null

    val decodedFile = decoder(file)

    merger(decodedFile, imports(file, decodedFile).map { importFile ->
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

