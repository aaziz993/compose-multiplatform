package ui.news.articles.presentation.viewmodel

public sealed interface ArticlesAction {
    public data object Fetch : ArticlesAction
    public data class ArticleDetails(val articleId: Long) : ArticlesAction
}
