package klib.data.processing

import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.ksp.toKModifier
import com.squareup.kotlinpoet.ksp.toTypeName
import klib.data.processing.model.ClassData
import klib.data.processing.model.FunctionData

/**
 * Convert a [KSClassDeclaration] to [ClassData]
 * @param logger used to log errors
 * @return the transformed classdata
 */
public fun KSClassDeclaration.toClassData(
    logger: KSPLogger,
    imports: Set<String>,
    transform: KSFunctionDeclaration.(KSPLogger) -> FunctionData,
): ClassData {
    val ksClassDeclaration = this
    val packageName = ksClassDeclaration.packageName.asString()
    val className = ksClassDeclaration.simpleName.asString()

    val functionDataList: List<FunctionData> =
        ksClassDeclaration
            .getDeclaredFunctions()
            .toList()
            .map { funcDeclaration ->
                return@map funcDeclaration.transform(logger)
            }

    val filteredSupertypes =
        ksClassDeclaration.superTypes.toList().filterNot {
            /** In KSP Any is a supertype of an interface */
            it.toTypeName() == ANY
        }
    val properties = ksClassDeclaration.getDeclaredProperties().toList()

    return ClassData(
        name = className,
        packageName = packageName,
        functions = functionDataList,
        imports = imports + functionDataList.flatMap(FunctionData::imports),
        superClasses = filteredSupertypes,
        properties = properties,
        modifiers = ksClassDeclaration.modifiers.mapNotNull { it.toKModifier() },
        ksFile = ksClassDeclaration.getKsFile(),
        annotations = ksClassDeclaration.annotations.toList(),
    )
}
