package klib.data.processing

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import java.io.OutputStream

public fun CodeGenerator.createNewFileWithOverride(
    dependencies: Dependencies,
    packageName: String,
    fileName: String
): OutputStream {
    return try {
        createNewFile(dependencies, packageName, fileName)
    } catch (ex: FileAlreadyExistsException) {
        ex.file.outputStream()
    }
}
