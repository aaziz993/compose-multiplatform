package klib.data.cache.kache.file.journal

import klib.data.cache.kache.KacheStrategy
import kotlin.collections.iterator
import kotlinx.io.Sink
import kotlinx.io.writeString
import kotlinx.io.writeUByte
import kotlinx.io.writeUShort

internal class JournalWriter(
    private val sink: Sink,
    private val cacheVersion: Int = 1,
    private val strategy: KacheStrategy = KacheStrategy.LRU,
) : AutoCloseable {

    internal fun writeHeader() {
        sink.writeString(JOURNAL_MAGIC)
        sink.writeByte(JOURNAL_VERSION)
        sink.writeInt(cacheVersion)
        sink.writeByte(strategy.ordinal.toByte())
        sink.flush()
    }

    internal fun writeAll(cleanKeys: Map<String, String>, dirtyKeys: Collection<String>) {
        for ((key, transformedKey) in cleanKeys) writeEntry(
            JournalEntry.Companion.CLEAN_WITH_TRANSFORMED_KEY, key, transformedKey,
        )
        for (key in dirtyKeys) writeEntry(JournalEntry.Companion.DIRTY, key)
        sink.flush()
    }

    internal fun writeDirty(key: String) = writeEntryAndFlush(JournalEntry.Companion.DIRTY, key)

    internal fun writeClean(key: String, transformedKey: String? = null) = writeEntryAndFlush(
        if (transformedKey == null) JournalEntry.Companion.CLEAN
        else JournalEntry.Companion.CLEAN_WITH_TRANSFORMED_KEY,
        key,
        transformedKey,
    )

    internal fun writeCancel(key: String) = writeEntryAndFlush(JournalEntry.Companion.CANCEL, key)

    internal fun writeRemove(key: String) = writeEntryAndFlush(JournalEntry.Companion.REMOVE, key)

    internal fun writeRead(key: String) =
        writeEntryAndFlush(JournalEntry.Companion.READ, key)

    private fun writeEntryAndFlush(opcode: Byte, key: String, transformedKey: String? = null) {
        writeEntry(opcode, key, transformedKey)
        sink.flush()
    }

    private fun writeEntry(opcode: Byte, key: String, transformedKey: String? = null) {
        sink.writeByte(opcode)
        sink.writeUShortLengthUtf8(key)
        if (transformedKey != null) sink.writeUByteLengthUtf8(transformedKey)
    }

    private fun Sink.writeUByteLengthUtf8(string: String) {
        writeUByte(string.length.toUByte())
        writeString(string)
    }

    private fun Sink.writeUShortLengthUtf8(string: String) {
        writeUShort(string.length.toUShort())
        writeString(string)
    }

    override fun close(): Unit = sink.close()
}
