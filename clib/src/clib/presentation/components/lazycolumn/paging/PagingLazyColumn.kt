package clib.presentation.components.lazycolumn.paging

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState.Error
import androidx.paging.LoadState.Loading
import androidx.paging.LoadState.NotLoading
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import clib.presentation.components.lazycolumn.model.LazyPagingColumnLocalization
import kotlin.jvm.JvmSuppressWildcards

@Composable
public fun <T : Any> LazyPagingColumn(
    data: LazyPagingItems<T>,
    modifier: Modifier = Modifier.fillMaxSize(),
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(8.dp),
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    beforePrepend: LazyListScope.() -> Unit = {},
    beforeItems: LazyListScope.() -> Unit = {},
    afterItems: LazyListScope.() -> Unit = {},
    afterAppend: LazyListScope.() -> Unit = {},
    getRefreshErrorMsg: (Throwable) -> String? = { null },
    getAddErrorMsg: (Throwable) -> String? = { null }, itemKey: ((T) -> Any)? = null,
    itemContentType: ((item: @JvmSuppressWildcards T) -> Any?)? = null,
    localization: LazyPagingColumnLocalization = LazyPagingColumnLocalization(),
    content: @Composable (T) -> Unit) {
    LazyColumn(
        modifier,
        state,
        contentPadding,
        reverseLayout,
        verticalArrangement,
        horizontalAlignment,
        flingBehavior,
        userScrollEnabled,
    ) {
        beforePrepend()
        when (data.loadState.prepend) {
            Loading -> addLoading()

            is Error -> {
                getAddErrorMsg((data.loadState.prepend as Error).error)?.let { text ->
                    addLoadError(
                        message = text,
                        onClickRetry = data::retry,
                    )
                }
            }

            else -> Unit
        }

        beforeItems()

        when (data.loadState.refresh) {
            is NotLoading -> {
                if (data.itemCount > 0) {
                    items(
                        data.itemCount,
                        itemKey?.let(data::itemKey),
                        data.itemContentType(itemContentType),
                    ) { index ->
                        data[index]?.let { content(it) }
                    }
                }
                else {
                    localization.noItems?.let { text ->
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(1f),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = text,
                                    modifier = Modifier.align(Alignment.Center),
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }
                }
            }

            Loading -> {
                item {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(20.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            color = Color.Red,
                        )
                    }
                }
            }

            is Error -> {
                getRefreshErrorMsg((data.loadState.refresh as Error).error)?.let { text ->
                    refreshLoadError(
                        message = text,
                        onClickRetry = data::retry,
                        modifier = Modifier.fillMaxWidth(1f),
                    )
                }
            }
        }

        afterItems()

        when (data.loadState.append) {
            Loading -> addLoading()

            is Error -> {
                getAddErrorMsg((data.loadState.append as Error).error)?.let { text ->
                    addLoadError(
                        message = text,
                        onClickRetry = data::retry,
                    )
                }
            }

            else -> Unit
        }

        afterAppend()
    }
}
