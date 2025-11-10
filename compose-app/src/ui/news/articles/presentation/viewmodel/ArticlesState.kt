package ui.news.articles.presentation.viewmodel

import ui.news.articles.data.model.Article

public data class ArticlesState(
    val articles: List<Article> = emptyList(),
)
