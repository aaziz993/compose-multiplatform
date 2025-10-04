package clib.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

public fun NestedScrollConnection.asFabNestedScrollConnection(threshold: Int = 1): FabNestedScrollConnection =
    FabNestedScrollConnection(threshold, this)

public class FabNestedScrollConnection(
    private val threshold: Int,
    private val scrollConnection: NestedScrollConnection
) : NestedScrollConnection by scrollConnection {

    public var isFabVisible: Boolean by mutableStateOf(true)
        private set

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        if (available.y < -threshold) isFabVisible = false
        if (available.y > threshold) isFabVisible = true

        return super.onPreScroll(available, source)
    }
}
