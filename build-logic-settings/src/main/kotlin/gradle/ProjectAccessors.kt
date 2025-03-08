package gradle

import androidx.room.gradle.RoomExtension
import app.cash.sqldelight.gradle.SqlDelightExtension
import com.android.build.gradle.BaseExtension
import com.apollographql.apollo3.gradle.api.ApolloExtension
import com.diffplug.gradle.spotless.SpotlessExtension
import com.github.gmazzo.gradle.plugins.BuildConfigExtension
import com.google.devtools.ksp.gradle.KspExtension
import com.osacky.doctor.DoctorExtension
import de.jensklingenberg.ktorfit.gradle.KtorfitGradleConfiguration
import gradle.model.project.ProjectProperties
import io.github.sgrishchenko.karakum.gradle.plugin.KarakumExtension
import kotlinx.atomicfu.plugin.gradle.AtomicFUPluginExtension
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import kotlinx.rpc.RpcExtension
import kotlinx.validation.ApiValidationExtension
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.GradleInternal
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.toolchain.management.ToolchainManagement
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.amper.gradle.getBindingMap
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.android.AndroidExtension
import org.jetbrains.compose.desktop.DesktopExtension
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.versioning.VersioningConfiguration
import org.jetbrains.gradle.apple.AppleProjectExtension
import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsEnvSpec
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.npm.NpmExtension
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension
import org.jetbrains.kotlin.noarg.gradle.NoArgExtension
import org.jetbrains.kotlin.powerassert.gradle.PowerAssertGradleExtension
import org.sonarqube.gradle.SonarExtension

private const val PROJECT_PROPERTIES_EXT = "project.properties.ext"

internal var Project.projectProperties: ProjectProperties
    get() = extraProperties[PROJECT_PROPERTIES_EXT] as ProjectProperties
    set(value) {
        extraProperties[PROJECT_PROPERTIES_EXT] = value
    }

internal val Project.settings: Settings
    get() = (gradle as GradleInternal).settings

internal val Project.java: JavaPluginExtension get() = the()

internal fun Project.java(configure: JavaPluginExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.javaApp: JavaApplication get() = the()

internal fun Project.javaApp(configure: JavaApplication.() -> Unit) =
    extensions.configure(configure)

internal val Project.android: BaseExtension get() = the()

internal fun Project.android(configure: BaseExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.kotlin: KotlinMultiplatformExtension get() = the()

internal fun Project.kotlin(configure: KotlinMultiplatformExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.doctor: DoctorExtension get() = the()

internal fun Project.doctor(configure: DoctorExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.buildConfig: BuildConfigExtension get() = the()

internal fun Project.buildConfig(configure: BuildConfigExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.spotless: SpotlessExtension get() = the()

internal fun Project.spotless(configure: SpotlessExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.kover: KoverProjectExtension get() = the()

internal fun Project.kover(configure: KoverProjectExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.sonar: SonarExtension get() = the()

internal fun Project.sonar(configure: SonarExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.dokka: DokkaExtension get() = the()

internal fun Project.dokka(configure: DokkaExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.versioning: VersioningConfiguration get() = the()

internal fun Project.versioning(configure: VersioningConfiguration.() -> Unit) =
    extensions.configure(configure)

internal val Project.apiValidation: ApiValidationExtension get() = the()

internal fun Project.apiValidation(configure: ApiValidationExtension.() -> Unit) =
    extensions.configure(configure)

@Suppress("UnstableApiUsage")
internal val Project.toolchain: ToolchainManagement get() = the()

@Suppress("UnstableApiUsage")
internal fun Project.toolchain(configure: ToolchainManagement.() -> Unit) =
    extensions.configure(configure)

internal val Project.ksp: KspExtension get() = the()

internal fun Project.ksp(configure: KspExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.atomicFU: AtomicFUPluginExtension get() = the()

internal fun Project.atomicFU(configure: AtomicFUPluginExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.allOpen: AllOpenExtension get() = the()

internal fun Project.allOpen(configure: AllOpenExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.noArg: NoArgExtension get() = the()

internal fun Project.noArg(configure: NoArgExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.sqldelight: SqlDelightExtension get() = the()

internal fun Project.sqldelight(configure: SqlDelightExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.room: RoomExtension get() = the()

internal fun Project.room(configure: RoomExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.rpc: RpcExtension get() = the()

internal fun Project.rpc(configure: RpcExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.ktorfit: KtorfitGradleConfiguration get() = the()

internal fun Project.ktorfit(configure: KtorfitGradleConfiguration.() -> Unit) =
    extensions.configure(configure)

internal val Project.apollo: ApolloExtension get() = the()

internal fun Project.apollo(configure: ApolloExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.powerAssert: PowerAssertGradleExtension get() = the()

internal fun Project.powerAssert(configure: PowerAssertGradleExtension.() -> Unit) =
    extensions.configure(configure)

internal val KotlinMultiplatformExtension.cocoapods: CocoapodsExtension get() = the()

internal fun KotlinMultiplatformExtension.cocoapods(configure: CocoapodsExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.apple: AppleProjectExtension get() = the()

internal fun Project.apple(configure: AppleProjectExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.npm: NpmExtension get() = the()

internal fun Project.npm(configure: NpmExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.yarn: YarnRootExtension get() = the()

internal fun Project.yarn(configure: YarnRootExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.node: NodeJsRootExtension get() = the()

internal fun Project.node(configure: NodeJsRootExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.nodeEnv: NodeJsEnvSpec get() = the()

internal fun Project.nodeEnv(configure: NodeJsEnvSpec.() -> Unit) =
    extensions.configure(configure)

internal val Project.karakum: KarakumExtension get() = the()

internal fun Project.karakum(configure: KarakumExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.compose: ComposeExtension get() = the()

internal fun Project.compose(configure: ComposeExtension.() -> Unit) =
    extensions.configure(configure)

internal val ComposeExtension.resources: ResourcesExtension get() = the()

internal fun ComposeExtension.resources(configure: ResourcesExtension.() -> Unit) =
    extensions.configure(configure)

internal val ComposeExtension.desktop: DesktopExtension get() = the()

internal fun ComposeExtension.desktop(configure: DesktopExtension.() -> Unit) =
    extensions.configure(configure)

internal val ComposeExtension.android: AndroidExtension get() = the()

internal fun ComposeExtension.android(configure: AndroidExtension.() -> Unit) =
    extensions.configure(configure)

/**
 * Create native module name from project path.
 */
internal val Project.moduleName
    get() = path.removePrefix(":").replace(":", "-")

/**
 * Create android namespace from project group and path.
 * Replace '-' and ':' in path with '.'
 */
internal val Project.androidNamespace
    get() = "$group.${path.removePrefix(":").replace("[-_:]".toRegex(), ".")}".also {
        println(
            """ANDROID NAMESPACE
        Path: $path
        Namespace: $it
        """.trimMargin(),
        )
    }

internal fun Project.execute(cmd: String): String = providers.exec {
    commandLine(cmd.split(" "))
}.standardOutput.asText.get().trim()

private const val SOURCES_SET_TO_COMPOSE_RESOURCES_DIR_EXT = "sourceset.compose.resources.dir.ext"

/**
 * Needed, because there is no [Project] during gradle setting setup, only [ProjectDescriptor],
 * so cant utilize [Project]'s [ExtensionAware] interface.
 */
internal val Project.sourceSetsToComposeResourcesDirs: MutableMap<KotlinSourceSet, Directory>
    get() = extraProperties.getBindingMap(SOURCES_SET_TO_COMPOSE_RESOURCES_DIR_EXT)
