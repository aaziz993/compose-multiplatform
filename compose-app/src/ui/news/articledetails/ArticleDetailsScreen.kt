package ui.news.articledetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import clib.data.type.primitives.string.annotatedStringResource
import clib.presentation.components.loading.CenterLoadingIndicator
import clib.presentation.navigation.NavigationAction
import coil3.compose.AsyncImage
import compose_app.generated.resources.Res
import compose_app.generated.resources.back
import compose_app.generated.resources.read_more_at
import compose_app.generated.resources.retry
import compose_app.generated.resources.share
import klib.data.load.LoadingResult
import klib.data.load.loading
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import ui.navigation.presentation.ArticleDetails
import ui.news.articledetails.viewmodel.ArticleDetailsAction
import ui.news.data.model.Article

@Composable
public fun ArticleDetailsScreen(
    modifier: Modifier = Modifier,
    route: ArticleDetails = ArticleDetails(),
    state: LoadingResult<Article> = loading(),
    onAction: (ArticleDetailsAction) -> Unit = {},
    onNavigationAction: (NavigationAction) -> Unit = {},
) {
    LaunchedEffect(Unit) {
        onAction(ArticleDetailsAction.Fetch(route.articleId))
    }

    when (val result = state.toSuccess()) {
        is LoadingResult.Loading -> CenterLoadingIndicator()

        is LoadingResult.Success -> ArticleDetailsSuccessContent(
            modifier,
            result.value,
            {
                onNavigationAction(NavigationAction.Pop)
            },
            { url ->
                onAction(ArticleDetailsAction.Share(url))
            },
            { url ->
                onAction(ArticleDetailsAction.ReadMore(url))
            },
        )

        else -> RetryTextButton(route.articleId, onAction)
    }
}

@Composable
private fun RetryTextButton(
    articleId: Long,
    onAction: (ArticleDetailsAction) -> Unit,
) = Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    TextButton(
        onClick = {
            onAction(ArticleDetailsAction.Fetch(articleId))
        },
    ) {
        Text(stringResource(Res.string.retry))
    }
}

@Composable
private fun ArticleDetailsSuccessContent(
    @Suppress("ComposeModifierWithoutDefault") modifier: Modifier,
    article: Article,
    onBackClick: () -> Unit,
    onShareClick: (String) -> Unit,
    onReadMoreClick: (String) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier,
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = article.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
            )

            IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.TopStart)) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(Res.string.back),
                    tint = Color.White,
                )
            }

            article.url?.let { url ->
                IconButton(
                    onClick = { onShareClick(url) },
                    modifier = Modifier.align(Alignment.TopEnd),
                ) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = stringResource(Res.string.share),
                        tint = Color.White,
                    )
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        ) {

            Text(
                text = article.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = article.newsSite,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Text(
                    text = " • ",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Text(
                    text = article.publishedAt.toLocalDateTime(TimeZone.currentSystemDefault())
                        .format(
                            LocalDateTime.Format {
                                year()
                                char('-')
                                monthNumber()
                                char('-')
                                day()

                                chars(" at ")

                                hour()
                                char(':')
                                minute()
                            },
                        ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = article.summary,
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
            )

            article.url?.let { url ->
                Text(
                    text = annotatedStringResource(
                        Res.string.read_more_at,
                        article.newsSite,
                        linkInteractionListener = { onReadMoreClick(url) },
                    ),
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewArticleDetailsScreen(): Unit = ArticleDetailsScreen()
