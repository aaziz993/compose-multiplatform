package klib.data.cache.kache.file.journal

import kotlinx.io.IOException

internal sealed class JournalException(override val message: String? = null) : IOException(message)

internal class JournalInvalidHeaderException(override val message: String? = null) : JournalException(message)

internal class JournalInvalidOpcodeException : JournalException()
