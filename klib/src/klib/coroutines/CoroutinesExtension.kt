package klib.coroutines

import kotlinx.coroutines.CoroutineDispatcher

public interface DispatcherProvider {
    public val main: CoroutineDispatcher
    public val io: CoroutineDispatcher
    public val unconfined: CoroutineDispatcher
}
