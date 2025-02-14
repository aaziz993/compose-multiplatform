@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project

import java.io.File
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.jetbrains.amper.frontend.AmperModule
import org.jetbrains.amper.frontend.Fragment
import org.jetbrains.amper.frontend.schema.ComposeResourcesSettings
import org.jetbrains.amper.gradle.android.AndroidAwarePart
import org.jetbrains.amper.gradle.base.AmperNamingConventions
import org.jetbrains.amper.gradle.base.PluginPartCtx
import org.jetbrains.amper.gradle.moduleDir
import org.jetbrains.amper.gradle.tryRemove
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.internal.IDEA_IMPORT_TASK_NAME
import org.jetbrains.compose.resources.CopyNonXmlValueResourcesTask
import org.jetbrains.compose.resources.GenerateActualResourceCollectorsTask
import org.jetbrains.compose.resources.GenerateExpectResourceCollectorsTask
import org.jetbrains.compose.resources.GenerateResourceAccessorsTask
import org.jetbrains.compose.resources.PrepareComposeResourcesTask
import org.jetbrains.compose.resources.RES_GEN_DIR
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.compose.resources.XmlValuesConverterTask
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinMetadataTarget
import org.jetbrains.kotlin.tooling.core.withClosure
import org.slf4j.LoggerFactory

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
        val composeVersion = chooseComposeVersion(model)!!
        val composeResourcesDir = module.moduleDir.resolve("composeResources").toFile()

        project.plugins.apply("org.jetbrains.kotlin.plugin.compose")
        project.plugins.apply("org.jetbrains.compose")

//        // Clean old resources from source sets.
//        kotlinMPE.sourceSets.all { resources.tryRemove { it.endsWith("composeResources") } }

        // Adjust task.
        project.adjustComposeResourcesGeneration()

        // Adjust source sets.
        module.rootFragment.kotlinSourceSet?.apply {
//            resources.srcDirs(composeResourcesDir)
            dependencies {
                implementation("org.jetbrains.compose.runtime:runtime:$composeVersion")
                implementation("org.jetbrains.compose.components:components-resources:$composeVersion")
            }
        }

