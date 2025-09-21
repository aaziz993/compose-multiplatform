/**
 *         Copyright 2025 Aziz Atoev
 * 
 * Licensed under the [project_license_name] (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *         Apache License, Version 2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import java.io.OutputStream

public class CompilerProcessor(
  private val options: Map<String, String>,
  private val logger: KSPLogger,
  private val codeGenerator: CodeGenerator,
) : SymbolProcessor {

  private var imports = mutableSetOf<String>()

  public operator fun OutputStream.plusAssign(str: String) {
    this.write(str.toByteArray())
  }

  override fun process(resolver: Resolver): List<KSAnnotated> {
    logger.info("Processing compilation graph...")

    val compiledFile = codeGenerator.getFile("ai.tech.core.type", "Compiled")

    compiledFile += "package ai.tech.core.type\n\n"

    compiledFile += imports.joinToString("\n", postfix = "\n\n") { "import $it" }

    compiledFile.close()

    return emptyList()
  }

  private inner class CompilerVisitor : KSVisitorVoid() {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {}
  }

  private fun CodeGenerator.getFile(packageName: String, fileName: String): OutputStream =
    try {
      createNewFile(Dependencies.ALL_FILES, packageName, fileName)
    } catch (ex: FileAlreadyExistsException) {
      ex.file.outputStream()
    }
}
