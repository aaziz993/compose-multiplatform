@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project

import java.io.File
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.provider.Provider
import org.gradle.internal.os.OperatingSystem
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.gradle.kotlin.dsl.withType
import org.jetbrains.amper.frontend.AmperModule
import org.jetbrains.amper.frontend.Fragment
import org.jetbrains.amper.frontend.schema.ComposeResourcesSettings
import org.jetbrains.amper.frontend.schema.ProductType
import org.jetbrains.amper.gradle.android.AndroidAwarePart
import org.jetbrains.amper.gradle.base.AmperNamingConventions
import org.jetbrains.amper.gradle.base.PluginPartCtx
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.desktop.DesktopExtension
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.resources.AssembleTargetResourcesTask
import org.jetbrains.compose.resources.GenerateActualResourceCollectorsTask
import org.jetbrains.compose.resources.GenerateExpectResourceCollectorsTask
import org.jetbrains.compose.resources.GenerateResClassTask
import org.jetbrains.compose.resources.GenerateResourceAccessorsTask
import org.jetbrains.compose.resources.KMP_RES_EXT
import org.jetbrains.compose.resources.PrepareComposeResourcesTask
import org.jetbrains.compose.resources.RES_GEN_DIR
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.compose.resources.getModuleResourcesDir
import org.jetbrains.kotlin.gradle.ComposeKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinMetadataTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.resources.KotlinTargetResourcesPublication
import plugin.utils.all
import plugin.utils.ideaImportDependOn

public class ComposePluginPart(ctx: PluginPartCtx) : KMPEAware, AmperNamingConventions, AndroidAwarePart(ctx) {

    override val kotlinMPE: KotlinMultiplatformExtension =
        project.extensions.getByType<KotlinMultiplatformExtension>()

    override val needToApply: Boolean by lazy {
        module.leafFragments.any { it.settings.compose.enabled }
    }

    private val hasCommonIosSourceSet by lazy {
        kotlinMPE.sourceSets.any { it.name == "ios" }
    }

    // Highly dependent on compose version and ABI.
    // Need to implement API on compose plugin side.
    override fun applyBeforeEvaluate(): Unit = with(project) {
        val composeVersion = chooseComposeVersion(model)!!

        plugins.apply("org.jetbrains.kotlin.plugin.compose")
        plugins.apply("org.jetbrains.compose")

        module.rootFragment.kotlinSourceSet?.apply {
            dependencies {
                implementation("org.jetbrains.compose.runtime:runtime:$composeVersion")
                implementation("org.jetbrains.compose.components:components-resources:$composeVersion")
            }
        }

        extensions.configure<ComposeExtension> {
            if (module.type == ProductType.JVM_APP) {
                // Configure desktop
                extensions.configure<DesktopExtension>(::configureDesktopExtension)
            }

            // Adjust task.
            extensions.configure<ResourcesExtension>(::adjustComposeResources)
        }
    }

    private fun configureDesktopExtension(extension: DesktopExtension): DesktopExtension = with(project) {
        extension.apply {
            application {
                jvmArgs(
                    "--add-opens=java.desktop/sun.awt=ALL-UNNAMED",
                    "--add-opens=java.desktop/java.awt.peer=ALL-UNNAMED",
                )

                if (OperatingSystem.current() == OperatingSystem.MAC_OS) {
                    jvmArgs("--add-opens=java.desktop/sun.lwawt=ALL-UNNAMED")
                    jvmArgs("--add-opens=java.desktop/sun.lwawt.macosx=ALL-UNNAMED")
                }

                nativeDistributions {
                    targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
                    packageName = group.toString()
//                    packageVersion = lib.versions.desktop.`package`.version.get()

                    linux {
                        iconFile = file("jvmAppIcons/LinuxIcon.png")
                    }
                    windows {
                        iconFile = file("jvmAppIcons/WindowsIcon.ico")
                    }
                    macOS {
                        iconFile = file("jvmAppIcons/MacOSIcon.icns")
                        bundleID = "$group.${rootProject.name}"
                    }
                }
                // also proguard rules
                buildTypes.release.proguard {
                    configurationFiles.from("compose-desktop.pro")
                }
            }
        }
    }

