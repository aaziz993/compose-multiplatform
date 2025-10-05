package klib.data.js

import kotlinx.cinterop.CValuesRef
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSString
import platform.JavaScriptCore.JSContext
import platform.JavaScriptCore.setObject
import platform.Security.SecRandomCopyBytes
import platform.Security.errSecSuccess
import platform.Security.kSecRandomDefault

@Suppress("CAST_NEVER_SUCCEEDS")
internal fun JSContext.loadCrypto() {
    val getRandomValues: (signed: Boolean, bits: Double, count: Double) -> List<*> = { signed, bits, count ->
        if (signed) {
            when (bits.toInt()) {
                8 -> ByteArray(count.toInt()).getRandomValues().toList()

                16 -> ShortArray(count.toInt()).getRandomValues().toList()

                32 -> IntArray(count.toInt()).getRandomValues().toList()

                64 -> LongArray(count.toInt()).getRandomValues().toList()

                else -> throw IllegalArgumentException("Unknown bits size: $bits")
            }
        }
        else {
            when (bits.toInt()) {
                8 -> UByteArray(count.toInt()).getRandomValues().toList()

                16 -> UShortArray(count.toInt()).getRandomValues().toList()

                32 -> UIntArray(count.toInt()).getRandomValues().toList()

                64 -> ULongArray(count.toInt()).getRandomValues().toList()

                else -> throw IllegalArgumentException("Unknown bits size: $bits")
            }
        }
    }

    setObject(getRandomValues, "_getRandomValues" as NSString)

    evaluateScript(
        """
        var crypto = {
            getRandomValues: (array) => {
	            var type;
                const str= Object.prototype.toString.call(array)
                switch (str) {
                     case '[object Uint8Array]':
                     type= [false, 8];
                     break;
                     case '[object Uint16Array]':
                     type=[false, 16];
                     break;
                     case '[object Uint32Array]':
                     type=[false, 32];
                     break;
                     default:
                     	switch(str){
                         	case '[object Int8Array]':
                     		type= [true, 8];
                     		break;
                     		case '[object Int16Array]':
                            type=[true, 16];
                            break;
                            case '[object Int32Array]':
                            type=[true, 32];
                            break;
                            default:
                     	        throw new TypeError('crypto.getRandomValues: Expected a TypedArray');
                        }
                }

                const randomArray=_getRandomValues(type[0], type[1], array.length);
                for (var i = 0; i < array.length; i++) {
                    array[i] = randomArray[i];
                }
            }
        }
    """.trimIndent(),
    )
}

private fun getRandomValues(byteSize: Int, bytes: CValuesRef<*>?) {

    val result = SecRandomCopyBytes(kSecRandomDefault, byteSize.toULong(), bytes)

    if (result != errSecSuccess) {
        throw IllegalStateException("Failed to generate secure random numbers")
    }
}

private fun ByteArray.getRandomValues(): ByteArray = apply {
    usePinned {
        getRandomValues(size * Byte.SIZE_BYTES, it.addressOf(0))
    }
}

private fun ShortArray.getRandomValues(): ShortArray = apply {
    usePinned {
        getRandomValues(size * Short.SIZE_BYTES, it.addressOf(0))
    }
}

private fun IntArray.getRandomValues(): IntArray = apply {
    usePinned {
        getRandomValues(size * Int.SIZE_BYTES, it.addressOf(0))
    }
}

private fun LongArray.getRandomValues(): LongArray = apply {
    usePinned {
        getRandomValues(size * Long.SIZE_BYTES, it.addressOf(0))
    }
}

private fun UByteArray.getRandomValues(): UByteArray = apply {
    usePinned {
        getRandomValues(size * UByte.SIZE_BYTES, it.addressOf(0))
    }
}

private fun UShortArray.getRandomValues(): UShortArray = apply {
    usePinned {
        getRandomValues(size * UShort.SIZE_BYTES, it.addressOf(0))
    }
}

private fun UIntArray.getRandomValues(): UIntArray = apply {
    usePinned {
        getRandomValues(size * UInt.SIZE_BYTES, it.addressOf(0))
    }
}

private fun ULongArray.getRandomValues(): ULongArray = apply {
    usePinned {
        getRandomValues(size * ULong.SIZE_BYTES, it.addressOf(0))
    }
}
