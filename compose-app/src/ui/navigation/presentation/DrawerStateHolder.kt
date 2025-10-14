package ui.navigation.presentation

import clib.presentation.stateholders.BooleanStateHolder
import org.koin.core.annotation.Single

@Single
public class DrawerStateHolder(value: Boolean) : BooleanStateHolder(value)
