package ui.news.articles.presentation.viewmodel

import ui.news.articles.data.model.Article

public data class ArticlesViewState(
    val articles: List<Article> = emptyList(),
)