    private fun adjustComposeResources(extension: ResourcesExtension) = with(project) {
        val rootFragment = checkNotNull(module.rootFragment) { "Root fragment expected" }
        val config = rootFragment.settings.compose.resources
        val packageName = config.getResourcesPackageName(module, config.exposedAccessors)

        logger.info(
            """
            -----------------------ADJUSTING COMPOSE RESOURCES GENERATION--------------------------

            Project: $name
            Module root fragment: ${module.rootFragment.name}
            Targets: ${kotlinMPE.targets.map { it.name }}
            Source sets: ${kotlinMPE.sourceSets.map { it.name }}
            GenerateResClassTask: ${tasks.withType<GenerateResClassTask>().map(Task::getName)}
            PrepareComposeResourcesTask: ${tasks.withType<PrepareComposeResourcesTask>().map(Task::getName)}
            GenerateResourceAccessorsTask: ${tasks.withType<GenerateResourceAccessorsTask>().map(Task::getName)}
            GenerateExpectResourceCollectorsTask: ${
                tasks.withType<GenerateExpectResourceCollectorsTask>().map(Task::getName)
            }
            GenerateActualResourceCollectorsTask: ${
                tasks.withType<GenerateActualResourceCollectorsTask>().map(Task::getName)
            }
            AssembleTargetResourcesTask: ${
                tasks.withType<AssembleTargetResourcesTask>().map(Task::getName)
            }

            """.trimIndent(),
        )


        extension.apply {
            packageOfResClass = packageName
            publicResClass = config.exposedAccessors

            kotlinMPE.sourceSets.all {
                // Adjust composeResources to match flatten directory structure
                customDirectory(
                    name,
                    provider {
                        layout.projectDirectory.dir(
                            when (name) {
                                KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME -> "composeResources"
                                KotlinSourceSet.COMMON_TEST_SOURCE_SET_NAME -> "composeTestResources"
                                else -> "composeResources@$name"
                            },
                        )
                    },
                )
            }
        }
        adjustResourceCollectorsGeneration(
            packageName,
            config.exposedAccessors,
        )

        configureResources(provider { extension }.getModuleResourcesDir(project))
    }

    private fun configureResources(moduleIsolationDirectory: Provider<File>) = with(project) {
        (listOf("jvm", "wasm", "js") + if (hasCommonIosSourceSet) emptyList() else listOf("iosArm64", "iosX64", "iosSimulatorArm64"))
            .forEach { platform ->
                // Add generated resource collectors code sources to new target source set resources
                kotlinMPE.sourceSets.matching { it.name == platform }.all { sourceSet ->
                    sourceSet.kotlin.srcDir(
                        project.layout.buildDirectory.dir(
                            sourceSet.getResourceCollectorsCodeDirName("Main"),
                        ),
                    )
                }
                // Add generated assembled resources to new target source set resources
                kotlinMPE.targets.matching { it.name == platform }.all { target ->
                    val sourceSetName = if (module.shouldSeparateResourceCollectorsExpectActual) target.name else "commonMain"

                    adjustAssembledResources(target, sourceSetName)
                }
            }

        // Create ios target assembled resources[
        listOf("iosArm64", "iosX64", "iosSimulatorArm64").firstNotNullOfOrNull { platform ->
            kotlinMPE.targets.findByName("iosArm64")
        }?.let { target ->
            adjustAssembledResources(target, "ios")
        }
    }

    @OptIn(ComposeKotlinGradlePluginApi::class)
    private fun Project.adjustAssembledResources(
        target: KotlinTarget,
        sourceSetName: String,
    ) = kotlinMPE.sourceSets.findByName(sourceSetName)?.let { sourceSet ->
        val assembleTask = tasks.named(
            "assemble${target.targetName.uppercaseFirstChar()}MainResources",
            AssembleTargetResourcesTask::class.java,
        )

        var allResources = assembleTask.flatMap { it.outputDirectory.asFile }

        if (target.platformType in platformsForSetupKmpResources) {
            val kmpResources = extraProperties.get(KMP_RES_EXT) as KotlinTargetResourcesPublication

            allResources = kmpResources.resolveResources(target)
        }

        sourceSet.resources.srcDir(allResources)

        //setup task execution during IDE import
        ideaImportDependOn(assembleTask)
    }

    private fun ComposeResourcesSettings.getResourcesPackageName(module: AmperModule, makeAccessorsPublic: Boolean): String {
        return packageName.takeIf { it.isNotEmpty() }?.let {
            if (makeAccessorsPublic) "${project.name.replace("[-_]".toRegex(), ".")}.$it" else it
        } ?: run {
            val packageParts =
                module.rootFragment.inferPackageNameFromPublishing() ?: module.inferPackageNameFromModule()
            (packageParts + listOf("generated", "resources")).joinToString(separator = ".") {
                it.lowercase().asUnderscoredIdentifier()
            }
        }
    }

