package clib.presentation.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import clib.presentation.components.loading.LoadingCircle
import clib.presentation.viewmodel.model.exception.ViewModelStateException

@Immutable
public sealed interface ViewModelState<T> {

    public data class Idle<T>(val exception: ViewModelStateException? = null) : ViewModelState<T>

    public data class Loading<T>(val data: T) : ViewModelState<T>

    public data class Success<T>(val data: T) : ViewModelState<T>

    public data class Failure<T>(val data: T, val exception: ViewModelStateException) : ViewModelState<T>

    public fun toLoading(): ViewModelState<T> =
        when (this) {
            is Idle -> Idle()
            is Loading -> Loading(data)
            is Success -> Loading(data)
            is Failure -> Loading(data)
        }

    public fun toSuccess(exception: ViewModelStateException): ViewModelState<T> =
        when (this) {
            is Idle -> Idle(exception)
            is Loading -> Success(data)
            is Success -> Success(data)
            is Failure -> Success(data)
        }

    public fun toFailure(exception: ViewModelStateException): ViewModelState<T> =
        when (this) {
            is Idle -> Idle(exception)
            is Loading -> Failure(data, exception)
            is Success -> Failure(data, exception)
            is Failure -> Failure(data, exception)
        }

    public fun onIdle(
        elseBlock: (ViewModelState<T>) -> Unit = {},
        block: (ViewModelStateException?) -> Unit = {},
    ): ViewModelState<T> = also {
        if (it is Idle) block(it.exception) else elseBlock(it)
    }

    public fun onLoading(
        elseBlock: (ViewModelState<T>) -> Unit = {},
        block: (T) -> Unit = {},
    ): ViewModelState<T> = also {
        if (it is Loading) block(it.data) else elseBlock(it)
    }

    public fun onSuccess(
        elseBlock: (ViewModelState<T>) -> Unit = {},
        block: (T) -> Unit = {},
    ): ViewModelState<T> = also {
        if (it is Success) block(it.data) else elseBlock(it)
    }

    public fun onFailure(
        elseBlock: (ViewModelState<T>) -> Unit = { },
        block: (data: T, throwable: Throwable) -> Unit = { _, _ -> },
    ): ViewModelState<T> = also {
        if (it is Failure) block(it.data, it.exception) else elseBlock(it)
    }

    @Composable
    public fun onLoadingComposable(
        elseBlock: @Composable (ViewModelState<T>) -> Unit = {},
        block: @Composable (T) -> Unit = { LoadingCircle() },
    ): ViewModelState<T> = also {
        if (it is Loading) block(it.data) else elseBlock(it)
    }

    @Composable
    public fun onSuccessComposable(
        elseBlock: @Composable (ViewModelState<T>) -> Unit = {},
        block: @Composable (T) -> Unit = {},
    ): ViewModelState<T> = also {
        if (it is Success) block(it.data) else elseBlock(it)
    }

    @Composable
    public fun onFailureComposable(
        elseBlock: @Composable (ViewModelState<T>) -> Unit = { },
        block: @Composable (data: T, throwable: Throwable) -> Unit = { _, _ -> },
    ): ViewModelState<T> = also {
        if (it is Failure) block(it.data, it.exception) else elseBlock(it)
    }
}

public fun <T> idle(exception: ViewModelStateException? = null): ViewModelState<T> = ViewModelState.Idle(exception)

public fun <T> loading(data: T): ViewModelState.Loading<T> = ViewModelState.Loading(data)

public fun <T> success(data: T): ViewModelState.Success<T> = ViewModelState.Success(data)

public fun <T> failure(data: T, exception: ViewModelStateException): ViewModelState.Failure<T> =
    ViewModelState.Failure(data, exception)
