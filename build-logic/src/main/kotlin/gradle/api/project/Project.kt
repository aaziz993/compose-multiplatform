package gradle.api.project

import androidx.room.gradle.RoomExtension
import app.cash.sqldelight.gradle.SqlDelightExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.apollographql.apollo3.gradle.api.ApolloExtension
import com.diffplug.gradle.spotless.SpotlessExtension
import com.github.gmazzo.buildconfig.BuildConfigExtension
import com.google.devtools.ksp.gradle.KspExtension
import com.osacky.doctor.DoctorExtension
import de.jensklingenberg.ktorfit.gradle.KtorfitPluginExtension
import gradle.api.initialization.catalogs
import gradle.api.initialization.dsl.VersionCatalog
import gradle.api.initialization.libs
import gradle.api.initialization.sensitive
import gradle.api.initialization.sensitiveOrElse
import gradle.api.repositories.CacheRedirector
import gradle.plugins.getOrPut
import io.github.sgrishchenko.karakum.gradle.plugin.KarakumExtension
import io.ktor.plugin.features.KtorExtension
import kotlinx.benchmark.gradle.BenchmarksExtension
import kotlinx.knit.KnitPluginExtension
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import kotlinx.rpc.RpcExtension
import kotlinx.validation.ApiValidationExtension
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.Directory
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.GradleInternal
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskCollection
import org.gradle.api.toolchain.management.ToolchainManagement
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.SigningExtension
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.compose.android.AndroidExtension
import org.jetbrains.compose.desktop.DesktopExtension
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.versioning.VersioningConfiguration
import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsEnvSpec
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.npm.NpmExtension
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootEnvSpec
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension
import org.jetbrains.kotlin.noarg.gradle.NoArgExtension
import org.jetbrains.kotlin.powerassert.gradle.PowerAssertGradleExtension
import org.owasp.dependencycheck.gradle.extension.DependencyCheckExtension
import org.sonarqube.gradle.SonarExtension

/**
 * Create native module name from project path.
 */
public val Project.moduleName: String
    get() = path
        .removePrefix(":")
        .replace(":", "-")
        .replace("[^A-Za-z0-9_]".toRegex(), "_")

/**
 * Create android namespace from project group and path.
 * Replace '-' and ':' in path with '.'
 */
public val Project.androidNamespace: String
    get() = path.removePrefix(":")
        .split("[-_:]".toRegex()).filter(String::isNotBlank)
        .joinToString(".", transform = String::sanitize)

private val RESERVED = setOf(
    "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class",
    "const", "continue", "default", "do", "double", "else", "enum", "extends", "final",
    "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int",
    "interface", "long", "native", "new", "package", "private", "protected", "public",
    "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this",
    "throw", "throws", "transient", "try", "void", "volatile", "while",
)

private fun String.sanitize(): String {
    val cleaned = replace("[^A-Za-z0-9_]".toRegex(), "_") // invalid → underscore
        .trim('_')                               // no leading/trailing _
        .ifEmpty { "module" }                    // fallback if empty

    return if (cleaned in RESERVED) "${cleaned}x" else cleaned
}

public val Project.packageOfResClass: String
    get() = "${name.lowercase().asUnderscoredIdentifier()}.generated.resources"

private fun String.asUnderscoredIdentifier(): String =
    replace('-', '_')
        .let { if (it.isNotEmpty() && it.first().isDigit()) "_$it" else it }

public val Project.settings: Settings get() = (gradle as GradleInternal).settings

public val Project.catalogs: Map<String, VersionCatalog>
    get() = settings.catalogs

public fun Project.libs(name: String): VersionCatalog = settings.libs(name)

public val Project.libs: VersionCatalog
    get() = settings.libs

public val Project.composeLibs: ComposePlugin.Dependencies
    get() = extensions.getByType<ComposeExtension>().dependencies

@Suppress("UnstableApiUsage")
public val Project.toolchain: ToolchainManagement get() = the()

@Suppress("UnstableApiUsage")
public fun Project.toolchain(configure: ToolchainManagement.() -> Unit): Unit = extensions.configure(configure)

public val Project.doctor: DoctorExtension get() = the()

public fun Project.doctor(configure: DoctorExtension.() -> Unit): Unit = extensions.configure(configure)

public val Project.dependencyCheck: DependencyCheckExtension get() = the()

public fun Project.dependencyCheck(configure: DependencyCheckExtension.() -> Unit): Unit =
    extensions.configure(configure)

public val Project.buildConfig: BuildConfigExtension get() = the()

public fun Project.buildConfig(configure: BuildConfigExtension.() -> Unit): Unit =
    extensions.configure(configure)

public val Project.spotless: SpotlessExtension get() = the()

public fun Project.spotless(configure: SpotlessExtension.() -> Unit): Unit = extensions.configure(configure)

public val Project.kover: KoverProjectExtension get() = the()

public fun Project.kover(configure: KoverProjectExtension.() -> Unit): Unit = extensions.configure(configure)

public val Project.sonar: SonarExtension get() = the()

public fun Project.sonar(configure: SonarExtension.() -> Unit): Unit = extensions.configure(configure)

public val Project.knit: KnitPluginExtension get() = the()

public fun Project.knit(configure: KnitPluginExtension.() -> Unit): Unit = extensions.configure(configure)

public val Project.dokka: DokkaExtension get() = the()

public fun Project.dokka(configure: DokkaExtension.() -> Unit): Unit = extensions.configure(configure)

public val Project.versioning: VersioningConfiguration get() = the()

public fun Project.versioning(configure: VersioningConfiguration.() -> Unit): Unit =
    extensions.configure(configure)

public val Project.apiValidation: ApiValidationExtension get() = the()

