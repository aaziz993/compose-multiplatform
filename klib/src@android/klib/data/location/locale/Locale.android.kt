package klib.data.location.locale

import androidx.core.os.LocaleListCompat

public actual val Locale.Companion.current: Locale
    get() = LocaleListCompat.getDefault()[0]!!.toKotlinLocale()
