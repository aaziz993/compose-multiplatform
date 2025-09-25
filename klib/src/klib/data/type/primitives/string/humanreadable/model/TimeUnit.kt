package klib.data.type.primitives.string.humanreadable.model

public data class TimeUnit(
    val past: (quantity: Int) -> String,
    val present: (quantity: Int) -> String,
    val future: (quantity: Int) -> String
) {

    internal fun format(quantity: Int, relativeTime: RelativeTime): String {
        return when (relativeTime) {
            RelativeTime.Past -> past(quantity).ifEmpty { present(quantity) }
            RelativeTime.Present -> present(quantity)
            RelativeTime.Future -> future(quantity).ifEmpty { present(quantity) }
        }
    }
}

