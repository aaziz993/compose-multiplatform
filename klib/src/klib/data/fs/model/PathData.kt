package klib.data.fs.model

import klib.data.type.collections.iterator.coroutine.CoroutineIterator

public data class PathData(
    val path: String,
    val data: CoroutineIterator<ByteArray>,
)
