package klib.processing.model

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSTypeReference
import com.squareup.kotlinpoet.KModifier

/**
 * @param name of the interface that contains annotations
 * @param superClasses List of qualifiedNames of interface that a Ktorfit interface extends
 */
public data class ClassData(
    val name: String,
    val packageName: String,
    val imports: Set<String>,
    val annotations: List<KSAnnotation>,
    val modifiers: List<KModifier> = emptyList(),
    val superClasses: List<KSTypeReference> = emptyList(),
    val properties: List<KSPropertyDeclaration> = emptyList(),
    val functions: List<FunctionData>,
    val ksFile: KSFile,
) {

    public val implName: String = "_${name}Impl"
    public val providerName: String = "_${name}Provider"
}
