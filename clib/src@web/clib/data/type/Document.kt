package clib.data.type

import web.dom.Document
import web.dom.Element
import web.html.HTMLLinkElement

public fun Document.favicon(resourcePath: String, type: String = "image/x-icon") {
    // Remove existing favicons
    val links = head.querySelectorAll("link[rel~='icon']")
    links.forEach(Element::remove)

    // Create new link element
    val link = createElement("link") as HTMLLinkElement
    link.rel = "icon"
    link.type = type
    link.href = resourcePath
    head.appendChild(link)
}
