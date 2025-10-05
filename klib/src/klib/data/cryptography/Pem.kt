package klib.data.cryptography

import dev.whyoleg.cryptography.serialization.pem.Pem
import dev.whyoleg.cryptography.serialization.pem.PemContent
import dev.whyoleg.cryptography.serialization.pem.PemLabel

public fun ByteArray.encodePEM(label: String): String =
    Pem.encode(
        PemContent(
            label = PemLabel(label),
            bytes = this,
        ),
    )

public fun String.decodePEM(): PemContent = Pem.decode(this)
