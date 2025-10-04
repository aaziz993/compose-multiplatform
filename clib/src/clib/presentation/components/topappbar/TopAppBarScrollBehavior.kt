package clib.presentation.components.topappbar

import androidx.compose.material3.TopAppBarScrollBehavior
import clib.presentation.FabNestedScrollConnection
import clib.presentation.asFabNestedScrollConnection

public fun TopAppBarScrollBehavior.fabNestedScrollConnection(threshold: Int = 1): FabNestedScrollConnection =
    nestedScrollConnection.asFabNestedScrollConnection(threshold)
