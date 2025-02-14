@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project

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
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.internal.IDEA_IMPORT_TASK_NAME
import org.jetbrains.compose.resources.GenerateActualResourceCollectorsTask
import org.jetbrains.compose.resources.GenerateExpectResourceCollectorsTask
import org.jetbrains.compose.resources.GenerateResourceAccessorsTask
import org.jetbrains.compose.resources.PrepareComposeResourcesTask
import org.jetbrains.compose.resources.RES_GEN_DIR
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinMetadataTarget
import org.jetbrains.kotlin.tooling.core.withClosure
import org.slf4j.LoggerFactory
import org.gradle.api.Task
import org.gradle.internal.os.OperatingSystem
import org.gradle.kotlin.dsl.withType
import org.jetbrains.amper.frontend.schema.ProductType
import org.jetbrains.compose.desktop.DesktopExtension
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.resources.AssembleTargetResourcesTask
import org.jetbrains.compose.resources.GenerateResClassTask

public class ComposePluginPart(ctx: PluginPartCtx) : KMPEAware, AmperNamingConventions, AndroidAwarePart(ctx) {

    private val logger = LoggerFactory.getLogger(ComposePluginPart::class.java)

    override val kotlinMPE: KotlinMultiplatformExtension =
        project.extensions.getByType<KotlinMultiplatformExtension>()

    override val needToApply: Boolean by lazy {
        module.leafFragments.any { it.settings.compose.enabled }
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


        println(
            """
            ADJUSTING COMPOSE RESOURCES GENERATION

            Project: $name
            Source sets: ${kotlinMPE.sourceSets.map { "${it.name} resources: ${it.resources.sourceDirectories.map { it.name }}" }}
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
                            }.also(::println),
                        )
                    },
                )
            }
        }


        adjustResourceCollectorsGeneration(
            config.packageName,
            config.exposedAccessors,
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

    private fun adjustResourceCollectorsGeneration(
        packageName: String,
        makeAccessorsPublic: Boolean,
    ) = with(project) {
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

        val actualSourceSets = mutableListOf<KotlinSourceSet>()

        kotlinMPE.targets.all {
            if (this is KotlinAndroidTarget) {
                kotlinMPE.sourceSets.matching { it.name == "androidMain" }.all {
                    adjustActualResourceCollectorsGeneration(
                        this,
                        shouldSeparateExpectActual,
                    )
                    actualSourceSets.add(this)
                }
            }
            else if (this !is KotlinMetadataTarget) {
                compilations.matching { it.name == KotlinCompilation.MAIN_COMPILATION_NAME }.all {
                    adjustActualResourceCollectorsGeneration(
                        defaultSourceSet,
                        shouldSeparateExpectActual,
                    )
                    actualSourceSets.add(defaultSourceSet)
                }
            }
        }

        if (shouldSeparateExpectActual) {
            // In multiplatform module add generated actual directories to amper specific source set source directories
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
                        provider { packageName },
                        provider { makeAccessorsPublic },
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

    private fun configureActualResourceCollectorsGeneration(
        sourceSet: KotlinSourceSet,
        packageName: Provider<String>,
        makeAccessorsPublic: Provider<Boolean>,
    ): TaskProvider<GenerateActualResourceCollectorsTask> = with(project) {
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
            this.useActualModifier = true
            resourceAccessorDirs.from(accessorDirs)
            codeDir = layout.buildDirectory.dir("$RES_GEN_DIR/kotlin/${sourceSet.name}ResourceCollectors")
        }

        //register generated source set
        sourceSet.kotlin.srcDir(genTask.get().codeDir)

        return genTask
    }

    private fun KotlinSourceSet.getResourceAccessorsGenerationTaskName(): String {
        return "generateResourceAccessorsFor${this.name.uppercaseFirstChar()}"
    }
}
