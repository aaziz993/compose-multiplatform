package klib.data.type.primitives.string.fuzzywuzzy.model

public data class MatchingBlock(
    var spos: Int = 0,
    var dpos: Int = 0,
    var length: Int = 0
){

    override fun toString(): String {
        return "($spos,$dpos,$length)"
    }
}
