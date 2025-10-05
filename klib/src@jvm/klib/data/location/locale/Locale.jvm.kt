package klib.data.location.locale

import java.util.Locale as JavaLocale

public actual fun currentLocale(): Locale = JavaLocale.getDefault().let { locale ->
    Locale.forLanguage(
        locale.language,
        locale.script,
        locale.country,
    )
}
