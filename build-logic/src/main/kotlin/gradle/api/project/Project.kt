package gradle.api.project

import gradle.api.initialization.catalogs
import gradle.api.initialization.dsl.VersionCatalog
import gradle.api.initialization.libs
import gradle.api.initialization.sensitive
import gradle.api.initialization.sensitiveOrElse
import gradle.api.repositories.CacheRedirector
import gradle.plugins.getOrPut
import klib.data.type.primitives.string.LETTER_OR_DIGIT
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.Directory
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.GradleInternal
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.plugins.JavaPlatformExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskCollection
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.extraProperties

/**
 * Create module name for compose resource package, web and native output from project path.
 */
public val Project.pathUnderscore: String
    get() = pathSanitize("_")

/**
 * Create android namespace from project group and path.
 * Replace '-' and ':' in path with '.' and etc.
 */
public val Project.pathDot: String
    get() = pathSanitize(".")

public val Project.packageOfResClass: String
    get() = "${pathUnderscore}.generated.resources"

private fun Project.pathSanitize(separator: String) =
    path.removePrefix(":")
        .split("[^${Regex.LETTER_OR_DIGIT}]".toRegex())
        .filter(String::isNotBlank)
        .joinToString(separator, transform = String::sanitize)
        .let { path -> if (path.first().isDigit()) "x$path" else path }

private val RESERVED = setOf(
    "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class",
    "const", "continue", "default", "do", "double", "else", "enum", "extends", "final",
    "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int",
    "interface", "long", "native", "new", "package", "private", "protected", "public",
    "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this",
    "throw", "throws", "transient", "try", "void", "volatile", "while",
)

private fun String.sanitize(): String = if (this in RESERVED) "${this}x" else this

public val Project.settings: Settings get() = (gradle as GradleInternal).settings

public val Project.catalogs: Map<String, VersionCatalog>
    get() = settings.catalogs

public fun Project.libs(name: String): VersionCatalog = settings.libs(name)

public val Project.libs: VersionCatalog
    get() = settings.libs

public val Project.composeLibs: ComposePlugin.Dependencies
    get() = extensions.getByType<ComposeExtension>().dependencies

public val Project.javaToolchain: JavaToolchainService get() = the()

public fun Project.javaToolchain(configure: JavaToolchainService.() -> Unit): Unit = extensions.configure(configure)

public val Project.javaPlatform: JavaPlatformExtension get() = the()

public fun Project.javaPlatform(configure: JavaPlatformExtension.() -> Unit): Unit = extensions.configure(configure)

public val Project.java: JavaPluginExtension get() = the()

public fun Project.java(configure: JavaPluginExtension.() -> Unit): Unit = extensions.configure(configure)

public val Project.javaApp: JavaApplication get() = the()

public fun Project.javaApp(configure: JavaApplication.() -> Unit): Unit = extensions.configure(configure)

public val Project.kotlin: KotlinMultiplatformExtension get() = the()

public fun Project.kotlin(configure: KotlinMultiplatformExtension.() -> Unit): Unit =
    extensions.configure(configure)

public val Project.sourceSets: SourceSetContainer get() = the()

public const val SOURCES_SET_TO_COMPOSE_RESOURCES_DIR_EXT: String = "sourceset.compose.resources.dir.ext"

public val Project.sourceSetsToComposeResourcesDirs: MutableMap<KotlinSourceSet, Directory>
    get() = extraProperties.getOrPut(SOURCES_SET_TO_COMPOSE_RESOURCES_DIR_EXT, ::mutableMapOf)

public fun Project.files(elements: Iterable<String>): ConfigurableFileCollection =
    files(*elements.toList().toTypedArray())

public inline fun <reified T : Task> Project.registerAggregationTestTask(
    name: String,
    noinline targetFilter: (KotlinTarget) -> Boolean,
    crossinline tasksFilter: (TaskCollection<T>) -> TaskCollection<T> = { it },
): Unit = kotlin.targets
    .matching(targetFilter)
    .map(KotlinTarget::targetName)
    .let { targetNames ->
        registerAggregationTestTask(
            name,
            targetNames,
        ) {
            tasksFilter(tasks.withType<T>())
        }
    }

public inline fun <reified T : Task> Project.registerAggregationTestTask(
    name: String,
    targetNames: List<String>,
    tasksFilter: (TaskCollection<T>) -> TaskCollection<T> = { it },
) {
    if (targetNames.isEmpty()) return

    tasksFilter(
        tasks.withType<T>().matching { task ->
            targetNames.any { targetName -> task.name.startsWith(targetName) }
        },
    ).takeIf(TaskCollection<*>::isNotEmpty)
        ?.let { testTasks ->
            tasks.register("${name}Test") {
                group = "verification"

                dependsOn(testTasks)
            }
        }
}

public fun Project.enableCacheRedirect(): Unit = CacheRedirector.applyTo(project)

public fun Project.execute(cmd: String): String = providers.exec {
    commandLine(cmd.split(" "))
}.standardOutput.asText.get().trim()

public fun Project.sensitiveOrElse(key: String, defaultValue: () -> String): String =
    settings.sensitiveOrElse(key, defaultValue)

public fun Project.sensitive(key: String): String = settings.sensitive(key)

