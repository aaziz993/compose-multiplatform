package klib.data.type.serialization.serializers.bignum

import com.ionspin.kotlin.bignum.integer.BigInteger
import klib.data.type.serialization.serializers.primitive.PrimitiveStringSerializer
import kotlinx.serialization.Serializable

public object BigIntegerSerializer :
    PrimitiveStringSerializer<BigInteger>(
        "com.ionspin.kotlin.bignum.integer.BigInteger",
        BigInteger::toString,
        BigInteger::parseString,
    )

public typealias BigIntegerSerial = @Serializable(with = BigIntegerSerializer::class) BigInteger
