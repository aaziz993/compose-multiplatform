package clib.presentation.navigation.scene

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import kotlin.collections.Map

public class WrapperScene<T : Any>(
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>,
    private val entry: NavEntry<T>,
    private val wrapper: @Composable (content: @Composable () -> Unit) -> Unit,
) : Scene<T> {

    override val entries: List<NavEntry<T>> = listOf(entry)

    override val content: @Composable (() -> Unit) = {
        wrapper {
            entry.Content()
        }
    }
}

@Stable
public abstract class WrapperSceneStrategy<T : Any> : SceneStrategy<T> {

    protected abstract val key: String

    @Composable
    protected abstract fun Content(content: @Composable () -> Unit)

    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val lastEntry = entries.lastOrNull()?.takeIf { entry -> entry.metadata[key] == true } ?: return null

        // We use the list's contentKey to uniquely identify the scene.
        val sceneKey = lastEntry.contentKey

        // Return special Scene for NavigationSuiteScaffold.
        return WrapperScene(
            sceneKey,
            entries.dropLast(1),
            lastEntry,
        ) { content ->
            Content(content)
        }
    }

    public companion object Companion {

        // Helper function to create metadata.
        public fun wrapper(key: String): Map<String, Boolean> = mapOf(key to true)
    }
}
