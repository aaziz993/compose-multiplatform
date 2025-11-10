package ui.navigation.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.koin.core.annotation.Single

@Single
public class DrawerStateHolder(isOpen: Boolean = true) {

    public var isOpen: Boolean by mutableStateOf(isOpen)
        private set

    public fun toggle() {
        isOpen = !isOpen
    }
}
