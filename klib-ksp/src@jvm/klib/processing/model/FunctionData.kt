package klib.processing.model

import com.google.devtools.ksp.processing.Resolver
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeName
import klib.processing.model.annotations.FunctionAnnotation

public open class FunctionData(
    public val name: String,
    public val annotations: List<AnnotationSpec>,
    public val functionAnnotations: List<FunctionAnnotation> = emptyList(),
    public val modifiers: List<KModifier> = emptyList(),
    public val isSuspend: Boolean = false,
    public val parameters: List<ParameterData>,
    public val returnType: ReturnTypeData,
    public val imports: Set<String> = emptySet(),
) {

    public fun toFunSpec(
        resolver: Resolver,
        setQualifiedTypeName: Boolean,
        addBody: FunSpec.Builder.(
            functionData: FunctionData,
            resolver: Resolver,
            setQualifiedTypeName: Boolean,
            returnTypeName: TypeName
        ) -> Unit
    ): FunSpec {
        val returnTypeName = returnType.typeName ?: throw IllegalStateException("Return type not found")

        return FunSpec
            .builder(name)
            .addModifiers(modifiers)
            .addAnnotations(annotations)
            .addParameters(parameters.map(ParameterData::parameterSpec))
            .apply { addBody(this@FunctionData, resolver, setQualifiedTypeName, returnTypeName) }
            .returns(returnTypeName)
            .build()
    }
}
