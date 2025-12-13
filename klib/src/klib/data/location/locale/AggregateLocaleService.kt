package klib.data.location.locale

public class AggregateLocaleService(
    private val services: List<LocaleService>
) : LocaleService() {

    override suspend fun getLocales(): List<Locale> = services.flatMap { service -> service.getLocales() }

    override suspend fun getTranslations(locale: Locale): Map<String, List<String>> =
        services.fold(emptyMap()) { acc, service -> acc + service.getTranslations(locale) }
}
