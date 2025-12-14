package klib.data.cache.kache.file.journal

import klib.data.cache.kache.KacheStrategy
import kotlinx.io.EOFException
import kotlinx.io.Source
import kotlinx.io.readString
import kotlinx.io.readUByte
import kotlinx.io.readUShort

internal class JournalReader(
    private val source: Source,
    private val cacheVersion: Int = 1,
    private val strategy: KacheStrategy = KacheStrategy.LRU,
) : AutoCloseable {

    internal fun validateHeader() {
        val magic = try {
            source.readString(JOURNAL_MAGIC.length.toLong())
        }
        catch (_: EOFException) {
            throw JournalInvalidHeaderException("File size is less than journal magic code size")
        }

        if (magic != JOURNAL_MAGIC) throw JournalInvalidHeaderException("Journal magic ($magic) doesn't match")

        val version = source.readByte()
        if (version != JOURNAL_VERSION) throw JournalInvalidHeaderException("Journal version ($version) doesn't match")

        val existingCacheVersion = source.readInt()
        if (cacheVersion != existingCacheVersion) {
            throw JournalInvalidHeaderException(
                "Existing cache version ($existingCacheVersion) doesn't match current version ($cacheVersion)",
            )
        }

        val existingStrategy = source.readByte()
        if (strategy.ordinal != existingStrategy.toInt()) {
            throw JournalInvalidHeaderException(
                "Existing strategy ($existingStrategy) doesn't match current strategy (${strategy.ordinal})",
            )
        }
    }

    internal fun readEntry(): JournalEntry? {
        val opcodeId = try {
            source.readByte()
        }
        catch (_: EOFException) {
            // Fine, we've reached the end of the file
            return null
        }

        val key = source.readUShortLengthUtf8()

        val transformedKey =
            if (opcodeId == JournalEntry.Companion.CLEAN_WITH_TRANSFORMED_KEY) source.readUByteLengthUtf8()
            else null

        return JournalEntry(opcodeId, key, transformedKey)
    }

    override fun close(): Unit = source.close()

    private fun Source.readUByteLengthUtf8(): String {
        val length = readUByte().toLong()
        return readString(length)
    }

    private fun Source.readUShortLengthUtf8(): String {
        val length = readUShort().toLong()
        return readString(length)
    }
}
