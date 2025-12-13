package klib.data.location.locale

public class AggregateLocaleService(
    private val services: List<LocaleService>
) : LocaleService() {

    override suspend fun getLocale(locale: Locale): Localization {
        val localizations = services.map { service -> service.getLocale(locale) }

        return Localization(
            localizations.fold(emptyList()) { acc, localization -> acc + localization.locales },
            locale,
            localizations.fold(emptyMap()) { acc, localization ->
                acc + localization.translations
            },
        )
    }
}
