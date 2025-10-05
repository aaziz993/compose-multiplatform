package klib.data.location.locale

import java.util.Locale as JavaLocale

public actual val Locale.Companion.current: Locale
    get() = JavaLocale.getDefault().toKotlinLocale()



