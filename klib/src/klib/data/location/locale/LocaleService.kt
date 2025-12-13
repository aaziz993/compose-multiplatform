package klib.data.location.locale

public open class LocaleService {

    public open suspend fun getLocales(): List<Locale> = emptyList()

    public open suspend fun getTranslations(locale: Locale): Map<String, List<String>> = emptyMap()

    public suspend fun getLocalization(locale: Locale): Localization = Localization(
        getLocales(),
        locale,
        getTranslations(locale),
    )
}

