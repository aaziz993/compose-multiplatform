package klib.data.type.primitive

import kotlinx.cinterop.UnsafeNumber
import platform.posix.timespec

@OptIn(UnsafeNumber::class)
public val timespec.epochMillis: Long
    get() = tv_sec * 1000L + tv_sec / 1_000_000L
