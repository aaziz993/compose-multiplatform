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
import klib.data.net.http.client.HTTP_CLIENT_JSON
import klib.data.net.http.client.createHttpClient
import klib.data.net.http.client.ktorfit
import kotlinx.coroutines.flow.toList

public class WeblateApiService(
    baseUrl: String,
    apiKey: String,
    private val project: String,
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
        api.getAllTranslations().toList().flatMap { response ->
            response.results.filter { translation -> translation.component.project.name == project }
        }.mapNotNull { translation ->
            (translation.language.aliases + translation.language.code)
                .sortedByDescending(String::length)
                .firstNotNullOfOrNull(Locale::forLanguageTagOrNull)
        }

    override suspend fun getTranslations(locale: Locale): Map<String, List<String>> {
        val languageTag = locale.toString()

        return api.getAllUnits().toList().flatMap { response ->
            response.results.mapNotNull { unit ->
                val segments = Url(unit.translation).segments
                if (segments[2] == project && segments[3] == languageTag) unit.context to unit.target else null
            }
        }.toMap()
    }
}
