@file:OptIn(KspExperimental::class)

package processor.generators.location.currency

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.CodeGenerator
import com.squareup.kotlinpoet.ClassName
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
import klib.data.processing.model.builder
import klib.data.processing.writeToWithOverride
import kotlinx.serialization.json.Json
import processor.CompilerOptions

// https://github.com/ourworldincode/currency/blob/main/currencies.json
public fun generateCurrencyRegistry(
    logger: Logger,
    codeGenerator: CodeGenerator,
    options: CompilerOptions
) {

    val file = File(options.kspResourcesDir).resolve("iso/currency/currencies.json")
    if (!file.exists()) {
        logger.error("Currencies file not found at '$file'")
        return
    }

    logger.info("Generating CurrencyRegistry...")

    val currencies: Map<String, Currency> = Json.decodeFromString(file.readText())

    val classData = ClassData(
        name = "CurrencyRegistry",
        packageName = "klib.data.location.currency",
        imports = setOf(
            "klib.data.iso.Alpha3Letter",
        ),
    )

    val alpha3Class = ClassName("klib.data.iso", "Alpha3Letter")

    val items = currencies.map { (code, currency) ->
        CodeBlock.builder().apply {
            add("%T(%S) to {\n", alpha3Class, code.lowercase())
            indent()
            add("Currency(\n")
            indent()
            currency.name?.let { add("name = %S,\n", it) }
            add("code = Alpha3Letter(%S),\n", currency.demonym)
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
            ClassName("klib.data.iso", "Alpha3Letter"),
            LambdaTypeName.get(returnType = ClassName("klib.data.location.currency", "Currency")),
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

    val fileSpec = FileSpec.builder(classData)
        .addType(
            TypeSpec.objectBuilder("CurrencyRegistry")
                .addProperty(mapSpec)
                .build(),
        )
        .build()


    fileSpec.writeToWithOverride(codeGenerator, aggregating = false)
}
