package ui.news.data.model

import kotlin.time.Clock
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Article(
    val id: Long = 0,
    val title: String = "Fake news",
    val url: String? = null,
    @SerialName("image_url")
    val imageUrl: String = "http://fake.news/image",
    @SerialName("news_site")
    val newsSite: String = "http://fake.news",
    val summary: String = "Fake news summary",
    @SerialName("published_at")
    val publishedAt: Instant = Clock.System.now(),
    @SerialName("updated_at")
    val updatedAt: Instant = Clock.System.now(),
)
