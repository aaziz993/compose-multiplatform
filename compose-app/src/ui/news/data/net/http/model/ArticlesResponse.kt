package ui.news.data.net.http.model

import kotlinx.serialization.Serializable
import ui.news.data.model.Article

@Serializable
public data class ArticlesResponse(
    val results: List<Article>
)
