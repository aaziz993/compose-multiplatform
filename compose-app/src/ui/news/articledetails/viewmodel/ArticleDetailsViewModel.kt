package ui.news.articledetails.viewmodel

import androidx.lifecycle.viewModelScope
import clib.presentation.viewmodel.ViewModel
import klib.data.load.LoadingResult
import klib.data.load.failure
import klib.data.load.loading
import klib.data.load.success
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ui.news.data.db.ArticlesRepository
import ui.news.data.model.Article

@KoinViewModel
public class ArticleDetailsViewModel(
    private val repository: ArticlesRepository,
) : ViewModel<ArticleDetailsAction>() {

    public val state: StateFlow<LoadingResult<Article>>
        field = MutableStateFlow(loading())

    override fun action(action: ArticleDetailsAction): Unit = when (action) {
        is ArticleDetailsAction.Fetch -> fetch(action.articleId)
        is ArticleDetailsAction.ReadMore -> readMore(action.articleId)
        is ArticleDetailsAction.Share -> share(action.articleId)
    }

    private fun fetch(articleId: Long) {
        viewModelScope.launch {
            state.update { loading() }

            try {
                val article = repository.getArticleById(articleId)
                if (article != null) state.update { success(article) }
                else state.update { failure(NoSuchElementException("No article id $articleId")) }
            }
            catch (e: Exception) {
                state.update { failure(e) }
            }
        }
    }

    private fun readMore(articleId: Long): Unit = TODO()

    private fun share(articleId: Long): Unit = TODO()
}
