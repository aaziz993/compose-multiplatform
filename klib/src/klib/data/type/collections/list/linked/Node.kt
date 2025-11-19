package klib.data.type.collections.list.linked

import kotlinx.serialization.Serializable

@Serializable
public data class Node<T>(
    var data: T,
    var prev: Node<T>? = null,
    var next: Node<T>? = null,
)
