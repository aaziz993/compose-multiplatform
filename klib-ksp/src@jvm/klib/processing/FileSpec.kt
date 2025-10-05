package klib.processing

import com.squareup.kotlinpoet.FileSpec

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
