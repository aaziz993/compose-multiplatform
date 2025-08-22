package klib.data.type.collections.list

@Suppress("UNCHECKED_CAST")
public val Any.asNullableListOrNull: List<Any?>?
    get() = this as? List<Any?>

public val Any.asNullableList: List<Any?>
    get() = asNullableListOrNull!!

@Suppress("UNCHECKED_CAST")
public val Any.asListOrNull: List<Any>?
    get() = this as? List<Any>

public val Any.asList: List<Any>
    get() = asListOrNull!!

@Suppress("UNCHECKED_CAST")
public fun <E> Any.asListOrNull(): List<E>? = this as? List<E>

public fun <E> Any.asList(): List<E> = asListOrNull()!!

public inline fun <E> List<E>.withDefault(defaultValue: (Int) -> E): List<E> =
    mapIndexed { index, element -> element ?: defaultValue(index) }

public infix fun <E> List<E>.slice(indices: Iterable<Int>): List<E> = indices.map(::get)

public infix fun <E> Iterable<E>.minusIndices(indices: Iterable<Int>): List<E> = filterIndexed { index, _ ->
    index !in indices
}

public fun <E> List<E>.drop(): List<E> = drop(1)

public fun <E> List<E>.dropLast(): List<E> = dropLast(1)

public fun <E> List<E>.subListTo(toIndex: Int): List<E> = subList(0, toIndex)

public fun <E> List<E>.subListFrom(fromIndex: Int): List<E> = subList(fromIndex, size)

public fun <T> zip(vararg lists: List<T>): List<T> =
    List(lists[0].size * lists.size) {
        lists[it % lists.size][it / lists.size]
    }

public fun <T> List<T>.unzip(size: Int): List<List<T>> =
    List(size) { offset ->
        (indices step size).map { this[offset + it] }
    }
