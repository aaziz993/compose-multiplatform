package klib.data.cache.kache.file.journal

import klib.data.fs.path.resolve
import klib.data.fs.path.toPath
import kotlin.test.*
import kotlinx.io.buffered
import kotlinx.io.files.SystemFileSystem

class JournalReadTest {

    @Test
    fun readNonExistingJournal() {
        val fileSystem = SystemFileSystem

        assertNull(fileSystem.readJournalIfExists(directory))
    }

    @Test
    fun readSimpleJournal() {
        val fileSystem = SystemFileSystem
        fileSystem.createDirectories(directory)
        fileSystem.sink(journalFile).buffered().use { it.write(journalWithAdd) }

        assertEquals(journalWithAddData, fileSystem.readJournalIfExists(directory))
    }

    @Test
    fun readSimpleBackupJournal() {
        val fileSystem = SystemFileSystem
        fileSystem.createDirectories(directory)
        fileSystem.sink(backupJournalFile).buffered().use { it.write(journalWithAdd) }

        assertEquals(journalWithAddData, fileSystem.readJournalIfExists(directory))
        assertTrue(fileSystem.exists(journalFile))
        assertFalse(fileSystem.exists(backupJournalFile))
    }

    @Test
    fun readJournalAlongWithTempAndBackupJournal() {
        val fileSystem = SystemFileSystem
        fileSystem.createDirectories(directory)
        fileSystem.sink(journalFile).buffered().use { it.write(journalWithAdd) }
        fileSystem.sink(tempJournalFile).buffered().use { it.write(emptyJournal) }
        fileSystem.sink(backupJournalFile).buffered().use { it.write(emptyJournal) }

        assertEquals(journalWithAddData, fileSystem.readJournalIfExists(directory))
        assertTrue(fileSystem.exists(journalFile))
        assertFalse(fileSystem.exists(tempJournalFile))
        assertFalse(fileSystem.exists(backupJournalFile))
    }

    @Test
    fun readJournalWithDirtyAdding() {
        val fileSystem = SystemFileSystem
        fileSystem.createDirectories(directory)
        fileSystem.sink(journalFile).buffered().use { it.write(journalWithDirty) }

        assertEquals(journalWithDirtyData, fileSystem.readJournalIfExists(directory))
    }

    @Test
    fun readJournalWithAddingAndRemoving() {
        val fileSystem = SystemFileSystem
        fileSystem.createDirectories(directory)
        fileSystem.sink(journalFile).buffered().use { it.write(journalWithAddAndRemove) }

        assertEquals(journalWithAddAndRemoveData, fileSystem.readJournalIfExists(directory))
    }

    @Test
    fun readJournalWithMixedAdding() {
        val fileSystem = SystemFileSystem
        fileSystem.createDirectories(directory)
        fileSystem.sink(journalFile).buffered().use { it.write(journalWithMixedAdd) }

        assertEquals(journalWithMixedAddData, fileSystem.readJournalIfExists(directory))
    }

    @Test
    fun readJournalWithAddingAndReAdding() {
        val fileSystem = SystemFileSystem
        fileSystem.createDirectories(directory)
        fileSystem.sink(journalFile).buffered().use { it.write(journalWithAddAndReAdd) }

        assertEquals(journalWithAddAndReAddData, fileSystem.readJournalIfExists(directory))
    }

    @Test
    fun readJournalWithAddingAndDirtyAdding() {
        val fileSystem = SystemFileSystem
        fileSystem.createDirectories(directory)
        fileSystem.sink(journalFile).buffered().use { it.write(journalWithAddAndDirtyAdd) }

        assertEquals(journalWithAddAndDirtyAddData, fileSystem.readJournalIfExists(directory))
    }

    @Test
    fun readJournalWithAddingAndCancelledAdding() {
        val fileSystem = SystemFileSystem
        fileSystem.createDirectories(directory)
        fileSystem.sink(journalFile).buffered().use { it.write(journalWithAddAndCancelledAdd) }

        assertEquals(journalWithAddAndCancelledAddData, fileSystem.readJournalIfExists(directory))
    }

