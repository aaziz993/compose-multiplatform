package klib.data.location.locale

import androidx.core.os.LocaleListCompat

public actual fun currentLocale(): Locale = LocaleListCompat.getDefault()[0]!!.toKotlinLocale()
