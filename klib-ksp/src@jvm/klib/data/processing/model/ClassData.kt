package klib.data.processing.model

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSTypeReference
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ksp.toAnnotationSpec
import klib.data.processing.addImports

public data class ClassData(
    val comment: String = "",
    val name: String,
    val packageName: String,
    val imports: Set<String> = emptySet(),
    val annotations: List<KSAnnotation> = emptyList(),
    val modifiers: List<KModifier> = emptyList(),
    val superClasses: List<KSTypeReference> = emptyList(),
    val properties: List<KSPropertyDeclaration> = emptyList(),
    val functions: List<FunctionData> = emptyList(),
    val ksFile: KSFile? = null,
)

public fun FileSpec.Companion.builder(classData: ClassData): FileSpec.Builder {
    val suppressAnnotation =
        AnnotationSpec
            .builder(Suppress::class)
            .addMember("\"warnings\"")
            .build()

    return builder(classData.packageName, classData.name)
        .addFileComment(classData.comment)
        .addImports(classData.imports)
        .addAnnotation(suppressAnnotation)
        .addAnnotations(
            classData.annotations.map { ksAnnotation -> ksAnnotation.toAnnotationSpec() },
        )
}
