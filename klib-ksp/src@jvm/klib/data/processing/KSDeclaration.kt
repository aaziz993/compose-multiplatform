package klib.data.processing

import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFile

public fun KSDeclaration.getKsFile(): KSFile = this.containingFile
    ?: throw Error("Containing File for ${this.simpleName} was null")
