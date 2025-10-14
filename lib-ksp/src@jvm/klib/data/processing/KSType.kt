package klib.data.processing

import com.google.devtools.ksp.symbol.KSType

public fun KSType?.resolveTypeName(): String {
    // TODO: Find better way to handle type alias Types
    return this.toString().removePrefix("[typealias ").removeSuffix("]")
}
