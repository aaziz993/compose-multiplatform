package klib.data.type.serialization.serializers.bignum

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import klib.data.type.serialization.serializers.primitive.StringSerializer
import kotlinx.serialization.Serializable

public object BigDecimalSerializer :
    StringSerializer<BigDecimal>(
        "com.ionspin.kotlin.bignum.decimal.BigDecimal",
        BigDecimal::toString,
        BigDecimal::parseString,
    )

public typealias BigDecimalSerial = @Serializable(with = BigDecimalSerializer::class) BigDecimal
