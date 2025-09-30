package klib.data.net

import io.ktor.utils.io.*
import klib.data.BUFFER_SIZE
import klib.data.type.collections.iterator.coroutine.ByteArrayClosableCoroutineIterator

// //////////////////////////////////////////////COROUTINEITERATOR//////////////////////////////////////////////////////
public fun ByteReadChannel.iterator(bufferSize: Int = BUFFER_SIZE): ByteArrayClosableCoroutineIterator =
    ByteArrayClosableCoroutineIterator(::readAvailable, ::cancel, bufferSize)
