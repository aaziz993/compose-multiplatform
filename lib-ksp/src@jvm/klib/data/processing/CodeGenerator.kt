package klib.data.processing

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import java.io.OutputStream

public fun CodeGenerator.createNewFileOrOverride(
    dependencies: Dependencies,
    packageName: String,
    fileName: String,
    extensionName: String = "kt"
): OutputStream {
    return try {
        createNewFile(dependencies, packageName, fileName, extensionName)
    }
    catch (ex: FileAlreadyExistsException) {
        ex.file.outputStream()
    }
}
