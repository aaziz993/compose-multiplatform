package ui.news.articles.presentation.viewmodel

public sealed interface ArticlesAction {
    public data object Retry : ArticlesAction
}
