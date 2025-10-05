package klib.processing

import com.google.devtools.ksp.symbol.KSName

public fun KSName?.safeString(): String = this?.asString() ?: ""