    private fun Fragment.inferPackageNameFromPublishing(): List<String>? {
        return settings.publishing?.let {
            listOfNotNull(it.group, it.name).takeIf(List<*>::isNotEmpty)
        }
    }

    private fun AmperModule.inferPackageNameFromModule(): List<String> {
        return listOf(userReadableName)
    }

    private fun String.asUnderscoredIdentifier(): String =
        replace('-', '_')
            .let { if (it.isNotEmpty() && it.first().isDigit()) "_$it" else it }

    private fun adjustResourceCollectorsGeneration(
        packageName: String,
        makeAccessorsPublic: Boolean,
    ) = with(project) {
        // `expect` is generated in `common` only, while `actual` are generated in the refined fragments.
        //  do not separate `expect`/`actual` if the module only contains a single fragment.

        kotlinMPE.sourceSets
            .matching { it.name == KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME }
            .all {
                adjustExpectResourceCollectorsGeneration(
                    this,
                    module.shouldSeparateResourceCollectorsExpectActual,
                )
            }

        val actualSourceSets = mutableListOf<KotlinSourceSet>()

        kotlinMPE.targets.all { target ->
            if (target is KotlinAndroidTarget) {
                kotlinMPE.sourceSets.matching { it.name == "androidMain" }.all { sourceSet ->
                    adjustActualResourceCollectorsGeneration(
                        sourceSet,
                        module.shouldSeparateResourceCollectorsExpectActual,
                        true,
                    )
                    actualSourceSets.add(sourceSet)
                }
            }
            else if (target !is KotlinMetadataTarget) {
                // Prevent ios sub source sets resource generation if ios is presented
                val shouldGenerateCode = if (hasCommonIosSourceSet) !target.targetName.startsWith("ios") else true

                target.compilations.matching { it.name == KotlinCompilation.MAIN_COMPILATION_NAME }.all { compilation ->
                    adjustActualResourceCollectorsGeneration(
                        compilation.defaultSourceSet,
                        module.shouldSeparateResourceCollectorsExpectActual,
                        shouldGenerateCode,
                    )

                    if (shouldGenerateCode) {
                        actualSourceSets.add(compilation.defaultSourceSet)
                    }
                }
            }
        }


        kotlinMPE.sourceSets.matching { it.name == "ios" }.all {
            val genTask = configureActualResourceCollectorsGeneration(
                this,
                provider { true },
                provider { packageName },
                provider { makeAccessorsPublic },
                true,
            )

            //setup task execution during IDE import
            ideaImportDependOn(genTask)
        }

        if (!module.shouldSeparateResourceCollectorsExpectActual) {
            // In single-platform module add generated actual directory to commonMain source set source directories
            kotlinMPE.sourceSets
                .matching { it.name == KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME }
                .all {
                    kotlin.srcDir(
                        actualSourceSets.map { sourceSet ->
                            project.layout.buildDirectory.dir(sourceSet.getResourceCollectorsCodeDirName())
                        },
                    )
                }
        }
    }

    private fun adjustExpectResourceCollectorsGeneration(
        sourceSet: KotlinSourceSet,
        shouldGenerateCode: Boolean,
    ) = with(project) {
        val genTask = tasks.named(
            "generateExpectResourceCollectorsFor${sourceSet.name.uppercaseFirstChar()}",
            GenerateExpectResourceCollectorsTask::class.java,
        ) {
            onlyIf { shouldGenerateCode }
        }

        val codeDirPath = genTask.flatMap { it.codeDir.asFile }

        if (!shouldGenerateCode) {
            //unregister generated source set
            sourceSet.kotlin.sourceDirectories.removeAll { file ->
                file == codeDirPath
            }
        }
    }

    private fun KotlinSourceSet.getResourceCollectorsCodeDirName(suffix: String = ""): String =
        "$RES_GEN_DIR/kotlin/${name}${suffix}ResourceCollectors"

    private fun Project.adjustActualResourceCollectorsGeneration(
        sourceSet: KotlinSourceSet,
        useActualModifier: Boolean,
        shouldGenerateCode: Boolean
    ) = tasks.named(
        "generateActualResourceCollectorsFor${sourceSet.name.uppercaseFirstChar()}",
        GenerateActualResourceCollectorsTask::class.java,
    ) {
        this.useActualModifier = useActualModifier

        onlyIf { shouldGenerateCode }
    }
}
