package clib.presentation.components.map

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import clib.presentation.components.map.tile.Tile

@Composable
public fun TilePicker(
    tiles: List<Tile>,
    selectedTile: Tile,
    onTileSelected: (Tile) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) { // <- apply the modifier here
        Button(onClick = { expanded = true }) {
            Text(selectedTile.name)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            tiles.forEach { tile ->
                DropdownMenuItem(
                    text = { Text(tile.name) },
                    onClick = {
                        onTileSelected(tile)
                        expanded = false
                    },
                )
            }
        }
    }
}
