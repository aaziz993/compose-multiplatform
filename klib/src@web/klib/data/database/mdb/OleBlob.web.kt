package klib.data.database.mdb

import kotlinx.io.Sink

public actual class OleBlob {

    public actual fun writeTo(out: Sink) {
    }

    public actual val content: Content
        get() = TODO("Not yet implemented")
}
