package klib.data.cache.kache.file.journal

import klib.data.nullableUse
import kotlin.test.*
import kotlinx.io.Buffer
import kotlinx.io.EOFException
import kotlinx.io.RawSource
import kotlinx.io.buffered

class JournalReaderTest {

    @Test
    fun validateHeader() {
        // Empty buffer
        assertFailsWith<JournalInvalidHeaderException> {
            JournalReader(Buffer()).use { it.validateHeader() }
        }

        // Text file containing "Text File"
        val textFile = bufferOf(0x54, 0x65, 0x78, 0x74, 0x20, 0x46, 0x69, 0x6C, 0x65)
        assertFailsWith<JournalInvalidHeaderException> {
            JournalReader(textFile).use { it.validateHeader() }
        }

        // Journal with incorrect version
        val journalWithIncorrectVersion = bufferOf(
            0x4A, 0x4F, 0x55, 0x52, 0x4E, 0x41, 0x4C, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00,
            0x01, 0x00, 0x07, 0x54, 0x65, 0x73, 0x74, 0x4B, 0x65, 0x79,
        )

        assertFailsWith<JournalInvalidHeaderException> {
            JournalReader(journalWithIncorrectVersion).use { it.validateHeader() }
        }

        // Different cache version
        val journalWithDifferentCacheVersion = bufferOf(
            0x4A, 0x4F, 0x55, 0x52, 0x4E, 0x41, 0x4C, 0x01, 0x00, 0x00, 0x00, 0x02, 0x00,
            0x01, 0x00, 0x07, 0x54, 0x65, 0x73, 0x74, 0x4B, 0x65, 0x79,
        )

        assertFailsWith<JournalInvalidHeaderException> {
            JournalReader(journalWithDifferentCacheVersion).use { it.validateHeader() }
        }

        // Valid header
        val emptyJournalWithValidHeader = bufferOf(
            0x4A, 0x4F, 0x55, 0x52, 0x4E, 0x41, 0x4C, JOURNAL_VERSION, 0x00, 0x00, 0x00, 0x01, 0x00,
        )

        try {
            JournalReader(emptyJournalWithValidHeader).use { it.validateHeader() }
        }
        catch (_: JournalInvalidHeaderException) {
            fail("Header should be detected as valid")
        }
    }

    @Test
    fun readEntry() {
        // Empty buffer
        val emptyBuffer = bufferOf()
        val emptyBufferResult = JournalReader(emptyBuffer).nullableUse(JournalReader::readEntry)
        assertNull(emptyBufferResult)

        // Dirty operation
        val dirtyOperation = bufferOf(
            JournalEntry.DIRTY, 0x00, KEY_1.length.toByte(), *KEY_1.encodeToByteArray(),
        )
        val dirtyOperationResult = JournalReader(dirtyOperation).use { it.readEntry() }
        assertEquals(JournalEntry.Dirty(KEY_1), dirtyOperationResult)

        // Clean operation
        val cleanOperation = bufferOf(
            JournalEntry.CLEAN, 0x00, KEY_1.length.toByte(), *KEY_1.encodeToByteArray(),
        )
        val cleanOperationResult = JournalReader(cleanOperation).use { it.readEntry() }
        assertEquals(JournalEntry.Clean(KEY_1), cleanOperationResult)

        // Remove operation
        val removeOperation = bufferOf(
            JournalEntry.REMOVE, 0x00, KEY_1.length.toByte(), *KEY_1.encodeToByteArray(),
        )
        val removeOperationResult = JournalReader(removeOperation).use { it.readEntry() }
        assertEquals(JournalEntry.Remove(KEY_1), removeOperationResult)

        // Invalid operation
        val invalidOperation = bufferOf(
            (0xFE).toByte(), 0x00, KEY_1.length.toByte(), *KEY_1.encodeToByteArray(),
        )
        assertFailsWith<JournalInvalidOpcodeException> {
            JournalReader(invalidOperation).use { it.readEntry() }
        }

        // Read operation without key
        val readOperationWithoutKey = bufferOf(JournalEntry.READ)
        assertFailsWith<EOFException> {
            JournalReader(readOperationWithoutKey).use(JournalReader::readEntry)
        }

        // Dirty operation with truncated key
        val dirtyOperationWithTruncatedKey = bufferOf(
            JournalEntry.DIRTY, 0x00, KEY_1.length.toByte(), *KEY_1.first().toString().encodeToByteArray(),
        )
        assertFailsWith<EOFException> {
            JournalReader(dirtyOperationWithTruncatedKey).use(JournalReader::readEntry)
        }
    }

    @Test
    fun close() {
        val source = object : RawSource {
            var wasClosed = false

            override fun readAtMostTo(sink: Buffer, byteCount: Long): Long = byteCount

            override fun close() {
                wasClosed = true
            }
        }

        JournalReader(source.buffered()).close()
        assertTrue(source.wasClosed)
    }

    companion object {

        private const val KEY_1 = "one"

        private fun bufferOf(vararg elements: Byte): Buffer = Buffer().apply { write(elements) }
    }
}
