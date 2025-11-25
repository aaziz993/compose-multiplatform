package clib.presentation.density

import androidx.compose.ui.unit.Density
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

public class DensityState(initialValue: Density? = null) {

    public var density: Density? by mutableStateOf(initialValue)

    public companion object Companion {

        public val Saver: Saver<DensityState, *> = listSaver(
            save = { listOf(it.density) },
            restore = { DensityState(it[0]) },
        )
    }
}

@Composable
public fun rememberDensityState(initialValue: Density? = null): DensityState =
    rememberSaveable(saver = DensityState.Saver) { DensityState(initialValue) }
