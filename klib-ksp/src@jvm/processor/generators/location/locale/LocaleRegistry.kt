package processor.generators.location.locale

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
import klib.data.processing.writeToOrOverride
import processor.CompilerOptions
import app.softwork.serialization.csv.*
import com.squareup.kotlinpoet.ClassName
import klib.data.processing.model.builder
import kotlinx.serialization.decodeFromString
import processor.generators.location.locale.model.Language

// https://github.com/datasets/language-codes
public fun generateLocaleRegistry(
    logger: Logger,
    codeGenerator: CodeGenerator,
    options: CompilerOptions
) {
    val file = File(options.kspResourcesDir).resolve("iso/locale/language-codes-full.csv")
    if (!file.exists()) {
        logger.error("Languages file not found at '$file'")
        return
    }

    logger.info("Generating LocaleRegistry...")

    val languages: List<Language> = CSVFormat.decodeFromString(file.readText())

    val classData = ClassData(
        name = "LocaleRegistry",
        packageName = "klib.data.location.locale",
        imports = setOf(
            "klib.data.iso.Alpha3Letter",
        ),
    )

    val alpha3Class = ClassName("klib.data.iso", "Alpha3Letter")

    val items = languages
        .filter { it.alpha3b.length == 3 }
        .map { language ->
            CodeBlock.builder().apply {
                add("%T(%S) to {\n", alpha3Class, language.alpha3b)
                indent()
                add("Locale(\n")
                indent()
                add("languageTag = LanguageTagRegistry.languageTags.find { tag ->\n")
                indent()
                add("val languageTag = tag()\n")
                add("languageTag.language == %S\n", language.alpha3b)
                language.alpha3t?.let { add("|| languageTag.language == %S\n", it) }
                language.alpha2?.let { add("|| languageTag.language == %S\n", it) }
                unindent()
                add("}!!(),\n")
                unindent()
                add(")\n")
                unindent()
                add("}")
            }.build()
        }

    val mapSpec = PropertySpec.builder(
        "locales",
        Map::class.asClassName().parameterizedBy(
            ClassName("klib.data.iso", "Alpha3Letter"),
            LambdaTypeName.get(returnType = ClassName("klib.data.location.locale", "Locale")),
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
            TypeSpec.objectBuilder("LocaleRegistry")
                .addProperty(mapSpec)
                .build(),
        )
        .build()


    fileSpec.writeToOrOverride(codeGenerator, aggregating = false)
}


