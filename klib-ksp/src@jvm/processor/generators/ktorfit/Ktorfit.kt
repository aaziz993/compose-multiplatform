package processor.generators.ktorfit

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import de.jensklingenberg.ktorfit.KtorfitOptions
import de.jensklingenberg.ktorfit.model.ClassData
import de.jensklingenberg.ktorfit.poetspec.createFileSpec
import de.jensklingenberg.ktorfit.poetspec.getImplClassSpec
import klib.data.processing.writeToOrOverride
import processor.CompilerOptions

/**
 * Generate the Impl class for every interface used for Ktorfit
 */
@OptIn(KspExperimental::class)
public fun generateImplClass(
    classDataList: List<ClassData>,
    codeGenerator: CodeGenerator,
    resolver: Resolver,
    ktorfitOptions: KtorfitOptions,
    options: CompilerOptions
) {
    classDataList.forEach { classData ->
        with(classData) {
            val implClassSpec = classData.getImplClassSpec(resolver, ktorfitOptions)

            val fileSource =
                createFileSpec(
                    classData,
                    classData.implName,
                    implClassSpec,
                )

            val commonMainModuleName = "commonMain"
            val moduleName =
                try {
                    resolver.getModuleName().getShortName()
                }
                catch (_: Throwable) {
                    ""
                }

            if (!ktorfitOptions.multiplatformWithSingleTarget) {
                if (moduleName.contains(commonMainModuleName)) {
                    if (!ksFile.filePath.contains(options.commonMainKotlinSrc)) return@forEach
                }
                else {
                    if (ksFile.filePath.contains(options.commonMainKotlinSrc)) return@forEach
                }
            }

//            fileSource.writeToOrOverride(codeGenerator, Dependencies(false, ksFile))
        }
    }
}
