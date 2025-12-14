package klib.data.cache.kache.file.journal

internal fun JournalEntry(
    opcode: Byte,
    key: String,
    transformedKey: String? = null,
) = when (opcode) {
    JournalEntry.DIRTY -> JournalEntry.Dirty(key)
    JournalEntry.CLEAN -> JournalEntry.Clean(key)
    JournalEntry.CLEAN_WITH_TRANSFORMED_KEY -> JournalEntry.CleanWithTransformedKey(key, transformedKey!!)
    JournalEntry.CANCEL -> JournalEntry.Cancel(key)
    JournalEntry.REMOVE -> JournalEntry.Remove(key)
    JournalEntry.READ -> JournalEntry.Read(key)

    else -> throw JournalInvalidOpcodeException()
}

internal sealed interface JournalEntry {
    val key: String

    data class Dirty(override val key: String) : JournalEntry
    data class Clean(override val key: String) : JournalEntry
    data class CleanWithTransformedKey(override val key: String, val transformedKey: String) : JournalEntry
    data class Cancel(override val key: String) : JournalEntry
    data class Remove(override val key: String) : JournalEntry
    data class Read(override val key: String) : JournalEntry

    companion object {
        const val DIRTY: Byte = 0x10
        const val CLEAN: Byte = 0x20
        const val CLEAN_WITH_TRANSFORMED_KEY: Byte = 0x21
        const val CANCEL: Byte = 0x30
        const val REMOVE: Byte = 0x40
        const val READ: Byte = 0x50
    }
}
