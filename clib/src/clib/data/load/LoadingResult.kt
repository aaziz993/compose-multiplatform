package clib.data.load

import androidx.compose.runtime.Composable
import clib.presentation.components.loading.LoadingCircle
import klib.data.load.LoadingResult
import klib.data.load.LoadingResult.Loading
import klib.data.load.LoadingResult.Success
import klib.data.load.LoadingResult.Failure
import kotlin.also

@Suppress("ComposeUnstableReceiver")
@Composable
public fun <T> LoadingResult<T>.onLoadingComposable(
    other: @Composable (LoadingResult<T>) -> Unit = {},
    block: @Composable (T) -> Unit = { LoadingCircle() },
): LoadingResult<T> = also { result ->
    if (result is Loading) block(result.value) else other(result)
}

@Suppress("ComposeUnstableReceiver")
@Composable
public fun <T> LoadingResult<T>.onSuccessComposable(
    other: @Composable (LoadingResult<T>) -> Unit = {},
    block: @Composable (T) -> Unit = {},
): LoadingResult<T> = also { result ->
    if (result is Success) block(result.value) else other(result)
}

@Suppress("ComposeUnstableReceiver")
@Composable
public fun <T> LoadingResult<T>.onFailureComposable(
    other: @Composable (LoadingResult<T>) -> Unit = { },
    block: @Composable (data: T, throwable: Throwable) -> Unit = { _, _ -> },
): LoadingResult<T> = also { result ->
    if (result is Failure) block(result.value, result.throwable) else other(result)
}
