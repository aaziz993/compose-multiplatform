package clib.presentation.theme.density

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import clib.presentation.theme.density.model.DensitySerial

@Suppress("ComposeCompositionLocalUsage")
public val LocalDensityState: ProvidableCompositionLocal<DensityState> =
    staticCompositionLocalOf(::DensityState)

public class DensityState(initialValue: DensitySerial = Density(2f)) {

    public var density: Density by mutableStateOf(initialValue)

    public companion object Companion {

        public val Saver: Saver<DensityState, *> = listSaver(
            save = { listOf(it.density) },
            restore = { DensityState(it[0]) },
        )
    }
}

@Composable
public fun rememberDensityState(initialValue: Density = LocalDensity.current): DensityState =
    rememberSaveable(saver = DensityState.Saver) { DensityState(initialValue) }
