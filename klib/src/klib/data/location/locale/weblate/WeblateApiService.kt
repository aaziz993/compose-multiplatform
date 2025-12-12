package klib.data.location.locale.weblate

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.Url
import io.ktor.serialization.kotlinx.json.json
import klib.data.location.locale.Locale
import klib.data.location.locale.LocaleService
import klib.data.location.locale.weblate.model.WeblateTranslationsResponse
import klib.data.location.locale.weblate.model.WeblateUnitsResponse
import klib.data.net.http.client.HTTP_CLIENT_JSON
import klib.data.net.http.client.createHttpClient
import klib.data.net.http.client.ktorfit
import kotlinx.coroutines.flow.toList

public class WeblateApiService(
    baseUrl: String,
    apiKey: String,
    private val project: String,
    private val components: Set<String>,
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

    override suspend fun getLocales(): List<Locale> =
        components.flatMap { component ->
            api.getAllTranslations(
                project,
                component,
            ).toList().flatMap(WeblateTranslationsResponse::results)
        }.mapNotNull { translation ->
            (translation.language.aliases + translation.language.code)
                .sortedByDescending(String::length)
                .firstNotNullOfOrNull(Locale::forLanguageTagOrNull)
        }.toList()

    override suspend fun getTranslations(locale: Locale): Map<String, List<String>> {
        val languageTag = locale.toString().replace("-", "_")

        return components.flatMap { component ->
            api.getAllUnits(
                project,
                component,
                languageTag,
            ).toList().flatMap(WeblateUnitsResponse::results)
        }.mapNotNull { unit ->
            val segments = Url(unit.translation).segments
            if (segments[2] == project && segments[4] == languageTag) unit.context to unit.target else null
        }.toMap()
    }
}
