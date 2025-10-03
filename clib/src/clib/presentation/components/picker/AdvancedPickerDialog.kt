package clib.presentation.components.picker

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import clib.presentation.components.picker.model.PickerItem
import clib.presentation.components.textfield.search.AdvancedSearchField
import clib.presentation.components.textfield.search.model.SearchFieldCompare
import clib.presentation.components.textfield.search.model.SearchFieldState
import clib.presentation.components.textfield.search.model.rememberSearchFieldState

@Composable
public fun <T : Any> AdvancedPickerDialog(
    items: List<PickerItem<T>>,
    onItemClick: (PickerItem<T>) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape,
    colors: CardColors = CardDefaults.cardColors(),
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
    searchFieldState: SearchFieldState? = rememberSearchFieldState(),
    searchPlaceholder: (@Composable () -> Unit)? = null,
    searchSingleLine: Boolean = true,
    matchAll: Boolean = true,
    regexMatch: Boolean = true,
    ignoreCase: Boolean = true,
    compareMatchers: List<SearchFieldCompare> = listOf(
        SearchFieldCompare.EQUALS,
        SearchFieldCompare.NOT_EQUALS,
        SearchFieldCompare.LESS_THAN_EQUAL,
        SearchFieldCompare.LESS_THAN,
        SearchFieldCompare.GREATER_THAN,
        SearchFieldCompare.GREATER_THAN_EQUAL,
        SearchFieldCompare.BETWEEN,
    ),
    currentItem: PickerItem<T> = items.first(),
    itemText: (T) -> String = { value -> value.toString() },
    onDismissRequest: () -> Unit
) {
    val matcher: ((String, String) -> Boolean)? = searchFieldState?.matcher()

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            modifier,
            shape,
            colors,
            elevation,
            border,
        ) {
            Column {
                if (searchFieldState != null) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color.White.copy(alpha = 0.1f),
                            ),
                    ) {
                        val equalityMatcher =
                            (searchFieldState.compareMatch == SearchFieldCompare.EQUALS || searchFieldState.compareMatch == SearchFieldCompare.NOT_EQUALS)

                        AdvancedSearchField(
                            searchFieldState,
                            Modifier.fillMaxWidth(),
                            placeholder = searchPlaceholder,
                            singleLine = searchSingleLine,
                            textStyle = TextStyle(textAlign = TextAlign.Start),
                            matchAll = matchAll && equalityMatcher,
                            regexMatch = regexMatch && equalityMatcher,
                            ignoreCase = ignoreCase && equalityMatcher,
                            compareMatchers = compareMatchers,
                        )
                        HorizontalDivider(thickness = 1.dp)
                    }
                }
                LazyColumn {
                    items(
                        (if (searchFieldState?.query?.isEmpty() != false) {
                            items
                        }
                        else {
                            items.filter {
                                matcher!!(itemText(it.value!!), searchFieldState.query)
                            }
                        }),
                    ) { item ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 18.dp,
                                    vertical = 18.dp,
                                )
                                .clickable {
                                    onItemClick(item)
                                    onDismissRequest()
                                },
                        ) {
                            item.icon?.invoke(Modifier)
                            item.text?.invoke(Modifier)
                        }
                    }
                }
            }
        }
    }
}

