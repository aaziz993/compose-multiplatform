package ui.news.articles.data.net.http.model

import kotlinx.serialization.Serializable
import ui.news.articles.data.model.Article

@Serializable
public data class ArticlesResponse(
    val results: List<Article>
)
