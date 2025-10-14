package klib.data.type.collections

public fun zip(arrays: List<BooleanArray>): BooleanArray =
    BooleanArray(arrays[0].size * arrays.size) {
        arrays[it % arrays.size][it / arrays.size]
    }

public fun BooleanArray.unzip(size: Int): List<BooleanArray> =
    List(size) { offset ->
        BooleanArray(this.size / size) {
            this[offset + it * size]
        }
    }
