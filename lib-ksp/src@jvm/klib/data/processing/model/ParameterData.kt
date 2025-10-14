package klib.data.processing.model

import com.squareup.kotlinpoet.ParameterSpec
import klib.data.processing.model.annotations.ParameterAnnotation

public data class ParameterData(
    val name: String,
    val annotations: List<ParameterAnnotation> = emptyList(),
    val type: ReturnTypeData,
) {

    public inline fun <reified T : ParameterAnnotation> findAnnotationOrNull(): T? = this.annotations.firstOrNull { it is T } as? T

    public inline fun <reified T : ParameterAnnotation> hasAnnotation(): Boolean = this.findAnnotationOrNull<T>() != null

    public fun parameterSpec(): ParameterSpec {
        val parameterType = this.type.typeName ?: throw IllegalStateException("Type ${this.name} not found")
        return ParameterSpec(this.name, parameterType)
    }
}
