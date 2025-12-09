package klib.data.load

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 *  Data loader class will be in the same file so it can access private parts
 */
public class Refresher {

    public val flow: SharedFlow<Unit>
        field = MutableSharedFlow<Unit>()

    public suspend fun refresh() {
        flow.emit(Unit)
    }
}
