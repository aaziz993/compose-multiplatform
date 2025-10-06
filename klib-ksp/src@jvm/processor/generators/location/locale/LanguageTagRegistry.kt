package processor.generators.location.locale

import com.google.devtools.ksp.processing.CodeGenerator
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import java.io.File
import klib.data.processing.Logger
import klib.data.processing.model.ClassData
import klib.data.processing.model.CompilerClass
import klib.data.processing.writeToWithOverride
import processor.CompilerOptions
import processor.generators.location.locale.model.LanguageTag

public fun generateLanguageTagRegistry(
    logger: Logger,
    codeGenerator: CodeGenerator,
    options: CompilerOptions
) {

    val file = File(options.kspResourcesDir).resolve("iso/language-tags.txt")
    if (!file.exists()) {
        logger.error("Language tags file not found at '$file'")
        return
    }

    logger.info("Generating LanguageTagRegistry...")

    val languageTags = parseLanguageTags(file)

    val classData = ClassData(
        name = "LanguageTagRegistry",
        packageName = "klib.data.location.locale",
        imports = setOf(
            "klib.data.location.locale.LanguageTag",
        ),
    )

    val items = languageTags.map { tag ->
        CodeBlock.builder().apply {
            add("LanguageTag(\n")
            indent()
            add("extensions = listOf(${tag.extensions.joinToString(",") { "%S".format(it) }}),\n")
            add("extLangs = listOf(${tag.extlangs.joinToString(",") { "%S".format(it) }}),\n")
            add("language = %S,\n", tag.language)
            add("privateUse = %S,\n", tag.privateUse)
            add("region = %S,\n", tag.region)
            add("script = %S,\n", tag.script)
            add("variants = listOf(${tag.variants.joinToString(",") { "%S".format(it) }})\n")
            unindent()
            add(")")
        }.build()
    }

    val listSpec = PropertySpec.builder(
        "languageTags",
        List::class.asClassName().parameterizedBy(
            CompilerClass("LanguageTag", "klib.data.location.locale", "").toClassName(),
        ),
    ).initializer(
        CodeBlock.builder().apply {
            add("listOf(\n")
            indent()
            items.forEach { add("%L,\n", it) }
            unindent()
            add(")")
        }.build(),
    ).addModifiers(KModifier.PUBLIC).build()

    val fileSpec = FileSpec.builder(classData.packageName, classData.name)
        .addType(
            TypeSpec.objectBuilder("LanguageTagRegistry")
                .addProperty(listSpec)
                .build(),
        )
        .build()


    fileSpec.writeToWithOverride(codeGenerator, aggregating = false)
}

private fun parseLanguageTags(file: File): List<LanguageTag> {
    val items = file.readText()
        .split("""^%%\s*""".toRegex(RegexOption.MULTILINE))
        .drop(1) // skip the header before the first %%
        .map { record ->
            record.lines().associate { line ->
                line.split(":", limit = 2).map(String::trim).let { (key, value) -> key to value }
            }
        }

    val privateUseRange = items.find { item -> item["Scope"] == "private-use" }?.let { item ->
        item["Subtag"]!!.split("..").let { (key, value) -> key to value }
    }

    return items.filter { item -> item["Type"] == "language" }
        .map { item ->
            val subTag = item["Subtag"]!!
            val script = item["Script"] ?: item["Suppress-Script"]
            LanguageTag(
                extlangs = items.filter { item -> item["Type"] == "extlang" && item["Prefix"] == subTag }.map { item -> item["Subtag"]!! }.toSet(),
                language = if (item["Scope"] == "private-use") null else subTag,
                privateUse = if (item["Scope"] == "private-use") subTag else null,
                script = script,
                variants = items.filter { item -> item["Type"] == "variant" && item["Prefix"] == subTag }.map { item -> item["Subtag"]!! }.toSet(),
            )
        }
}


