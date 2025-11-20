package clib.presentation.navigation

import androidx.compose.runtime.Composable
import kotlin.system.exitProcess

@Composable
public actual fun systemOnBack(): () -> Unit = { exitProcess(0) }
