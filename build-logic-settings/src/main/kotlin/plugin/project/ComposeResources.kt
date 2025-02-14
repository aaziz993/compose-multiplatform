@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project

import plugin.utils.all
import java.io.File
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.jetbrains.compose.resources.GenerateActualResourceCollectorsTask
import org.jetbrains.compose.resources.GenerateResourceAccessorsTask
import org.jetbrains.compose.resources.getPreparedComposeResourcesDir
import org.jetbrains.compose.resources.AssembleTargetResourcesTask
import org.jetbrains.compose.resources.KMP_RES_EXT
import org.jetbrains.compose.resources.RES_GEN_DIR
import org.jetbrains.kotlin.gradle.ComposeKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJsCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.resources.KotlinTargetResourcesPublication
import org.jetbrains.kotlin.tooling.core.withClosure

internal fun Project.configureActualResourceCollectorsGeneration(
    sourceSet: KotlinSourceSet,
    shouldGenerateCode: Provider<Boolean>,
    packageName: Provider<String>,
    makeAccessorsPublic: Provider<Boolean>,
    useActualModifier: Boolean
): TaskProvider<GenerateActualResourceCollectorsTask> {
    val taskName = "generateActualResourceCollectorsFor${sourceSet.name.uppercaseFirstChar()}"
    if (tasks.names.contains(taskName)) {
        logger.info("Actual resource collectors generation for ${sourceSet.name} is already configured")
        return tasks.named(taskName, GenerateActualResourceCollectorsTask::class.java)
    }
    logger.info("Configure actual resource collectors generation for ${sourceSet.name}")

    val accessorDirs = project.files(
        {
            val allSourceSets = sourceSet.withClosure { it.dependsOn }
            allSourceSets.mapNotNull { item ->
                val accessorsTaskName = item.getResourceAccessorsGenerationTaskName()
                if (tasks.names.contains(accessorsTaskName)) {
                    tasks.named(accessorsTaskName, GenerateResourceAccessorsTask::class.java).map { it.codeDir }
                }
                else null
            }
        },
    )

    val genTask = tasks.register(
        taskName,
        GenerateActualResourceCollectorsTask::class.java,
    ) {
        this.packageName = packageName
        this.makeAccessorsPublic = makeAccessorsPublic
        this.useActualModifier = useActualModifier
        resourceAccessorDirs.from(accessorDirs)
        codeDir = layout.buildDirectory.dir("$RES_GEN_DIR/kotlin/${sourceSet.name}ResourceCollectors")
        onlyIf { shouldGenerateCode.get() }
    }

    //register generated source set
    sourceSet.kotlin.srcDir(
        genTask.zip(shouldGenerateCode) { task, flag ->
            if (flag) listOf(task.codeDir) else emptyList()
        },
    )

    return genTask
}

private fun KotlinSourceSet.getResourceAccessorsGenerationTaskName(): String {
    return "generateResourceAccessorsFor${this.name.uppercaseFirstChar()}"
}

private fun Project.configureTargetResources(
    target: KotlinTarget,
    moduleIsolationDirectory: Provider<File>
) {
    target.compilations.all { compilation ->
        logger.info("Configure ${compilation.name} resources for '${target.targetName}' target")
        val compilationResources = files(
            {
                compilation.allKotlinSourceSets.map { sourceSet -> getPreparedComposeResourcesDir(sourceSet) }
            },
        )
        val assembleResTask = tasks.register(
            "assemble${target.targetName.uppercaseFirstChar()}${compilation.name.uppercaseFirstChar()}Resources",
            AssembleTargetResourcesTask::class.java,
        ) {
            resourceDirectories.setFrom(compilationResources)
            relativeResourcePlacement.set(moduleIsolationDirectory)
            outputDirectory.set(
                layout.buildDirectory.dir(
                    "$RES_GEN_DIR/assembledResources/${target.targetName}${compilation.name.uppercaseFirstChar()}",
                ),
            )
        }
        val allCompilationResources = assembleResTask.flatMap { it.outputDirectory.asFile }

        if (
            target.platformType in platformsForSetupKmpResources
            && compilation.name == KotlinCompilation.MAIN_COMPILATION_NAME
        ) {
            configureKmpResources(compilation, allCompilationResources)
        }
        else {
            configureResourcesForCompilation(compilation, allCompilationResources)
        }
    }
}

internal val platformsForSetupKmpResources = listOf(
    KotlinPlatformType.native, KotlinPlatformType.js, KotlinPlatformType.wasm,
)

@OptIn(ComposeKotlinGradlePluginApi::class)
private fun Project.configureKmpResources(
    compilation: KotlinCompilation<*>,
    allCompilationResources: Provider<File>
) {
    require(compilation.platformType in platformsForSetupKmpResources)
    val kmpResources = extraProperties.get(KMP_RES_EXT) as KotlinTargetResourcesPublication

    //For Native/Js/Wasm main resources:
    // 1) we have to configure new Kotlin component publication
    // 2) we have to collect all transitive main resources

    //TODO temporary API misuse. will be changed on the KMP side
    //https://youtrack.jetbrains.com/issue/KT-70909
    val target = compilation.target
    val kmpEmptyPath = provider { File("") }
    val emptyDir = layout.buildDirectory.dir("$RES_GEN_DIR/emptyResourcesDir").map { it.asFile }
    logger.info("Configure KMP component publication for '${compilation.target.targetName}'")
    kmpResources.publishResourcesAsKotlinComponent(
            target,
            { kotlinSourceSet ->
                if (kotlinSourceSet == compilation.defaultSourceSet) {
                    KotlinTargetResourcesPublication.ResourceRoot(allCompilationResources, emptyList(), emptyList())
                }
                else {
                    KotlinTargetResourcesPublication.ResourceRoot(emptyDir, emptyList(), emptyList())
                }
            },
            kmpEmptyPath,
    )

    val allResources = kmpResources.resolveResources(target)
    logger.info("Collect resolved ${compilation.name} resources for '${compilation.target.targetName}'")
    configureResourcesForCompilation(compilation, allResources)
}

private fun Project.configureResourcesForCompilation(
    compilation: KotlinCompilation<*>,
    directoryWithAllResourcesForCompilation: Provider<File>
) {
    compilation.defaultSourceSet.resources.srcDir(directoryWithAllResourcesForCompilation)

    //JS packaging requires explicit dependency
    if (compilation is KotlinJsCompilation) {
        tasks.named(compilation.processResourcesTaskName).configure {
            dependsOn(directoryWithAllResourcesForCompilation)
        }
    }
}
