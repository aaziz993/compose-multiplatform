package plugin.project.gradle.spotless.model

import com.diffplug.gradle.spotless.JavaExtension
import org.gradle.api.Project

internal interface JavaExtension {

    val importOrder: ImportOrderConfig?
    val removeIfUnusedImports: Boolean?
    val removeUnusedImports: String?

    /** Uses the [google-java-format](https://github.com/google/google-java-format) jar to format source code.  */
    val googleJavaFormat: GoogleJavaFormatConfig?

    /** Uses the [palantir-java-format](https://github.com/palantir/palantir-java-format) jar to format source code.  */
    val palantirJavaFormat: PalantirJavaFormatConfig?
    val eclipse: EclipseConfig?

    /** Removes newlines between type annotations and types.  */
    val formatAnnotations: FormatAnnotationsConfig?

    /** Apply CleanThat refactoring rules.  */
    val cleanthat: CleanthatJavaConfig?

    context(Project)
    fun applyTo(extension: JavaExtension) {
        importOrder?.let { importOrder ->
            importOrder.applyTo(
                importOrder.importOrder?.let { extension.importOrder(*it.toTypedArray()) }
                    ?: importOrder.importOrderFile?.let(extension::importOrderFile)!!,
            )
        }

        removeIfUnusedImports?.let { extension.removeUnusedImports() }
        removeUnusedImports?.let(extension::removeUnusedImports)

        googleJavaFormat?.let { googleJavaFormat ->
            googleJavaFormat.applyTo(
                googleJavaFormat.version?.resolveVersion()?.let(extension::googleJavaFormat)
                    ?: extension.googleJavaFormat(),
            )
        }

        palantirJavaFormat?.let { palantirJavaFormat ->
            palantirJavaFormat.applyTo(
                palantirJavaFormat.version?.resolveVersion()?.let(extension::palantirJavaFormat)
                    ?: extension.palantirJavaFormat(),
            )
        }

        eclipse?.let { eclipse ->
            eclipse.applyTo(eclipse.formatterVersion?.let(extension::eclipse) ?: extension.eclipse())
        }

        formatAnnotations?.let { formatAnnotations ->
            formatAnnotations.applyTo(extension.formatAnnotations())
        }

        cleanthat?.let { cleanthat ->
            cleanthat.applyTo(extension.cleanthat())
        }
    }
}
