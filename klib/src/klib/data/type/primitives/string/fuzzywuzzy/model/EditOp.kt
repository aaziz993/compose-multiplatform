package klib.data.type.primitives.string.fuzzywuzzy.model

public data class EditOp(
    var type: EditType? = null,
    var spos: Int = 0, // source block pos
    var dpos: Int = 0 // destination block pos
) {

    override fun toString(): String {
        return type!!.name + "(" + spos + "," + dpos + ")"
    }
}
