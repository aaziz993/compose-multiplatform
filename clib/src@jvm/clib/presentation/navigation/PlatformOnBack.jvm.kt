package clib.presentation.navigation

import androidx.compose.runtime.Composable
import kotlin.system.exitProcess

@Composable
public actual fun platformOnBack(): () -> Unit = { exitProcess(0) }
