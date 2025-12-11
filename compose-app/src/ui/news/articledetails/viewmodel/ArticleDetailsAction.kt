package ui.news.articledetails.viewmodel

public sealed interface ArticleDetailsAction {
    public data class Fetch(val articleId: Long) : ArticleDetailsAction
    public data class ReadMore(val url: String) : ArticleDetailsAction
    public data class Share(val url: String) : ArticleDetailsAction
}
