package klib.data.net.http.client

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

public abstract class KtorfitClient(
    baseUrl: String,
    httpClient: HttpClient = createHttpClient()
) {

    protected open val json: Json = HTTP_CLIENT_JSON

    protected val httpClient: HttpClient = httpClient.config {
        defaultRequest {
            configureDefaultRequest()
            url(baseUrl)
        }

        install(ContentNegotiation) {
            json(json)
        }
    }

    protected val ktorfit: Ktorfit = Ktorfit.Builder()
        .httpClient(httpClient).baseUrl(baseUrl).configureKtorfit().build()

    protected open fun DefaultRequest.DefaultRequestBuilder.configureDefaultRequest(): Unit = Unit

    protected open fun Ktorfit.Builder.configureKtorfit(): Ktorfit.Builder = this
}
