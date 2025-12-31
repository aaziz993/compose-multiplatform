package klib.data.database.mdb

import kotlinx.io.IOException
import kotlinx.io.Sink

public actual class OleBlob {

    @Throws(IOException::class)
    public actual fun writeTo(out: Sink) {
    }

    public actual val content: Content
        get() = TODO("Not yet implemented")
}
