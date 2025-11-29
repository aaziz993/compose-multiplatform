package klib.data.type.collections.linkedlist.model

import kotlinx.serialization.Serializable

@Serializable
public data class DataNode<T>(
    var data: T,
    override var prev: DataNode<T>? = null,
    override var next: DataNode<T>? = null,
) : Node<DataNode<T>>
