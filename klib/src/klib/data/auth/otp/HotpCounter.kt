package klib.data.auth.otp

import kotlin.contracts.contract

public typealias HotpCounter = Long

public fun HotpCounter?.isValid(): Boolean {
    contract {
        returns(true) implies (this@isValid != null)
    }
    return this != null && this >= 0
}
