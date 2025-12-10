package ui.news.articledetails.viewmodel

public sealed interface ArticleDetailsAction {
    public data class Fetch(val articleId: Long) : ArticleDetailsAction
    public data class ReadMore(val articleId: Long) : ArticleDetailsAction
    public data class Share(val articleId: Long) : ArticleDetailsAction
}
