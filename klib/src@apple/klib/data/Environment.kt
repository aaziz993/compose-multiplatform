@file:OptIn(ExperimentalForeignApi::class)

package klib.data

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

public actual fun getEnv(name: String): String? = getenv(name)?.toKString()
