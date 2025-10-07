package klib.data.processing

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.kspDependencies
import com.squareup.kotlinpoet.ksp.originatingKSFiles
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

public fun FileSpec.Builder.addImports(imports: Set<String>): FileSpec.Builder {
    imports.forEach {
        /**
         * Wildcard imports are not allowed by KotlinPoet, as a workaround * is replaced with WILDCARDIMPORT, and it will be replaced again
         * after Kotlin Poet generated the source code
         */
        val packageName = it.substringBeforeLast(".")
        val className = it.substringAfterLast(".")

        this.addImport(packageName, className)
    }
    return this
}

public fun FileSpec.writeToOrOverride(
    codeGenerator: CodeGenerator,
    aggregating: Boolean,
    originatingKSFiles: Iterable<KSFile> = originatingKSFiles(),
) {
    val dependencies = kspDependencies(aggregating, originatingKSFiles)
    writeToOrOverride(codeGenerator, dependencies)
}

public fun FileSpec.writeToOrOverride(
    codeGenerator: CodeGenerator,
    dependencies: Dependencies,
) {
    val file = codeGenerator.createNewFileOrOverride(dependencies, packageName, name)
    OutputStreamWriter(file, StandardCharsets.UTF_8).use(::writeTo)
}
