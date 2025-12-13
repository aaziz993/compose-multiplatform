package klib.data.location.locale

public open class LocaleService {

    public open suspend fun getLocale(locale: Locale): Localization = Localization()
}

