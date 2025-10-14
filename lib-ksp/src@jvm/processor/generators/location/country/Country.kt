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
    val jsonFile = File(options.kspResourcesDir).resolve("iso/country/countries.json")
    if (!jsonFile.exists()) {
        logger.error("Countries file not found at '$jsonFile'")
        return
    }

    val csvFile = File(options.kspResourcesDir).resolve("iso/country/country-codes.csv")
    if (!csvFile.exists()) {
        logger.error("Countries file not found at '$csvFile'")
        return
    }

    logger.info("Generating Country.Companion.getCountries() extension...")

    val countries: List<Country> = Json.decodeFromString(jsonFile.readText())

    val countryDialMap = csvFile.loadCountryDialMap()

    val classData = ClassData(
        name = "CountryExt",
        packageName = "klib.data.location.country",
        imports = setOf(
            "klib.data.iso.Alpha2Letter",
            "klib.data.iso.Alpha2Letter",
            "klib.data.location.locale.toLocale",
        ),
    )

    val countryClass = ClassName("klib.data.location.country", "Country")
    val alpha2Class = ClassName("klib.data.iso", "Alpha2Letter")
    val alpha3Class = ClassName("klib.data.iso", "Alpha3Letter")

    val items = countries.map { country ->
        CodeBlock.builder().apply {
            add("%T(\n", countryClass)
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
            countryDialMap[country.alpha2]?.let { add("dial = %S,\n", "+$it") }
            unindent()
            add(")")
        }.build()
    }

    val seqSpec = FunSpec.builder("getCountries")
        .addModifiers(KModifier.PUBLIC)
        .receiver(countryClass.nestedClass("Companion"))
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
        .addFunction(seqSpec)
        .build()


    fileSpec.writeToOrOverride(codeGenerator, aggregating = false)
}

private fun File.loadCountryDialMap(): Map<String, String> {
    val lines = readLines()
    val header = lines.first().split(",")
    val alpha2Index = header.indexOf("ISO3166-1-Alpha-2")
    val dialIndex = header.indexOf("Dial")
    val languagesIndex = header.indexOf("Languages")

    require(alpha2Index >= 0 && dialIndex >= 0 && languagesIndex >= 0) {
        "CSV must contain 'ISO3166-1-Alpha-2' and 'Dial' columns"
    }

    return lines.drop(1).mapNotNull { line ->
        val columns = parseCsvLine(line)
        val alpha2 = columns.getOrNull(alpha2Index)?.trim().orEmpty()
        val dial = columns.getOrNull(dialIndex)?.trim().orEmpty()
        if (alpha2.isEmpty() && dial.isEmpty()) null else alpha2 to dial
    }.toMap()
}

private fun parseCsvLine(line: String): List<String> {
    val regex = Regex("""(?<=^|,)(?:"([^"]*)"|([^",]*))""")
    return regex.findAll(line).map { match ->
        match.groups[1]?.value ?: match.groups[2]?.value ?: ""
    }.toList()
}
