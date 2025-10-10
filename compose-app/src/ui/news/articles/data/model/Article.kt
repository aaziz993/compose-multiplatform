package ui.news.articles.data.model

import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Article(
    val id: Long,
    val title: String,
    val url: String,
    @SerialName("image_url")
    val imageUrl: String,
    @SerialName("news_site")
    val newsSite: String,
    val summary: String,
    @SerialName("published_at")
    val publishedAt: Instant,
    @SerialName("updated_at")
    val updatedAt: Instant,
)