//        androidSourceSets?.findByName("main")
//            ?.resources?.srcDirs(composeResourcesDir)
    }

    private fun Project.adjustComposeResourcesGeneration() {
        val rootFragment = checkNotNull(module.rootFragment) { "Root fragment expected" }
        val config = rootFragment.settings.compose.resources
        val packageName = config.getResourcesPackageName(module, config.exposedAccessors)

        project.extensions.configure<ComposeExtension> {
            extensions.configure<ResourcesExtension> {
                packageOfResClass = packageName
                publicResClass = config.exposedAccessors
            }
        }

        kotlinMPE.sourceSets.all {
            val resDir = module.moduleDir.resolve("composeResources@$name").toFile()

            adjustPrepareComposeResourcesTask(
                this,
                resDir,
            )
        }

        project.adjustResourceCollectorsGeneration(
            config.packageName,
            config.exposedAccessors,
            true,
        )
    }

    private fun ComposeResourcesSettings.getResourcesPackageName(module: AmperModule, makeAccessorsPublic: Boolean): String {
        return packageName.takeIf { it.isNotEmpty() }?.let {
            if (makeAccessorsPublic) "${project.name}.$it" else it
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

    private fun Project.adjustPrepareComposeResourcesTask(
        sourceSet: KotlinSourceSet,
        originalResourcesDir: File,
    ) {
        tasks.named(
            "convertXmlValueResourcesFor${sourceSet.name.uppercaseFirstChar()}",
            XmlValuesConverterTask::class.java,
        ) {
            this.originalResourcesDir = originalResourcesDir
        }

        tasks.named(
            "copyNonXmlValueResourcesFor${sourceSet.name.uppercaseFirstChar()}",
            CopyNonXmlValueResourcesTask::class.java,
        ) {
            this.originalResourcesDir = originalResourcesDir
        }

        tasks.named(
            getPrepareComposeResourcesTaskName(sourceSet),
            PrepareComposeResourcesTask::class.java
        )
    }

    private fun getPrepareComposeResourcesTaskName(sourceSet: KotlinSourceSet) =
        "prepareComposeResourcesTaskFor${sourceSet.name.uppercaseFirstChar()}"

    private fun Project.adjustResourceCollectorsGeneration(
        packageName: String,
        makeAccessorsPublic: Boolean,
        shouldGenerateCode: Boolean
    ) {
        // `expect` is generated in `common` only, while `actual` are generated in the refined fragments.
        //  do not separate `expect`/`actual` if the module only contains a single fragment.
        val shouldSeparateExpectActual = module.rootFragment.platforms.size > 1


        kotlinMPE.sourceSets
            .matching { it.name == KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME }
            .all {
                adjustExpectResourceCollectorsGeneration(
                    this,
                    shouldSeparateExpectActual,
                )
            }

        val targetSourceSets = mutableListOf<KotlinSourceSet>()

        kotlinMPE.targets.all {
            if (this is KotlinAndroidTarget) {
                kotlinMPE.sourceSets.matching { it.name == "androidMain" }.all {
                    adjustActualResourceCollectorsGeneration(
                        this,
                        shouldSeparateExpectActual,
                    )
                    targetSourceSets.add(this)
                }
            }
            else if (this !is KotlinMetadataTarget) {
                compilations.matching { it.name == KotlinCompilation.MAIN_COMPILATION_NAME }.all {
                    adjustActualResourceCollectorsGeneration(
                        defaultSourceSet,
                        shouldSeparateExpectActual,
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
                            getResourceCollectorsCodeDirName("Main"),
                        ),
                    )
                }

                kotlinMPE.sourceSets.matching { it.name == "wasm" }.all {
                    kotlin.srcDir(
                        project.layout.buildDirectory.dir(
                            getResourceCollectorsCodeDirName("Main"),
                        ),
                    )
                }

                kotlinMPE.sourceSets.matching { it.name == "js" }.all {
                    kotlin.srcDir(
                        project.layout.buildDirectory.dir(
                            getResourceCollectorsCodeDirName("Main"),
                        ),
                    )
                }

                kotlinMPE.sourceSets.matching { it.name == "ios" }.all {
                    val genTask = configureActualResourceCollectorsGeneration(
                        this,
                        provider { shouldGenerateCode },
                        provider { packageName },
                        provider { makeAccessorsPublic },
                        true,
                    )

                    //setup task execution during IDE import
                    tasks.configureEach {
                        if (name == IDEA_IMPORT_TASK_NAME) {
                            dependsOn(genTask)
                        }
                    }
                }
            }

            if (module.rootFragment.name == "common" || module.rootFragment.name == "ios") {
                listOf("iosArm64", "iosX64", "iosSimulatorArm64").forEach { sourceSet ->
                    kotlinMPE.sourceSets.matching { it.name == sourceSet }.all {
                        kotlin.srcDir(
                            project.layout.buildDirectory.dir(
                                getResourceCollectorsCodeDirName("Main"),
                            ),
                        )
                    }
                }
            }
        }
        else {
            kotlinMPE.sourceSets
                .matching { it.name == KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME }
                .all {
                    kotlin.srcDir(
                        targetSourceSets.map { sourceSet ->
                            project.layout.buildDirectory.dir(sourceSet.getResourceCollectorsCodeDirName())
                        },
                    )
                }
        }
    }

    private fun Project.adjustExpectResourceCollectorsGeneration(
        sourceSet: KotlinSourceSet,
        shouldGenerateCode: Boolean,
    ) {
        tasks.named(
            "generateExpectResourceCollectorsFor${sourceSet.name.uppercaseFirstChar()}",
            GenerateExpectResourceCollectorsTask::class.java,
        ) {
            onlyIf { shouldGenerateCode }
        }

        val codeDirPath =
            layout.buildDirectory.dir(sourceSet.getResourceCollectorsCodeDirName()).get().asFile

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
    ) = tasks.named(
        "generateActualResourceCollectorsFor${sourceSet.name.uppercaseFirstChar()}",
        GenerateActualResourceCollectorsTask::class.java,
    ) {
        this.useActualModifier = useActualModifier
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
}
