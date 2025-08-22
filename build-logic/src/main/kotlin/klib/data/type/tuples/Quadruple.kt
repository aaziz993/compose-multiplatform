package klib.data.type.tuples

import kotlinx.serialization.Serializable

@Serializable
public data class Quadruple<A,B,C,D>(var first: A, var second: B, var third: C, var fourth: D) {
    override fun toString(): String = "($first, $second, $third, $fourth)"
}