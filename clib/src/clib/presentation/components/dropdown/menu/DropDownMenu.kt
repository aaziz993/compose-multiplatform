package clib.presentation.components.dropdown.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import clib.presentation.components.dropdown.menu.model.MenuItem

@Suppress("ComposeModifierMissing")
@Composable
public fun DropDownMenu(
    items: List<MenuItem>,
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.elevatedShape,
    colors: CardColors = CardDefaults.elevatedCardColors(),
    elevation: CardElevation = CardDefaults.elevatedCardElevation(),
): Unit = ElevatedCard(
    modifier,
    shape,
    colors,
    elevation,
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        items.forEachIndexed { index, (title, leadingIcon, trailingIcon, enabled, onAction) ->
            if (index > 0) HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            DropdownMenuItem(
                text = title,
                onClick = onAction,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                enabled = enabled,
                colors = MenuDefaults.itemColors(
                    textColor = MaterialTheme.colorScheme.onSurface,
                    leadingIconColor = MaterialTheme.colorScheme.onSurface,
                    trailingIconColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
        }
    }
}
