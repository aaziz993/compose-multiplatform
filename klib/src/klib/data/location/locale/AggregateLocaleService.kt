package klib.data.location.locale

public class AggregateLocaleService(
    private val services: List<LocaleService>
) : LocaleService() {

    override suspend fun getLocales(): List<Locale> = services.flatMap { service ->
        service.getLocales()
    }

    override suspend fun setLocale(locale: Locale) {
        super.setLocale(locale)
        services.forEach { service -> service.setLocale(locale) }
        translations = services.fold(emptyMap()) { acc, service -> acc + service.translations }
    }
}
