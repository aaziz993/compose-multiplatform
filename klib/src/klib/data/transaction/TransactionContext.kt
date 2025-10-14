package klib.data.transaction

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

public class TransactionContext(
    public val id: String
) : AbstractCoroutineContextElement(TransactionContext) {

    public companion object Key : CoroutineContext.Key<TransactionContext>
}

public suspend fun currentTransactionId(): String? = coroutineContext[TransactionContext]?.id