    companion object {

        private const val KEY_1 = "one"
        private const val KEY_2 = "two"

        private val directory = "/cache/".toPath()
        private val journalFile = directory.resolve(JOURNAL_FILE)
        private val tempJournalFile = directory.resolve(JOURNAL_FILE_TEMP)
        private val backupJournalFile = directory.resolve(JOURNAL_FILE_BACKUP)

        private val keyOneBytes = byteArrayOf(0x00, KEY_1.length.toByte()) + KEY_1.encodeToByteArray()
        private val keyTwoBytes = byteArrayOf(0x00, KEY_2.length.toByte()) + KEY_2.encodeToByteArray()

        private val journalHeader =
            byteArrayOf(0x4A, 0x4F, 0x55, 0x52, 0x4E, 0x41, 0x4C, JOURNAL_VERSION, 0x00, 0x00, 0x00, 0x01, 0x00)
        private val emptyJournal = journalHeader

        private val journalWithDirty =
            byteArrayOf(*journalHeader, JournalEntry.DIRTY, *keyOneBytes, JournalEntry.DIRTY, *keyTwoBytes)

        private val journalWithDirtyData = JournalData(
            cleanEntries = linkedMapOf(),
            dirtyEntryKeys = setOf(KEY_1, KEY_2),
            redundantEntriesCount = 2,
        )

        private val journalWithAdd =
            byteArrayOf(*journalHeader, JournalEntry.DIRTY, *keyOneBytes, JournalEntry.CLEAN, *keyOneBytes)

        private val journalWithAddData = JournalData(
            cleanEntries = linkedMapOf(KEY_1 to null),
            dirtyEntryKeys = emptySet(),
            redundantEntriesCount = 1,
        )

        private val journalWithMixedAdd =
            byteArrayOf(
                *journalWithDirty,
                JournalEntry.CLEAN, *keyOneBytes,
                JournalEntry.CLEAN, *keyTwoBytes,
            )
        private val journalWithMixedAddData = JournalData(
            cleanEntries = linkedMapOf(KEY_1 to null, KEY_2 to null),
            dirtyEntryKeys = emptySet(),
            redundantEntriesCount = 2,
        )

        private val journalWithAddAndRemove =
            byteArrayOf(
                *journalWithAdd,
                JournalEntry.DIRTY, *keyOneBytes,
                JournalEntry.REMOVE, *keyOneBytes,
            )
        private val journalWithAddAndRemoveData = JournalData(
            cleanEntries = linkedMapOf(),
            dirtyEntryKeys = emptySet(),
            redundantEntriesCount = 4,
        )

        private val journalWithAddAndReAdd =
            byteArrayOf(
                *journalWithAdd,
                JournalEntry.DIRTY, *keyOneBytes,
                JournalEntry.CLEAN, *keyOneBytes,
            )
        private val journalWithAddAndReAddData = JournalData(
            cleanEntries = linkedMapOf(KEY_1 to null),
            dirtyEntryKeys = emptySet(),
            redundantEntriesCount = 3,
        )

        private val journalWithAddAndDirtyAdd =
            byteArrayOf(
                *journalWithAdd,
                JournalEntry.DIRTY, *keyOneBytes,
            )
        private val journalWithAddAndDirtyAddData = JournalData(
            cleanEntries = linkedMapOf(KEY_1 to null),
            dirtyEntryKeys = setOf(KEY_1),
            redundantEntriesCount = 2,
        )

        private val journalWithAddAndCancelledAdd =
            byteArrayOf(
                *journalWithAdd,
                JournalEntry.DIRTY, *keyOneBytes,
                JournalEntry.CANCEL, *keyOneBytes,
            )

        private val journalWithAddAndCancelledAddData = JournalData(
            cleanEntries = linkedMapOf(KEY_1 to null),
            dirtyEntryKeys = emptySet(),
            redundantEntriesCount = 3,
        )
    }
}
