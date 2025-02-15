@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.compose

import org.gradle.api.Task
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.gradle.kotlin.dsl.withType
import org.jetbrains.amper.frontend.schema.ComposeResourcesSettings
import org.jetbrains.amper.gradle.amperModule
import org.jetbrains.compose.resources.AssembleTargetResourcesTask
import org.jetbrains.compose.resources.GenerateActualResourceCollectorsTask
import org.jetbrains.compose.resources.GenerateExpectResourceCollectorsTask
import org.jetbrains.compose.resources.GenerateResClassTask
import org.jetbrains.compose.resources.GenerateResourceAccessorsTask
import org.jetbrains.compose.resources.KMP_RES_EXT
import org.jetbrains.compose.resources.PrepareComposeResourcesTask
import org.jetbrains.compose.resources.RES_GEN_DIR
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.gradle.ComposeKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinMetadataTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.resources.KotlinTargetResourcesPublication
import org.jetbrains.kotlin.konan.target.Family
import gradle.all
import gradle.configureActualResourceCollectorsGeneration
import gradle.ideaImportDependOn
import gradle.shouldSeparateResourceCollectorsExpectActual
import plugin.project.KMPEAware

context(KMPEAware)
internal fun Project.configureResourcesExtension(extension: ResourcesExtension) {
    val module = amperModule!!
    val rootFragment = checkNotNull(module.rootFragment) { "Root fragment expected" }
    val settings = rootFragment.settings.compose.resources

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
        packageOfResClass = settings.packageName
        publicResClass = settings.exposedAccessors

        kotlinMPE.sourceSets.all { sourceSet ->
            // Adjust composeResources to match flatten directory structure
            customDirectory(
                sourceSet.name,
                provider {
                    layout.projectDirectory.dir(
                        when (sourceSet.name) {
                            KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME -> "composeResources"
                            KotlinSourceSet.COMMON_TEST_SOURCE_SET_NAME -> "composeTestResources"
                            else -> "composeResources@${sourceSet.name}"
                        },
                    )
                },
            )
        }
    }
    adjustResourceCollectorsGeneration(settings)

    configureResources()
}

context(KMPEAware)
private fun Project.configureResources() {
    val module = amperModule!!

    (listOf("jvm", "wasm", "js") + if (kotlinMPE.sourceSets.hasIosSourceSet()) emptyList() else listOf("iosArm64", "iosX64", "iosSimulatorArm64"))
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

    // Create ios common source set assembled resources
    listOf("iosArm64", "iosX64", "iosSimulatorArm64").forEach { platform ->
        kotlinMPE.targets.matching { it.name == platform }.all { target ->
            adjustAssembledResources(target, "ios")
        }
    }
}

context(KMPEAware)
private fun Project.adjustResourceCollectorsGeneration(
    settings: ComposeResourcesSettings,
) {
    val module = amperModule!!

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
            val shouldGenerateCode = if (target.isIosTarget()) !kotlinMPE.sourceSets.hasIosSourceSet() else true

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
            provider { settings.packageName },
            provider { settings.exposedAccessors },
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

context(KMPEAware)
private fun Collection<KotlinSourceSet>.hasIosSourceSet() = kotlinMPE.sourceSets.any { it.name == "ios" }

private fun KotlinTarget.isIosTarget(): Boolean =
    this is KotlinNativeTarget && konanTarget.family == Family.IOS

private fun Project.adjustExpectResourceCollectorsGeneration(
    sourceSet: KotlinSourceSet,
    shouldGenerateCode: Boolean,
) {
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

private fun org.gradle.api.Project.adjustActualResourceCollectorsGeneration(
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

context(KMPEAware)
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
}

private val platformsForSetupKmpResources = listOf(
    KotlinPlatformType.native, KotlinPlatformType.js, KotlinPlatformType.wasm,
)
