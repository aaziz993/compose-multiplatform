package klib.data.type.collections

public fun <T> Iterator<T>.intersperse(separator: () -> T): List<T> = buildList {
    if (this@intersperse.hasNext()) {

        add(this@intersperse.next())

        while (this@intersperse.hasNext()) {
            add(separator())
            add(this@intersperse.next())
        }
    }
}

public fun <T> Iterator<T>.chunked(predicate: (T) -> Boolean): List<List<T>> = buildList {
    var buffer = mutableListOf<T>()

    this@chunked.forEach { element ->
        if (predicate(element)) {
            add(buffer)
            buffer = mutableListOf()
        } else buffer.add(element)
    }

    if (buffer.isNotEmpty()) {
        add(buffer)
    }
}