package klib.data.cache.kache.file.journal

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertTrue
import kotlinx.io.Buffer
import kotlinx.io.RawSink
import kotlinx.io.buffered
import kotlinx.io.readByteArray

class JournalWriterTest {

    @Test
    fun writeHeader() {
        val headerBytes =
            JOURNAL_MAGIC.encodeToByteArray() + JOURNAL_VERSION + byteArrayOf(0x00, 0x00, 0x00, 0x01, 0x00)

        val buffer = Buffer()
        JournalWriter(buffer).use { it.writeHeader() }
        assertContentEquals(headerBytes, buffer.readByteArray())
    }

    @Test
    fun writeDirty() {
        val bytes = byteArrayOf(
            JournalEntry.DIRTY, 0x00, KEY_1.length.toByte(), *KEY_1.encodeToByteArray(),
        )

        val buffer = Buffer()
        JournalWriter(buffer).use { it.writeDirty(KEY_1) }
        assertContentEquals(bytes, buffer.readByteArray())
    }

    @Test
    fun writeClean() {
        val bytes = byteArrayOf(
            JournalEntry.CLEAN, 0x00, KEY_1.length.toByte(), *KEY_1.encodeToByteArray(),
        )

        val buffer = Buffer()
        JournalWriter(buffer).use { it.writeClean(KEY_1) }
        assertContentEquals(bytes, buffer.readByteArray())
    }

    @Test
    fun writeRemove() {
        val bytes = byteArrayOf(
            JournalEntry.REMOVE, 0x00, KEY_1.length.toByte(), *KEY_1.encodeToByteArray(),
        )

        val buffer = Buffer()
        JournalWriter(buffer).use { it.writeRemove(KEY_1) }
        assertContentEquals(bytes, buffer.readByteArray())
    }

    @Test
    fun writeAll() {
        val bytes = byteArrayOf(
            JournalEntry.CLEAN_WITH_TRANSFORMED_KEY, 0x00, KEY_1.length.toByte(), *KEY_1.encodeToByteArray(),
            KEY_2.length.toByte(), *KEY_2.encodeToByteArray(),
            JournalEntry.DIRTY, 0x00, KEY_2.length.toByte(), *KEY_2.encodeToByteArray(),
        )

        val buffer = Buffer()
        JournalWriter(buffer).use { it.writeAll(mapOf(KEY_1 to KEY_2), listOf(KEY_2)) }
        assertContentEquals(bytes, buffer.readByteArray())
    }

    @Test
    fun close() {
        val sink = object : RawSink {
            var wasClosed = false

            override fun write(source: Buffer, byteCount: Long) {}

            override fun flush() {}

            override fun close() {
                wasClosed = true
            }
        }

        JournalWriter(sink.buffered()).close()
        assertTrue(sink.wasClosed)
    }

    companion object {

        private const val KEY_1 = "one"
        private const val KEY_2 = "two"
    }
}
