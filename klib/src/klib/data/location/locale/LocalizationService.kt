package klib.data.location.locale

public interface LocalizationService {

    public suspend fun getLocalization(locale: Locale): Localization
}
