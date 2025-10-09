package processor.generators.location.locale

import com.google.devtools.ksp.processing.CodeGenerator
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import java.io.File
import klib.data.processing.Logger
import klib.data.processing.model.ClassData
import klib.data.processing.writeToOrOverride
import processor.CompilerOptions
import app.softwork.serialization.csv.*
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import kotlinx.serialization.decodeFromString
import processor.generators.location.locale.model.LanguageTag

// https://github.com/datasets/language-codes
public fun generateLanguageTagRegistry(
    logger: Logger,
    codeGenerator: CodeGenerator,
    options: CompilerOptions
) {
    val file = File(options.kspResourcesDir).resolve("iso/locale/ietf-language-tags.csv")
    if (!file.exists()) {
        logger.error("Language tags file not found at '$file'")
        return
    }

    logger.info("Generating LanguageTagRegistry...")

    val languageTags: List<LanguageTag> = CSVFormat.decodeFromString(file.readText())

    val classData = ClassData(
        name = "LanguageTagRegistry",
        packageName = "klib.data.location.locale",
    )

    val items = languageTags.map { tag ->
        CodeBlock.builder().apply {
            add("LanguageTag.parse(%S)", tag.lang)
        }.build()
    }

    val seqSpec = FunSpec.builder("getLanguageTags")
        .addModifiers(KModifier.PUBLIC)
        .returns(
            Sequence::class.asClassName()
                .parameterizedBy(ClassName("klib.data.location.locale", "LanguageTag")),
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

    val fileSpec = FileSpec.builder(classData.packageName, classData.name)
        .addType(
            TypeSpec.objectBuilder("LanguageTagRegistry")
                .addFunction(seqSpec)
                .build(),
        )
        .build()


    fileSpec.writeToOrOverride(codeGenerator, aggregating = false)
}


