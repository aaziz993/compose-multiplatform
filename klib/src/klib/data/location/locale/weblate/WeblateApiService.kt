package klib.data.location.locale.weblate

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import klib.data.location.locale.Locale
import klib.data.location.locale.LocaleService
import klib.data.location.locale.Localization
import klib.data.location.locale.weblate.model.WeblateTranslationsResponse
import klib.data.location.locale.weblate.model.WeblateUnitsResponse
import klib.data.net.http.client.HTTP_CLIENT_JSON
import klib.data.net.http.client.createHttpClient
import klib.data.net.http.client.ktorfit
import kotlinx.coroutines.flow.toList

public class WeblateApiService(
    baseUrl: String,
    private val project: String,
    private val components: Set<String>,
    apiKey: String,
    httpClient: HttpClient = createHttpClient {
        install(ContentNegotiation) {
            json(HTTP_CLIENT_JSON)
        }
    },
) : LocaleService() {

    private val api: WeblateApi = httpClient.config {
        defaultRequest {
            header(HttpHeaders.Authorization, "Token  $apiKey")
        }
    }.ktorfit { baseUrl(baseUrl) }.createWeblateApi()

    override suspend fun getLocale(locale: Locale): Localization {
        val locales = components.flatMap { component ->
            api.getAllTranslations(
                project,
                component,
            ).toList().flatMap(WeblateTranslationsResponse::results)
        }.mapNotNull { translation ->
            (translation.language.aliases + translation.language.code)
                .sortedByDescending(String::length)
                .firstNotNullOfOrNull(Locale::forLanguageTagOrNull)
        }.toList()

        val languageTag = locale.toString().replace("-", "_")

        val translations = components.flatMap { component ->
            api.getAllUnits(
                project,
                component,
                languageTag,
            ).toList().flatMap(WeblateUnitsResponse::results)
        }.associate { unit -> unit.source.first() to unit.target }

        return Localization(locales, locale, translations)
    }
}
