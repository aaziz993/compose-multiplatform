@file:OptIn(ExperimentalUuidApi::class)

package klib.data.type.primitives

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

public val UUID_DEFAULT: Uuid
    get() = Uuid.random()

public fun Uuid.Companion.parseOrNull(value: String): Uuid? = value.runCatching(::parse).getOrNull()
