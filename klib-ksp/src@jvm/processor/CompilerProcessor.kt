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
import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import de.jensklingenberg.ktorfit.KtorfitLogger
import de.jensklingenberg.ktorfit.KtorfitOptions
import de.jensklingenberg.ktorfit.KtorfitProcessor
import de.jensklingenberg.ktorfit.KtorfitProcessor.Companion.ktorfitResolver
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.HEAD
import de.jensklingenberg.ktorfit.http.HTTP
import de.jensklingenberg.ktorfit.http.OPTIONS
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.model.toClassData
import java.io.File
import klib.data.processing.Logger
import processor.generators.ktorfit.generateImplClass
import processor.generators.location.country.generateCountryRegistry
import processor.generators.location.currency.generateCurrencyRegistry
import processor.generators.location.locale.generateLanguageTagRegistry
import processor.generators.location.locale.generateLocaleRegistry

public class CompilerProcessor(
    private val env: SymbolProcessorEnvironment,
    private val options: CompilerOptions,
) : SymbolProcessor {

    private var invoked = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val loggingType = options.errorsLoggingType
        ktorfitResolver = resolver
        if (invoked) return emptyList()
        invoked = true

        val logger = Logger(env.logger, loggingType)
        val codeGenerator = env.codeGenerator

        val commonMainModuleName = "commonMain"
        val moduleName =
            try {
                resolver.getModuleName().getShortName()
            }
            catch (_: Throwable) {
                ""
            }

        if (moduleName.contains(commonMainModuleName)) {
            generateCountryRegistry(logger, codeGenerator, options)
            generateLanguageTagRegistry(logger, codeGenerator, options)
            generateCurrencyRegistry(logger, codeGenerator, options)
            generateLocaleRegistry(logger, codeGenerator, options)
        }

        val classDataList =
            getAnnotatedFunctions(ktorfitResolver)
                .groupBy { it.closestClassDeclaration() }
                .map { (classDec) ->
                    classDec?.toClassData(KtorfitLogger(env.logger, loggingType))
                }.mapNotNull { it }

        generateImplClass(classDataList, env.codeGenerator, resolver, KtorfitOptions(env.options), options)

        return emptyList()
    }

    /**
     * Returns a list of all [KSFunctionDeclaration] which are annotated with a Http Method Annotation
     */
    private fun getAnnotatedFunctions(resolver: Resolver): List<KSFunctionDeclaration> {
        val getAnnotated = resolver.getSymbolsWithAnnotation(GET::class.java.name).toList()
        val postAnnotated = resolver.getSymbolsWithAnnotation(POST::class.java.name).toList()
        val putAnnotated = resolver.getSymbolsWithAnnotation(PUT::class.java.name).toList()
        val deleteAnnotated = resolver.getSymbolsWithAnnotation(DELETE::class.java.name).toList()
        val headAnnotated = resolver.getSymbolsWithAnnotation(HEAD::class.java.name).toList()
        val optionsAnnotated = resolver.getSymbolsWithAnnotation(OPTIONS::class.java.name).toList()
        val patchAnnotated = resolver.getSymbolsWithAnnotation(PATCH::class.java.name).toList()
        val httpAnnotated = resolver.getSymbolsWithAnnotation(HTTP::class.java.name).toList()

        val ksAnnotatedList =
            getAnnotated +
                postAnnotated +
                putAnnotated +
                deleteAnnotated +
                headAnnotated +
                optionsAnnotated +
                patchAnnotated +
                httpAnnotated
        return ksAnnotatedList.filterIsInstance<KSFunctionDeclaration>()
    }
}







