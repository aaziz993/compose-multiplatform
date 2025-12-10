package ui.news.articles.presentation.viewmodel

import clib.presentation.navigation.Router
import clib.presentation.viewmodel.ViewModel
import klib.data.load.LoadingResult
import klib.data.load.loading
import kotlinx.coroutines.flow.StateFlow
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Provided
import ui.navigation.presentation.ArticleDetails
import ui.news.data.db.ArticlesRepository
import ui.news.data.model.Article

@KoinViewModel
public class ArticleViewModel(
    private val articlesRepository: ArticlesRepository,
    @Provided
    private val router: Router,
) : ViewModel<ArticlesAction>() {

    public val state: StateFlow<LoadingResult<List<Article>>>
        field = loading<List<Article>>().loadStateFlow(
            fetcher = { result -> articlesRepository.getArticles() },
        )

    override fun action(action: ArticlesAction): Unit =
        when (action) {
            ArticlesAction.Fetch -> state.restart()
            is ArticlesAction.ArticleDetails -> articleDetails(action.articleId)
        }

    private fun articleDetails(articleId: Long) = router.push(ArticleDetails(articleId))
}


