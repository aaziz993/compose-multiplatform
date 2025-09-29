package clib.data.crud.model

import clib.ui.presentation.components.textfield.search.model.SearchFieldCompare
import clib.ui.presentation.components.textfield.search.model.SearchFieldState
import klib.data.type.BooleanVariable
import klib.data.type.f
import klib.data.type.reflection.kClass
import klib.data.type.reflection.parsePrimitiveOrNull
import klib.data.type.serialization.primitiveType
import klib.data.type.serialization.primitiveTypeOrNull
import klib.data.type.validator.Validator
import kotlin.reflect.typeOf
import kotlinx.serialization.descriptors.SerialDescriptor

public data class ExposedProperty(
    override val name: String,
    override val descriptor: SerialDescriptor,
    override val isId: Boolean = false,
    override val isReadOnly: Boolean,
    override val validator: Validator?
) : EntityProperty {

    override fun predicate(state: SearchFieldState): BooleanVariable? = when {
        descriptor.primitiveTypeOrNull == typeOf<String>() -> {
            if (state.compareMatch == SearchFieldCompare.NOT_EQUALS) {
                name.f.neq(state.query)
            }
            else if (state.regexMatch) {
                name.f.eqPattern(
                    state.query,
                    state.ignoreCase,
                    state.matchAll,
                )
            }
            else {
                name.f.eq(
                    state.query,
                    state.ignoreCase,
                    state.matchAll,
                )
            }
        }

        else -> {
            if (state.compareMatch == SearchFieldCompare.BETWEEN) {
                val left = state.query.substringBefore("").ifEmpty { null }
                    ?.let(descriptor.primitiveType.kClass::parsePrimitiveOrNull)
                val right = state.query.substringAfter("").ifEmpty { null }
                    ?.let(descriptor.primitiveType.kClass::parsePrimitiveOrNull)
                if (!(left == null || right == null)) {
                    name.f.between(left, right)
                }
                else {
                    null
                }
            }
            else {
                descriptor.primitiveType.kClass.parsePrimitiveOrNull(state.query)?.let {
                    when (state.compareMatch) {
                        SearchFieldCompare.GREATER_THAN -> name.f.gt(it)
                        SearchFieldCompare.GREATER_THAN_EQUAL -> name.f.gte(it)
                        SearchFieldCompare.EQUALS -> name.f.eq(it)
                        SearchFieldCompare.LESS_THAN -> name.f.lt(it)
                        SearchFieldCompare.LESS_THAN_EQUAL -> name.f.lte(it)
                        SearchFieldCompare.NOT_EQUALS -> name.f.neq(it)
                        else -> throw IllegalStateException()
                    }
                }
            }
        }
    }
}
