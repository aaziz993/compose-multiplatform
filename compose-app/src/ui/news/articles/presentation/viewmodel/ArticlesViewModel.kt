package ui.news.articles.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import clib.presentation.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ui.news.articles.data.net.http.ArticlesApiService

@KoinViewModel
public class ArticleViewModel(private val apiService: ArticlesApiService) : ViewModel<Unit>() {

    public val state: StateFlow<ArticlesState>
        field = MutableStateFlow(ArticlesState())

    init {
        viewModelScope.launch {
            val articles = apiService.getArticles()
            state.update { it.copy(articles = articles.filter { article -> !article.imageUrl.isEmpty() }) }
        }
    }

    override fun action(action: Unit): Unit = Unit
}


