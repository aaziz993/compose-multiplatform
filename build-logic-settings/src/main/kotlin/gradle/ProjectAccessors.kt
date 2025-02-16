package gradle

import com.diffplug.gradle.spotless.SpotlessExtension
import com.github.gmazzo.gradle.plugins.BuildConfigExtension
import com.osacky.doctor.DoctorExtension
import kotlinx.atomicfu.plugin.gradle.AtomicFUPluginExtension
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import kotlinx.validation.ApiValidationExtension
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.GradleInternal
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsEnvSpec
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.npm.NpmExtension
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

internal val Project.settings: Settings
    get() = (gradle as GradleInternal).settings

internal val Project.doctor: DoctorExtension get() = the()

internal fun Project.doctor(configure: DoctorExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.atomicFU: AtomicFUPluginExtension get() = the()

internal fun Project.atomicFU(configure: AtomicFUPluginExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.spotless: SpotlessExtension get() = the()

internal fun Project.spotless(configure: SpotlessExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.kover: KoverProjectExtension get() = the()

internal fun Project.kover(configure: KoverProjectExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.dokka: DokkaExtension get() = the()

internal fun Project.dokka(configure: DokkaExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.buildConfig: BuildConfigExtension get() = the()

internal fun Project.buildConfig(configure: BuildConfigExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.apiValidation: ApiValidationExtension get() = the()

internal fun Project.apiValidation(configure: ApiValidationExtension.() -> Unit) =
    extensions.configure(configure)

internal val Project.npm: NodeJsRootExtension get() = the()

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



