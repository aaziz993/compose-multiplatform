package klib.data.type.primitives.string.humanreadable.model

/**
 * Indicates in what time frame the requested time unit needs
 * to be localised. Used to support grammar cases in languages like German.
 */
internal enum class RelativeTime {
    Past, Present, Future
}
