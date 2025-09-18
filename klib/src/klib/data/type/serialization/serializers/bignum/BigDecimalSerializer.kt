package klib.data.type.serialization.serializers.bignum

import klib.data.type.serialization.serializers.primitive.PrimitiveStringSerializer
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import kotlinx.serialization.Serializable

public object BigDecimalSerializer :
    PrimitiveStringSerializer<BigDecimal>(
        "com.ionspin.kotlin.bignum.decimal.BigDecimal",
        BigDecimal::toString,
        BigDecimal::parseString,
    )

public typealias BigDecimalSerial = @Serializable(with = BigDecimalSerializer::class) BigDecimal
