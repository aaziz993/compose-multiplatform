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

@file:OptIn(KspExperimental::class)

package processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import klib.data.processing.Logger
import processor.generators.location.country.generateCountryRegistry
import processor.generators.location.locale.generateLanguageTagRegistry

public class CompilerProcessor(
    private val env: SymbolProcessorEnvironment,
    private val options: CompilerOptions,
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val loggingType = options.errorsLoggingType
        val logger = Logger(env.logger, loggingType)
        val codeGenerator = env.codeGenerator

        generateCountryRegistry(logger, codeGenerator, options)
        generateLanguageTagRegistry(logger, codeGenerator, options)

        return emptyList()
    }
}









