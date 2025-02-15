@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.jetbrains.compose.resources.GenerateActualResourceCollectorsTask
import org.jetbrains.compose.resources.GenerateResourceAccessorsTask
import org.jetbrains.compose.resources.RES_GEN_DIR
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
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
