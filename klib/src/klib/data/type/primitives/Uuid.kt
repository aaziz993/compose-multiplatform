package klib.data.type.primitives

import kotlin.uuid.Uuid

public val UUID_DEFAULT: Uuid
    get() = Uuid.random()

public fun String.toUuid(): Uuid = Uuid.parse(this)

public fun Uuid.Companion.parseOrNull(value: String): Uuid? = value.runCatching(::parse).getOrNull()
