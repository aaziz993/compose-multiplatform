package clib.presentation.navigation.scene

import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import androidx.navigation3.scene.SinglePaneSceneStrategy

public class DelegatedScreenStrategy<T : Any>(
    private val strategyMap: Map<String, SceneStrategy<T>>,
    private val fallbackStrategy: SceneStrategy<T> = SinglePaneSceneStrategy()
) : SceneStrategy<T> {

    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val lastEntry = entries.lastOrNull() ?: return null

        // Check all keys in metadata.
        val strategies = lastEntry.metadata.keys.mapNotNull { key -> strategyMap[key] }

        return with(
            if (strategies.isEmpty()) fallbackStrategy
            else strategies.reduce { left, right -> left.then(right) },
        ) {
            calculateScene(entries)
        }
    }
}
