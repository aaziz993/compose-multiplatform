@file:OptIn(KspExperimental::class)

package processor.generators.location.country

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.CodeGenerator
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import java.io.File
import klib.data.processing.Logger
import klib.data.processing.model.ClassData
import klib.data.processing.model.builder
import klib.data.processing.writeToOrOverride
import kotlinx.serialization.json.Json
import processor.CompilerOptions
import processor.generators.location.country.model.Country

public fun generateCountryRegistry(
    logger: Logger,
    codeGenerator: CodeGenerator,
    options: CompilerOptions
) {
    val file = File(options.kspResourcesDir).resolve("iso/country/countries.json")
    if (!file.exists()) {
        logger.error("Countries file not found at '$file'")
        return
    }

    logger.info("Generating CountryRegistry...")

    val countries: List<Country> = Json.decodeFromString(file.readText())

    val classData = ClassData(
        name = "CountryRegistry",
        packageName = "klib.data.location.country",
        imports = setOf(
            "klib.data.iso.Alpha2Letter",
            "klib.data.iso.Alpha3Letter",
        ),
    )

    val alpha2Class = ClassName("klib.data.iso", "Alpha2Letter")
    val alpha3Class = ClassName("klib.data.iso", "Alpha3Letter")

    val items = countries.map { country ->
        CodeBlock.builder().apply {
            add("Country(\n")
            indent()
            add("name = %S,\n", country.name)
            add("alpha2 = %T(%S),\n", alpha2Class, country.alpha2)
            add("alpha3 = %T(%S),\n", alpha3Class, country.alpha3)
            add("countryCode = %L,\n", country.countryCode.toIntOrNull() ?: 0)
            country.iso31662?.let { add("iso31662 = %S,\n", it) }
            country.region?.let { add("region = %S,\n", it) }
            country.subRegion?.let { add("subRegion = %S,\n", it) }
            country.intermediateRegion?.let { add("intermediateRegion = %S,\n", it) }
            country.regionCode?.toIntOrNull()?.let { add("regionCode = %L,\n", it) }
            country.subRegionCode?.toIntOrNull()?.let { add("subRegionCode = %L,\n", it) }
            country.intermediateRegionCode?.toIntOrNull()?.let { add("intermediateRegionCode = %L,\n", it) }
            unindent()
            add(")")
        }.build()
    }

    val seqSpec = FunSpec.builder("getCountries")
        .addModifiers(KModifier.PUBLIC)
        .returns(
            Sequence::class.asClassName()
                .parameterizedBy(ClassName("klib.data.location.country", "Country")),
        )
        .addCode(
            CodeBlock.builder().apply {
                add("return sequence {\n")
                indent()
                items.forEach { add("yield(%L)\n", it) }
                unindent()
                add("}")
            }.build(),
        )
        .build()

    val fileSpec = FileSpec.builder(classData)
        .addType(
            TypeSpec.objectBuilder("CountryRegistry")
                .addFunction(seqSpec)
                .build(),
        )
        .build()


    fileSpec.writeToOrOverride(codeGenerator, aggregating = false)
}
