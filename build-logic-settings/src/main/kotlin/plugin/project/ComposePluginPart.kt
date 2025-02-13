@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")


package plugin.project

import com.android.build.api.dsl.KotlinMultiplatformAndroidTarget
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.gradle.kotlin.dsl.withType
import org.jetbrains.amper.frontend.AmperModule
import org.jetbrains.amper.frontend.Fragment
import org.jetbrains.amper.frontend.schema.ComposeResourcesSettings
import org.jetbrains.amper.gradle.android.AndroidAwarePart
import org.jetbrains.amper.gradle.base.AmperNamingConventions
import org.jetbrains.amper.gradle.base.PluginPartCtx
import org.jetbrains.compose.internal.IDEA_IMPORT_TASK_NAME
import org.jetbrains.compose.internal.IdeaImportTask
import org.jetbrains.compose.resources.*
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinMetadataTarget
import org.jetbrains.kotlin.tooling.core.withClosure
import org.slf4j.LoggerFactory
import java.io.File

public class ComposePluginPart(ctx: PluginPartCtx) : KMPEAware, AmperNamingConventions, AndroidAwarePart(ctx) {

    private val logger = LoggerFactory.getLogger(ComposePluginPart::class.java)

    override val kotlinMPE: KotlinMultiplatformExtension =
        project.extensions.getByType<KotlinMultiplatformExtension>()

    override val needToApply: Boolean by lazy {
        module.leafFragments.any { it.settings.compose.enabled }
    }

    // Highly dependent on compose version and ABI.
    // Need to implement API on compose plugin side.
    override fun applyBeforeEvaluate() {
//        val composeVersion = chooseComposeVersion(model)!!

        project.plugins.apply("org.jetbrains.kotlin.plugin.compose")
        project.plugins.apply("org.jetbrains.compose")

        // Adjust task.
        project.adjustComposeResourcesGeneration()
    }

    private fun Project.adjustComposeResourcesGeneration() {
        val rootFragment = checkNotNull(module.rootFragment) { "Root fragment expected" }
        val config = rootFragment.settings.compose.resources
        val packageName = config.getResourcesPackageName(module)
        val makeAccessorsPublic = config.exposedAccessors
        val packagingDir = null
        val resDir = project.file("resources")

        /*
              The tasks generate code (collectors and Res) if either is true:
               - The project has some actual resources in any of the fragments.
               - The user explicitly requested to make the resources API public.
                 We generate public code to make API not depend on the actual presence of the resources,
                 because the user already opted-in to their usage.
            */
        val shouldGenerateCode = resDir.exists()

        logger.info(
            """
            ADJUSTING COMPOSE RESOURCES GENERATION

            Project: ${name}
            Root fragment: ${module.rootFragment.name}
            Platforms: ${module.rootFragment.platforms}
            Source sets: ${kotlinMPE.sourceSets.map(KotlinSourceSet::getName)}
            Should generate: $shouldGenerateCode
            Should separate expect/actual for resource collectors: ${module.rootFragment.platforms.size > 1}
            GenerateResClassTask: ${tasks.withType<GenerateResClassTask>().map(Task::getName)}
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


            """.trimIndent()
        )

        tasks.withType<GenerateResClassTask> {
            this.packageName = packageName
            this.makeAccessorsPublic = config.exposedAccessors
            this.packagingDir = packagingDir

            onlyIf { shouldGenerateCode }
        }

        kotlinMPE.sourceSets.all {
            val isCommonSourceSet = name == KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME

            val preparedResourcesTask = adjustPrepareComposeResourcesTask(
                this,
                resDir,
                shouldGenerateCode && isCommonSourceSet
            )

            val preparedResources = preparedResourcesTask.flatMap { it.outputDir.asFile }

            tasks.named(
                getResourceAccessorsGenerationTaskName(),
                GenerateResourceAccessorsTask::class.java
            ) {
                this.packageName = packageName
                this.makeAccessorsPublic = config.exposedAccessors
                this.packagingDir = packagingDir
                this.resDir = preparedResources

                onlyIf { shouldGenerateCode && isCommonSourceSet }
            }
        }

        adjustResourceCollectorsGeneration(shouldGenerateCode, packageName, makeAccessorsPublic)


        kotlinMPE.targets
            .matching { target -> !target.skipResourcesConfiguration() }
            .all { adjustTargetResources(this, packagingDir) }

        project.afterEvaluate {
            //configure ANDROID resources
            onAgpApplied {
                adjustAndroidComposeResources(packagingDir)
            }
        }
    }

