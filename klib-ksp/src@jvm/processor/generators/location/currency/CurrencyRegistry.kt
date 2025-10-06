@file:OptIn(KspExperimental::class)

package processor.generators.location.currency

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.CodeGenerator
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import java.io.File
import klib.data.processing.Logger
import klib.data.processing.model.ClassData
import klib.data.processing.model.CompilerClass
import klib.data.processing.writeToWithOverride
import kotlinx.serialization.json.Json
import processor.CompilerOptions

public fun generateCurrencyRegistry(
    logger: Logger,
    codeGenerator: CodeGenerator,
    options: CompilerOptions
) {

    val file = File(options.kspResourcesDir).resolve("iso/currencies.json")
    if (!file.exists()) {
        logger.error("Currencies file not found at '$file'")
        return
    }

    logger.info("Generating CurrencyRegistry...")

    val currencies: List<Currency> = Json.decodeFromString(file.readText())

    val classData = ClassData(
        name = "CurrencyRegistry",
        packageName = "klib.data.location.currency",
        imports = setOf(
            "klib.data.location.currency.Currency",
            "klib.data.iso.Alpha3Letter",
        ),
    )

    val items = currencies.map { currency ->
        CodeBlock.builder().apply {
            add("Alpha3Letter(%S) to {\n", currency.demonym)
            indent()
            add("Currency(\n")
            indent()
            currency.name?.let { add("name = %S,\n", it) }
            add("demonym = %S,\n", currency.demonym)
            currency.majorSingle?.let { add("majorSingle = %S,\n", it) }
            currency.majorPlural?.let { add("majorPlural = %S,\n", it) }
            add("ISOnum = %L,\n", currency.ISOnum)
            currency.symbol?.let { add("symbol = %S,\n", it) }
            currency.symbolNative?.let { add("symbolNative = %S,\n", it) }
            currency.minorSingle?.let { add("minorSingle = %S,\n", it) }
            currency.minorPlural?.let { add("minorPlural = %S,\n", it) }
            add("ISOdigits = %L,\n", currency.ISOdigits)
            currency.decimals?.let { add("decimals = %L,\n", it) }
            currency.numToBasic?.let { add("numToBasic = %L,\n", it) }

            unindent()
            add(")\n")
            unindent()
            add("}")
        }.build()
    }

    val mapSpec = PropertySpec.builder(
        "currencies",
        Map::class.asClassName().parameterizedBy(
            CompilerClass("Alpha3Letter", "klib.data.iso", "").toClassName(),
            LambdaTypeName.get(returnType = CompilerClass("Currency", "klib.data.location.currency", "").toClassName()),
        ),
    ).initializer(
        CodeBlock.builder().apply {
            add("mapOf(\n")
            indent()
            items.forEach { add("%L,\n", it) }
            unindent()
            add(")")
        }.build(),
    ).addModifiers(KModifier.PUBLIC).build()

    val fileSpec = FileSpec.builder(classData.packageName, classData.name)
        .addType(
            TypeSpec.objectBuilder("CurrencyRegistry")
                .addProperty(mapSpec)
                .build(),
        )
        .build()


    fileSpec.writeToWithOverride(codeGenerator, aggregating = false)
}
