package klib.data.cache.kache.file.journal

import klib.data.cache.kache.KacheStrategy
import klib.data.filesystem.atomicMove
import klib.data.filesystem.path.resolve
import kotlinx.io.buffered
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path

internal const val JOURNAL_MAGIC = "JOURNAL"
internal const val JOURNAL_VERSION: Byte = 4

internal const val JOURNAL_FILE = "journal"
internal const val JOURNAL_FILE_TEMP = "$JOURNAL_FILE.tmp"
internal const val JOURNAL_FILE_BACKUP = "$JOURNAL_FILE.bkp"

internal data class JournalData(
    val cleanEntries: LinkedHashMap<String, String?>,
    val dirtyEntryKeys: Set<String>,
    val redundantEntriesCount: Int,
)

internal fun FileSystem.readJournalIfExists(
    directory: Path,
    cacheVersion: Int = 1,
    strategy: KacheStrategy = KacheStrategy.LRU,
): JournalData? {
    val journalFile = directory.resolve(JOURNAL_FILE)
    val tempJournalFile = directory.resolve(JOURNAL_FILE_TEMP)
    val backupJournalFile = directory.resolve(JOURNAL_FILE_BACKUP)

    // If a backup file exists, use it instead.
    if (exists(backupJournalFile)) {
        // If journal file also exists just delete backup file.
        if (exists(journalFile)) {
            delete(backupJournalFile)
        }
        else {
            atomicMove(backupJournalFile, journalFile)
        }
    }

    // If a temp file exists, delete it
    delete(tempJournalFile)

    if (!exists(journalFile)) return null

    var entriesCount = 0
    val dirtyEntryKeys = mutableSetOf<String>()
    val cleanEntries = linkedMapOf<String, String?>()

    JournalReader(source(journalFile).buffered(), cacheVersion, strategy).use { reader ->
        reader.validateHeader()

        while (true) {
            val entry = reader.readEntry() ?: break
            entriesCount++

            when (entry) {
                is JournalEntry.Dirty -> {
                    dirtyEntryKeys.add(entry.key)
                }

                is JournalEntry.Clean -> {
                    // Remove existing entry to re-insert it at the end of the map
                    val transformedKey = cleanEntries.remove(entry.key)

                    dirtyEntryKeys.remove(entry.key)
                    cleanEntries[entry.key] = transformedKey
                }

                is JournalEntry.CleanWithTransformedKey -> {
                    // Remove existing entry to re-insert it at the end of the map
                    cleanEntries.remove(entry.key)

                    dirtyEntryKeys.remove(entry.key)
                    cleanEntries[entry.key] = entry.transformedKey
                }

                is JournalEntry.Cancel -> {
                    dirtyEntryKeys.remove(entry.key)
                }

                is JournalEntry.Remove -> {
                    dirtyEntryKeys.remove(entry.key)
                    cleanEntries.remove(entry.key)
                }

                is JournalEntry.Read -> {
                    if (strategy == KacheStrategy.LRU || strategy == KacheStrategy.MRU) {
                        // Remove existing entry to re-insert it at the end of the map
                        val transformedKey = cleanEntries.remove(entry.key)
                        cleanEntries[entry.key] = transformedKey
                    }
                }
            }
        }
    }

    return JournalData(
        cleanEntries = cleanEntries,
        dirtyEntryKeys = dirtyEntryKeys,
        redundantEntriesCount = entriesCount - cleanEntries.size,
    )
}

internal fun FileSystem.writeJournalAtomically(
    directory: Path,
    cleanEntries: Map<String, String>,
    dirtyEntryKeys: Collection<String>
) {
    val journalFile = directory.resolve(JOURNAL_FILE)
    val tempJournalFile = directory.resolve(JOURNAL_FILE_TEMP)
    val backupJournalFile = directory.resolve(JOURNAL_FILE_BACKUP)

    delete(tempJournalFile)

    JournalWriter(sink(tempJournalFile).buffered()).use { writer ->
        writer.writeHeader()
        writer.writeAll(cleanEntries, dirtyEntryKeys)
    }

    if (exists(journalFile)) atomicMove(journalFile, backupJournalFile, true)
    atomicMove(tempJournalFile, journalFile)
    delete(backupJournalFile)
}