    private fun ComposeResourcesSettings.getResourcesPackageName(module: AmperModule): String {
        return packageName.takeIf { it.isNotEmpty() } ?: run {
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

    private fun Project.adjustPrepareComposeResourcesTask(
        sourceSet: KotlinSourceSet,
        originalResourcesDir: File,
        shouldGenerate: Boolean
    ): TaskProvider<PrepareComposeResourcesTask?> {
        tasks.named(
            "convertXmlValueResourcesFor${sourceSet.name.uppercaseFirstChar()}",
            XmlValuesConverterTask::class.java
        ) {
            this.originalResourcesDir = originalResourcesDir

            onlyIf { shouldGenerate }
        }

        tasks.named(
            "copyNonXmlValueResourcesFor${sourceSet.name.uppercaseFirstChar()}",
            CopyNonXmlValueResourcesTask::class.java
        ) {
            this.originalResourcesDir = originalResourcesDir

            onlyIf { shouldGenerate }
        }

        return tasks.named(
            getPrepareComposeResourcesTaskName(sourceSet),
            PrepareComposeResourcesTask::class.java
        )
    }

    private fun getPrepareComposeResourcesTaskName(sourceSet: KotlinSourceSet) =
        "prepareComposeResourcesTaskFor${sourceSet.name.uppercaseFirstChar()}"

    private fun KotlinSourceSet.getResourceAccessorsGenerationTaskName(): String {
        return "generateResourceAccessorsFor${this.name.uppercaseFirstChar()}"
    }

    private fun Project.adjustResourceCollectorsGeneration(
        shouldGenerateCode: Boolean,
        packageName: String,
        makeAccessorsPublic: Boolean
    ) {
        // `expect` is generated in `common` only, while `actual` are generated in the refined fragments.
        //  do not separate `expect`/`actual` if the module only contains a single fragment.
        val shouldSeparateExpectActual = module.rootFragment.platforms.size > 1


        kotlinMPE.sourceSets
            .matching { it.name == KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME }
            .all {
                adjustExpectResourceCollectorsGeneration(
                    this,
                    shouldGenerateCode && shouldSeparateExpectActual,
                    packageName,
                    makeAccessorsPublic
                )
            }

        val targetSourceSets = mutableListOf<KotlinSourceSet>()

        kotlinMPE.targets.all {
            if (this is KotlinAndroidTarget) {
                kotlinMPE.sourceSets.matching { it.name == "androidMain" }.all {
                    adjustActualResourceCollectorsGeneration(
                        this,
                        shouldGenerateCode,
                        packageName,
                        makeAccessorsPublic,
                        shouldSeparateExpectActual
                    )
                    targetSourceSets.add(this)
                }
            } else if (this !is KotlinMetadataTarget) {
                compilations.matching { it.name == KotlinCompilation.MAIN_COMPILATION_NAME }.all {
                    adjustActualResourceCollectorsGeneration(
                        defaultSourceSet,
                        shouldGenerateCode,
                        packageName,
                        makeAccessorsPublic,
                        shouldSeparateExpectActual
                    )
                    targetSourceSets.add(defaultSourceSet)
                }
            }
        }

        if (shouldSeparateExpectActual) {
            if (module.rootFragment.name == "common") {
                kotlinMPE.sourceSets.matching { it.name == "jvm" }.all {
                    kotlin.srcDir(
                        project.layout.buildDirectory.dir(
                            getResourceCollectorsCodeDirName("Main")
                        )
                    )
                }

                kotlinMPE.sourceSets.matching { it.name == "wasm" }.all {
                    kotlin.srcDir(
                        project.layout.buildDirectory.dir(
                            getResourceCollectorsCodeDirName("Main")
                        )
                    )
                }

                kotlinMPE.sourceSets.matching { it.name == "js" }.all {
                    kotlin.srcDir(
                        project.layout.buildDirectory.dir(
                            getResourceCollectorsCodeDirName("Main")
                        )
                    )
                }

//                    kotlinMPE.sourceSets.matching { it.name == "ios" }.all {
//                        val genTask = configureActualResourceCollectorsGeneration(
//                            this,
//                            providers.provider { shouldGenerateCode },
//                            providers.provider { packageName },
//                            providers.provider { makeAccessorsPublic },
//                            true
//                        )
//
//                        //setup task execution during IDE import
//                        tasks.configureEach {
//                            if (name == IDEA_IMPORT_TASK_NAME) {
//                                dependsOn(genTask)
//                            }
//                        }
//                    }
            }

            if (module.rootFragment.name == "common" || module.rootFragment.name == "ios") {
                listOf("iosArm64", "iosX64", "iosSimulatorArm64").forEach { sourceSet ->
                    kotlinMPE.sourceSets.matching { it.name == sourceSet }.all {
                        kotlin.srcDir(
                            project.layout.buildDirectory.dir(
                                getResourceCollectorsCodeDirName("Main")
                            )
                        )
                    }
                }
            }
        } else {
            kotlinMPE.sourceSets
                .matching { it.name == KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME }
                .all {
                    kotlin.srcDir(targetSourceSets.map { sourceSet ->
                        project.layout.buildDirectory.dir(sourceSet.getResourceCollectorsCodeDirName())
                    })
                }
        }
    }

    private fun Project.adjustExpectResourceCollectorsGeneration(
        sourceSet: KotlinSourceSet,
        shouldGenerateCode: Boolean,
        packageName: String,
        makeAccessorsPublic: Boolean
    ) {
        tasks.named(
            "generateExpectResourceCollectorsFor${sourceSet.name.uppercaseFirstChar()}",
            GenerateExpectResourceCollectorsTask::class.java
        ) {
            this.packageName = packageName
            this.makeAccessorsPublic = makeAccessorsPublic

            onlyIf { shouldGenerateCode }
        }

        val codeDirPath =
            layout.buildDirectory.dir(sourceSet.getResourceCollectorsCodeDirName()).get().asFile

        if (!shouldGenerateCode) {
            //register generated source set
            sourceSet.kotlin.sourceDirectories.removeAll { file ->
                file == codeDirPath
            }
        }
    }

    private fun Project.adjustActualResourceCollectorsGeneration(
        sourceSet: KotlinSourceSet,
        shouldGenerateCode: Boolean,
        packageName: String,
        makeAccessorsPublic: Boolean,
        useActualModifier: Boolean,
    ) {
        tasks.named(
            "generateActualResourceCollectorsFor${sourceSet.name.uppercaseFirstChar()}",
            GenerateActualResourceCollectorsTask::class.java
        ) {
            this.packageName = packageName
            this.makeAccessorsPublic = makeAccessorsPublic
            this.useActualModifier = useActualModifier

            onlyIf { shouldGenerateCode }
        }

        val codeDirPath =
            layout.buildDirectory.dir(sourceSet.getResourceCollectorsCodeDirName()).get().asFile

        if (!shouldGenerateCode) {
            //register generated source set
            sourceSet.kotlin.sourceDirectories.removeAll { file ->
                file == codeDirPath
            }
        }
    }

    private fun Project.configureActualResourceCollectorsGeneration(
        sourceSet: KotlinSourceSet,
        shouldGenerateCode: Provider<Boolean>,
        packageName: Provider<String>,
        makeAccessorsPublic: Provider<Boolean>,
        useActualModifier: Boolean
    ): TaskProvider<GenerateActualResourceCollectorsTask> {
        val taskName = "generateActualResourceCollectorsFor${sourceSet.name.uppercaseFirstChar()}"

        logger.info("Configure actual resource collectors generation for ${sourceSet.name}")

        val accessorDirs = project.files({
            val allSourceSets = sourceSet.withClosure { it.dependsOn }
            allSourceSets.mapNotNull { item ->
                val accessorsTaskName = item.getResourceAccessorsGenerationTaskName()
                if (tasks.names.contains(accessorsTaskName)) {
                    tasks.named(accessorsTaskName, GenerateResourceAccessorsTask::class.java).map { it.codeDir }
                } else null
            }
        })

        val genTask = tasks.register(
            taskName,
            GenerateActualResourceCollectorsTask::class.java
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
            }
        )

        return genTask
    }

    private fun KotlinSourceSet.getResourceCollectorsCodeDirName(suffix: String = ""): String =
        "$RES_GEN_DIR/kotlin/${name}${suffix}ResourceCollectors"

    private fun KotlinTarget.skipResourcesConfiguration(): Boolean = when {
        this is KotlinMetadataTarget -> true

        //android resources should be configured via AGP
        this is KotlinAndroidTarget -> true

        //new AGP library target
        this.isMultiplatformAndroidTarget() -> true

        else -> false
    }

    @Suppress("UnstableApiUsage")
    private fun KotlinTarget.isMultiplatformAndroidTarget(): Boolean = try {
        this is KotlinMultiplatformAndroidTarget
    } catch (_: NoClassDefFoundError) {
        false
    }

    private fun Project.adjustTargetResources(
        target: KotlinTarget,
        moduleIsolationDirectory: File?
    ) {
        target.compilations.all {
            logger.info("Configure $name resources for '${target.targetName}' target")

            tasks.named(
                "assemble${target.targetName.uppercaseFirstChar()}${name.uppercaseFirstChar()}Resources",
                AssembleTargetResourcesTask::class.java
            ) {
                relativeResourcePlacement = moduleIsolationDirectory
            }
        }
    }

    private fun Project.adjustAndroidComposeResources(moduleResourceDir: File?) {
        tasks.withType<CopyResourcesToAndroidAssetsTask>().configureEach {
            moduleResourceDir?.let { relativeResourcePlacement = it }

            doLast {
                logger.info(
                    "COPY FROM ${
                        from.get().map { it.absolutePath }
                    } TO " + outputDirectory.get().asFile.absolutePath
                )
            }
        }
    }
}
