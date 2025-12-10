package ui.news.data.net.http

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.core.annotation.Single
import ui.news.data.model.Article
import ui.news.data.net.http.model.ArticlesResponse

private const val BASE_URL = "https://api.spaceflightnewsapi.net/v4"

@Single
public class ArticlesApiService(private val httpClient: HttpClient) {

    public suspend fun getArticles(): List<Article> {
        return httpClient.get("${BASE_URL}/articles/?format=json")
            .body<ArticlesResponse>().results.map {
                val imageUrl = it.imageUrl.ensureHttpsUrl()
                it.copy(imageUrl = imageUrl, summary = it.summary.trim(), title = it.title.trim())
            }
    }

    private fun String.ensureHttpsUrl(): String =
        if (startsWith("https")) this else replaceFirst("http", "https")
}
