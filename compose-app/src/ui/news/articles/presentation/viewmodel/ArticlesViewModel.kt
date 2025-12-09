package ui.news.articles.presentation.viewmodel

import clib.presentation.viewmodel.ViewModel
import klib.data.load.LoadingResult
import kotlinx.coroutines.flow.StateFlow
import org.koin.android.annotation.KoinViewModel
import ui.news.articles.data.net.http.ArticlesApiService

@KoinViewModel
public class ArticleViewModel(private val apiService: ArticlesApiService) : ViewModel<ArticlesAction>() {

    public val state: StateFlow<LoadingResult<ArticlesState>>
        field = loadStateFlow(
            fetcher = { result ->
                ArticlesState(apiService.getArticles())
            },
        )

    override fun action(action: ArticlesAction): Unit =
        when (action) {
            ArticlesAction.Retry -> state.restart()
        }
}


