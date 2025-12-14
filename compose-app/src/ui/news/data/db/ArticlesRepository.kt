package ui.news.data.db

import klib.data.type.collections.replaceWith
import klib.data.type.primitives.time.nowInstant
import org.koin.core.annotation.Single
import ui.news.data.model.Article
import ui.news.data.net.http.ArticlesApiService

@Single
public class ArticlesRepository(
//    databaseDriverFactory: DatabaseDriverFactory,
    private val api: ArticlesApiService
) {
//    private val database = Database(databaseDriverFactory)

    private val articles = mutableListOf<Article>().apply {
        add(
            Article(
                0,
                "Fake news",
                "https://fakenews.com",
                "https://fakenews.com/image",
                "FakeNews",
                "Some fake news summary",
                nowInstant,
                nowInstant,
            ),
        )
    }

    public suspend fun getArticles(): List<Article> {
        try {
            return api.getArticles().also {
//                database.insertArticles(it)
                articles.replaceWith(it)
            }
        }
        catch (e: Exception) {
            try {
//                val storedArticles = database.getNewestArticles()
                val storedArticles = articles
                if (storedArticles.isNotEmpty()) {
                    return storedArticles
                }
                throw e
            }
            catch (e: Exception) {
                throw e
            }
        }
    }

    public fun getArticleById(id: Long): Article? {
//        return database.getArticleById(id)
        return articles.find { article -> article.id == id }
    }
}
