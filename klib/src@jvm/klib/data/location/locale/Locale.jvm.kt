package klib.data.location.locale

import java.util.Locale as JavaLocale

public actual fun currentLocale(): Locale = JavaLocale.getDefault().toKotlinLocale()



