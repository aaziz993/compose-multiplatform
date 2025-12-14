import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import compose_app.generated.resources.Res
import compose_app.generated.resources.app_name
import kotlinx.browser.document
import org.jetbrains.compose.resources.getString
import org.w3c.dom.HTMLLinkElement

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalBrowserHistoryApi
public suspend fun main() {
    document.title = getString(Res.string.app_name)
    setFavicon(Res.getUri("drawable/compose-multiplatform.svg"), "image/svg+xml")
    val body = document.body ?: return
    ComposeViewport(body) {
        App()
    }
}

private fun setFavicon(href: String, type: String = "image/x-icon") {
    var link = document.querySelector("link[rel~='icon']") as HTMLLinkElement?
    if (link == null) {
        link = document.createElement("link") as HTMLLinkElement
        link.rel = "icon"
        document.head?.appendChild(link)
    }
    link.href = href
    link.type = type
}
