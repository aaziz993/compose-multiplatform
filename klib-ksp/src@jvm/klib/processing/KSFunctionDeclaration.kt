package klib.processing

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.Modifier

public fun KSFunctionDeclaration.getAnnotationByName(name: String): KSAnnotation? =
    this.annotations.toList().firstOrNull {
        it.shortName.asString() == name
    }


public val KSFunctionDeclaration.isSuspend: Boolean
    get() = (this).modifiers.contains(Modifier.SUSPEND)

