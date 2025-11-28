package clib.presentation.components.textfield.search.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import klib.data.type.primitives.string.matcher

public class SearchFieldState(
    value: String = "",
    matchAll: Boolean = true,
    regexMatch: Boolean = false,
    ignoreCase: Boolean = false,
    compareMatch: SearchFieldCompare = SearchFieldCompare.EQUALS
) {

    public constructor(data: SearchFieldStateData) : this(
        data.value,
        data.matchAll,
        data.regexMatch,
        data.ignoreCase,
        data.compareMatch,
    )

    public var query: String by mutableStateOf(value)

    public var matchAll: Boolean by mutableStateOf(matchAll)

    public var regexMatch: Boolean by mutableStateOf(regexMatch)

    public var ignoreCase: Boolean by mutableStateOf(ignoreCase)

    public var compareMatch: SearchFieldCompare by mutableStateOf(compareMatch)

    public fun matcher(): (String, String) -> Boolean =
        matcher(
            matchAll,
            regexMatch,
            ignoreCase,
        ).let { matcher ->
            if (compareMatch == SearchFieldCompare.NOT_EQUALS) {
                { s1, s2 -> !matcher(s1, s2) }
            }
            else matcher
        }

    public companion object {

        public val Saver: Saver<SearchFieldState, *> = listSaver(
            save = { listOf(it.query, it.matchAll, it.regexMatch, it.ignoreCase, it.compareMatch) },
            restore = {
                SearchFieldState(it[0] as String, it[1] as Boolean, it[2] as Boolean, it[3] as Boolean, it[4] as SearchFieldCompare)
            },
        )
    }
}

@Composable
public fun rememberSearchFieldState(state: SearchFieldState = SearchFieldState()): SearchFieldState =
    rememberSaveable(saver = SearchFieldState.Saver) { state }

@Composable
public fun rememberSearchFieldState(data: SearchFieldStateData): SearchFieldState =
    rememberSearchFieldState(SearchFieldState(data))
