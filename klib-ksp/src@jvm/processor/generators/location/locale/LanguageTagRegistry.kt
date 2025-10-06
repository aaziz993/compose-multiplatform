package processor.generators.location.locale

import com.github.jasync.sql.db.util.length
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
            add("%S to {\n", tag.language ?: tag.privateUse)
            indent()
            add("LanguageTag(\n")
            indent()
            add("extensions = listOf(${tag.extensions.joinToString(",", "\"", "\"")}),\n")
            add("extlangs = listOf(${tag.extlangs.joinToString(",", "\"", "\"")}),\n")
            add("language = %S,\n", tag.language)
            add("privateUse = %S,\n", tag.privateUse)
            add("region = %S,\n", tag.region)
            add("script = %S,\n", tag.script)
            add("variants = listOf(${tag.variants.joinToString(",", "\"", "\"")})\n")
            unindent()
            add(")\n")
            unindent()
            add("}")
        }.build()
    }

    val mapSpec = PropertySpec.builder(
        "languageTags",
        Map::class.asClassName()
            .parameterizedBy(
                String::class.asClassName(),
                LambdaTypeName.get(returnType = CompilerClass("LanguageTag", "klib.data.location.locale", "").toClassName()),
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
            TypeSpec.objectBuilder("LanguageTagRegistry")
                .addProperty(mapSpec)
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
            record.lines().mapNotNull { line ->
                line.split(":", limit = 2)
                    .takeIf { it.length == 2 }
                    ?.map(String::trim)
                    ?.let { (key, value) -> key to value }
            }.toMap()
        }

    val privateUseRange = items.find { item -> item["Scope"] == "private-use" }?.let { item ->
        item["Subtag"]!!.split("..").let { (key, value) -> key to value }
    }

    val inPrivateUseRange: (tag: String) -> Boolean = if (privateUseRange == null) {
        { false }
    }
    else {
        { tag -> tag in privateUseRange.first..privateUseRange.second }
    }

    return items.filter { item -> item["Type"] == "language" && item["Scope"] != "private-use" }
        .map { item ->
            val subTag = item["Subtag"]!!
            val script = item["Script"] ?: item["Suppress-Script"]
            val (language, privateUse) = if (inPrivateUseRange(subTag)) null to subTag else subTag to null
            LanguageTag(
                item["Extension"]?.split(",")?.map(String::trim).orEmpty().toSet(),
                items.filter { item -> item["Type"] == "extlang" && item["Prefix"] == subTag }.map { item -> item["Subtag"]!! }.toSet(),
                language,
                privateUse,
                null,
                script,
                items.filter { item -> item["Type"] == "variant" && item["Prefix"] == subTag }.map { item -> item["Subtag"]!! }.toSet(),
            )
        }
}


