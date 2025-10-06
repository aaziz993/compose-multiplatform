package klib.data.processing.model

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import klib.data.processing.model.annotations.FunctionAnnotation

public open class FunctionData(
    public val name: String,
    public val annotations: List<AnnotationSpec>,
    public val functionAnnotations: List<FunctionAnnotation> = emptyList(),
    public val modifiers: List<KModifier> = emptyList(),
    public val isSuspend: Boolean = false,
    public val parameters: List<ParameterData>,
    public val returnType: ReturnTypeData,
    public val imports: Set<String> = emptySet(),
)

public fun FileSpec.Companion.builder(functionData: FunctionData): FunSpec.Builder {
    val returnTypeName = functionData.returnType.typeName ?: throw IllegalStateException("Return type not found")

    return FunSpec
        .builder(functionData.name)
        .addModifiers(functionData.modifiers)
        .addAnnotations(functionData.annotations)
        .addParameters(functionData.parameters.map(ParameterData::parameterSpec))
        .returns(returnTypeName)
}
