package klib.processing

import com.google.devtools.ksp.symbol.KSAnnotation

public fun <T> KSAnnotation.getArgumentValueByName(name: String): T? = this.arguments.firstOrNull { it.name?.asString() == name }?.value as? T