public fun Project.apiValidation(configure: ApiValidationExtension.() -> Unit): Unit =
    extensions.configure(configure)

public val Project.ksp: KspExtension get() = the()

public fun Project.ksp(configure: KspExtension.() -> Unit): Unit = extensions.configure(configure)

public val Project.allOpen: AllOpenExtension get() = the()

public fun Project.allOpen(configure: AllOpenExtension.() -> Unit): Unit = extensions.configure(configure)

public val Project.noArg: NoArgExtension get() = the()

public fun Project.noArg(configure: NoArgExtension.() -> Unit): Unit = extensions.configure(configure)

public val Project.benchmark: BenchmarksExtension get() = the()

public fun Project.benchmark(configure: BenchmarksExtension.() -> Unit): Unit = extensions.configure(configure)

public val Project.javaToolchain: JavaToolchainService get() = the()

public val Project.java: JavaPluginExtension get() = the()

public fun Project.java(configure: JavaPluginExtension.() -> Unit): Unit = extensions.configure(configure)

public val Project.javaApp: JavaApplication get() = the()

public fun Project.javaApp(configure: JavaApplication.() -> Unit): Unit = extensions.configure(configure)

public val Project.android: BaseAppModuleExtension get() = the()

public fun Project.android(configure: BaseAppModuleExtension.() -> Unit): Unit =
    extensions.configure(configure)

public val Project.androidComponents: ApplicationAndroidComponentsExtension get() = the()

public fun Project.androidComponents(configure: ApplicationAndroidComponentsExtension.() -> Unit): Unit =
    extensions.configure(configure)

public val Project.kotlin: KotlinMultiplatformExtension get() = the()

public fun Project.kotlin(configure: KotlinMultiplatformExtension.() -> Unit): Unit =
    extensions.configure(configure)

public val Project.sqldelight: SqlDelightExtension get() = the()

public fun Project.sqldelight(configure: SqlDelightExtension.() -> Unit): Unit = extensions.configure(configure)

public val Project.room: RoomExtension get() = the()

public fun Project.room(configure: RoomExtension.() -> Unit): Unit = extensions.configure(configure)

public val Project.ktor: KtorExtension get() = the()

public fun Project.ktor(configure: KtorExtension.() -> Unit): Unit = extensions.configure(configure)

public val Project.rpc: RpcExtension get() = the()

public fun Project.rpc(configure: RpcExtension.() -> Unit): Unit = extensions.configure(configure)

public val Project.ktorfit: KtorfitPluginExtension get() = the()

public fun Project.ktorfit(configure: KtorfitPluginExtension.() -> Unit): Unit = extensions.configure(configure)

public val Project.apollo: ApolloExtension get() = the()

public fun Project.apollo(configure: ApolloExtension.() -> Unit): Unit = extensions.configure(configure)

public val Project.powerAssert: PowerAssertGradleExtension get() = the()

public fun Project.powerAssert(configure: PowerAssertGradleExtension.() -> Unit): Unit =
    extensions.configure(configure)

public val KotlinMultiplatformExtension.cocoapods: CocoapodsExtension get() = the()

public fun KotlinMultiplatformExtension.cocoapods(configure: CocoapodsExtension.() -> Unit): Unit =
    extensions.configure(configure)

public val Project.npm: NpmExtension get() = the()

public fun Project.npm(configure: NpmExtension.() -> Unit): Unit = extensions.configure(configure)

public val Project.yarn: YarnRootExtension get() = the()

public fun Project.yarn(configure: YarnRootExtension.() -> Unit): Unit = extensions.configure(configure)

public val Project.node: NodeJsRootExtension get() = the()

public fun Project.node(configure: NodeJsRootExtension.() -> Unit): Unit = extensions.configure(configure)

public val Project.nodeJsEnv: NodeJsEnvSpec get() = the()

public fun Project.nodeEnv(configure: NodeJsEnvSpec.() -> Unit): Unit = extensions.configure(configure)

public val Project.yarnEnv: YarnRootEnvSpec get() = the()

public fun Project.yarnEnv(configure: YarnRootEnvSpec.() -> Unit): Unit = extensions.configure(configure)

public val Project.karakum: KarakumExtension get() = the()

public fun Project.karakum(configure: KarakumExtension.() -> Unit): Unit = extensions.configure(configure)

public val Project.compose: ComposeExtension get() = the()

public fun Project.compose(configure: ComposeExtension.() -> Unit): Unit = extensions.configure(configure)

public val Project.sourceSets: SourceSetContainer get() = the()

public val ComposeExtension.resources: ResourcesExtension get() = the()

public fun ComposeExtension.resources(configure: ResourcesExtension.() -> Unit): Unit =
    extensions.configure(configure)

public const val SOURCES_SET_TO_COMPOSE_RESOURCES_DIR_EXT: String = "sourceset.compose.resources.dir.ext"

public val Project.sourceSetsToComposeResourcesDirs: MutableMap<KotlinSourceSet, Directory>
    get() = extraProperties.getOrPut(SOURCES_SET_TO_COMPOSE_RESOURCES_DIR_EXT, ::mutableMapOf)

public val ComposeExtension.desktop: DesktopExtension get() = the()

public fun ComposeExtension.desktop(configure: DesktopExtension.() -> Unit): Unit =
    extensions.configure(configure)

public val ComposeExtension.android: AndroidExtension get() = the()

public fun ComposeExtension.android(configure: AndroidExtension.() -> Unit): Unit =
    extensions.configure(configure)

public val Project.publishing: PublishingExtension get() = the()

public fun Project.publishing(configure: PublishingExtension.() -> Unit): Unit = extensions.configure(configure)

public val Project.signing: SigningExtension get() = the()

public fun Project.signing(configure: SigningExtension.() -> Unit): Unit = extensions.configure(configure)

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

