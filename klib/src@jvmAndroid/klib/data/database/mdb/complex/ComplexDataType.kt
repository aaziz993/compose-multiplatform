package klib.data.database.mdb.complex

import klib.data.type.collections.bimap.biMapOf

internal val COMPLEX_DATA_TYPES = biMapOf(
    com.healthmarketscience.jackcess.complex.ComplexDataType.ATTACHMENT to ComplexDataType.ATTACHMENT,
    com.healthmarketscience.jackcess.complex.ComplexDataType.MULTI_VALUE to ComplexDataType.MULTI_VALUE,
    com.healthmarketscience.jackcess.complex.ComplexDataType.VERSION_HISTORY to ComplexDataType.VERSION_HISTORY,
    com.healthmarketscience.jackcess.complex.ComplexDataType.UNSUPPORTED to ComplexDataType.UNSUPPORTED
)
