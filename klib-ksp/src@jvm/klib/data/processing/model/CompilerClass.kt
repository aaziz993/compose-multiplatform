package klib.data.processing.model

import com.squareup.kotlinpoet.ClassName

public data class CompilerClass(
    val name: String,
    val packageName: String,
    val objectName: String
) {

   public fun toClassName(): ClassName = ClassName(packageName, name)
}
