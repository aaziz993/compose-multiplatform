package klib.data.type.primitives.string.fuzzywuzzy.model

public data class OpCode(
    var type: EditType? = null,
    var sbeg: Int = 0,
    var send: Int = 0,
    var dbeg: Int = 0,
    var dend: Int = 0
) {

    override fun toString(): String {
        return (type!!.name + "(" + sbeg + "," + send + ","
            + dbeg + "," + dend + ")")
    }
}
