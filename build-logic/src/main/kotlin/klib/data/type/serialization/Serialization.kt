package klib.data.type.serialization

import klib.data.type.cast
import klib.data.type.collections.deepGetOrNull
import klib.data.type.collections.deepMap
import klib.data.type.collections.list.asList
import klib.data.type.collections.list.drop
import klib.data.type.collections.minusKeys
import klib.data.type.collections.substitute
import klib.data.type.collections.toNewMutableCollection
import klib.data.type.primitives.string.tokenization.substitution.SubstituteOption
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import kotlin.collections.List
import kotlin.collections.MutableMap
import kotlin.collections.contains
import kotlin.collections.first
import kotlin.collections.fold
import kotlin.collections.map
import kotlin.collections.mutableMapOf
import kotlin.collections.orEmpty
import kotlin.collections.set
import kotlin.collections.toTypedArray

public const val IMPORTS_KEY: String = "imports"

@Suppress("UNCHECKED_CAST")
public inline fun <reified T : Any> decodeFile(
    file: String,
    crossinline imports: (file: String, decodedFile: T) -> List<String>? = { _, decodedFile ->
        decodedFile.deepGetOrNull(IMPORTS_KEY).second?.cast()
    },
    crossinline decoder: (file: String) -> T,
    crossinline merger: (decodedFile: T, decodedImports: List<T>) -> T = { decodedFile, decodedImports ->
        val substitutedFile = decodedFile.minusKeys(IMPORTS_KEY).substitute(
            SubstituteOption.INTERPOLATE_BRACED,
            SubstituteOption.DEEP_INTERPOLATION,
            SubstituteOption.EVALUATE,
        )

        if (decodedImports.isEmpty()) substitutedFile
        else {
            val mergedImports = decodedImports.first().deepPlus(*decodedImports.drop().toTypedArray())

            substitutedFile.substitute(
                getter = { path -> mergedImports.deepGetOrNull(*path.toTypedArray()).second },
            ).deepMap(mergedImports)
        }
    },
): T = DeepRecursiveFunction<DecodeFileArgs<T>, T> { (file, mergedFiles) ->
    mergedFiles[file] = null

    val decodedFile = decoder(file)

    merger(
        decodedFile,
        imports(file, decodedFile).orEmpty().map { importFile ->
            if (importFile in mergedFiles) mergedFiles[importFile] ?: error("Cyclic import: $file -> $importFile")
            else callRecursive(DecodeFileArgs(importFile, mergedFiles))
        },
    ).also { mergedFile -> mergedFiles[file] = mergedFile }
}(DecodeFileArgs(file, mutableMapOf()))

@PublishedApi
internal data class DecodeFileArgs<T>(
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

