package klib.data.processing

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName

public fun AnnotationSpec.toClassName(): ClassName {
    return if (typeName is ClassName) {
        typeName as ClassName
    }
    else {
        (typeName as ParameterizedTypeName).rawType
    }
}
