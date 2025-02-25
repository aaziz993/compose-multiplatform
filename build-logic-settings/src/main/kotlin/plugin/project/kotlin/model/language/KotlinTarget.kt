package plugin.project.kotlin.model.language

import gradle.kotlin
import gradle.projectProperties
import gradle.serialization.getPolymorphicSerializer
import gradle.trySet
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalMainFunctionArgumentsDsl
import plugin.project.kotlin.model.language.jvm.KotlinJvmCompilerOptions
import plugin.project.kotlin.model.language.jvm.KotlinJvmRunDsl
import plugin.project.kotlin.model.language.jvm.KotlinJvmTestRun
import plugin.project.kotlin.model.language.nat.HasBinaries
import plugin.project.kotlin.model.language.nat.KotlinNativeBinaryContainer
import plugin.project.kotlin.model.language.nat.KotlinNativeCompilerOptions
import plugin.project.kotlin.model.language.web.*
import plugin.project.model.ProjectType
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor

@Serializable(with = KotlinTargetSerializer::class)
internal sealed class KotlinTarget {

    abstract val targetName: String

    context(Project)
    abstract fun applyTo()
}

internal object KotlinTargetPolymorphicSerializer :
    JsonContentPolymorphicSerializer<KotlinTarget>(KotlinTarget::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<KotlinTarget> {
        val type = element.jsonObject["type"]!!.jsonPrimitive.content
        return KotlinTarget::class.getPolymorphicSerializer(type)!!
    }
}

internal object KotlinTargetSerializer :
    JsonTransformingSerializer<KotlinTarget>(KotlinTargetPolymorphicSerializer) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        if (element is JsonObject) {
            val key = element.keys.single()
            val value = element.values.single()
            return JsonObject(
                buildMap {
                    put("type", JsonPrimitive(key))
                    putAll(value.jsonObject)
                },
            )
        }

        return JsonObject(
            mapOf(
                "type" to element,
            ),
        )
    }
}

@Serializable
@SerialName("jvm")
internal data class KotlinJvmTarget(
    override val targetName: String = "",
    override val compilerOptions: KotlinJvmCompilerOptions? = null,
    val testRuns: List<KotlinJvmTestRun>? = null,
    val mainRun: KotlinJvmRunDsl? = null,
    val withJava: Boolean? = null,
) : KotlinTarget(), HasConfigurableKotlinCompilerOptions<KotlinJvmCompilerOptions> {

    context(Project)
    override fun applyTo() {
        val target = targetName.takeIf(String::isNotEmpty)?.let(kotlin::jvm) ?: kotlin.jvm()

        testRuns?.forEach { testRuns ->
            testRuns.name.takeIf(String::isNotEmpty)?.also { name ->
                target.testRuns.named(name) {
                    testRuns.applyTo(this)
                }
            } ?: target.testRuns.all {
                testRuns.applyTo(this)
            }
        }

        mainRun?.let { mainRun ->
            target.mainRun(mainRun::applyTo)
        }

        withJava?.takeIf { it }?.let { target.withJava() }
        compilerOptions?.applyTo(target.compilerOptions)
    }
}

@Serializable
@SerialName("androidTarget")
internal data class KotlinAndroidTarget(
    override val targetName: String = "",
    /** Names of the Android library variants that should be published from the target's project within the default publications which are
     * set up if the `maven-publish` Gradle plugin is applied.
     *
     * Item examples:
     * * 'release' (in case no product flavors were defined)
     * * 'fooRelease' (for the release build type of a flavor 'foo')
     * * 'fooBarRelease' (for the release build type multi-dimensional flavors 'foo' and 'bar').
     *
     * If set to null, which can also be done with [publishAllLibraryVariants],
     * all library variants will be published, but not test or application variants. */
    val publishLibraryVariants: List<String>? = null,

    /** Set up all of the Android library variants to be published from this target's project within the default publications, which are
     * set up if the `maven-publish` Gradle plugin is applied. This overrides the variants chosen with [publishLibraryVariants] */
    val publishAllLibraryVariants: Boolean? = null,

    /** If true, a publication will be created per merged product flavor, with the build types used as classifiers for the artifacts
     * published within each publication. If set to false, each Android variant will have a separate publication. */
    val publishLibraryVariantsGroupedByFlavor: Boolean? = null,
    override val compilerOptions: KotlinJvmCompilerOptions? = null,
) : KotlinTarget(), HasConfigurableKotlinCompilerOptions<KotlinJvmCompilerOptions> {

    context(Project)
    override fun applyTo() {
        val target = targetName.takeIf(String::isNotEmpty)?.let(kotlin::androidTarget) ?: kotlin.androidTarget()

        publishLibraryVariants?.let { publishLibraryVariants ->
            target.publishLibraryVariants = publishLibraryVariants
        }

        publishAllLibraryVariants?.takeIf { it }?.run { target.publishAllLibraryVariants() }
        target::publishLibraryVariantsGroupedByFlavor trySet publishLibraryVariantsGroupedByFlavor
        compilerOptions?.applyTo(target.compilerOptions)
    }
}

@Serializable
@SerialName("native")
internal sealed class KotlinNativeTarget : KotlinTarget(), HasBinaries<KotlinNativeBinaryContainer?> {

    abstract val compilerOptions: KotlinNativeCompilerOptions?

    context(Project)
    protected fun applyTo(target: org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget) {
        binaries?.let { binaries ->
            target.binaries {
                binaries.framework?.let { framework ->
                    framework {
                        framework.applyTo(this)
                    }
                }
            }
        }
    }


    context(Project)
    override fun applyTo() {
        this::class.sealedSubclasses.forEach { clazz ->
            clazz.createInstance().applyTo()
        }
    }
}

@Serializable
@SerialName("androidNative")
internal sealed class KotlinAndroidNative : KotlinNativeTarget() {
    context(Project)
    override fun applyTo() {
        this::class.sealedSubclasses.forEach { clazz ->
            clazz.createInstance().applyTo()
        }
    }
}

@Serializable
@SerialName("androidNativeArm32")
internal data class KotlinAndroidNativeArm32(
    override val targetName: String = "",
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinAndroidNative() {

    context(Project)
    override fun applyTo() {
        super.applyTo(
            targetName.takeIf(String::isNotEmpty)?.let(kotlin::androidNativeArm32) ?: kotlin.androidNativeArm32()
        )
    }
}

@Serializable
@SerialName("androidNativeArm64")
internal data class KotlinAndroidNativeArm64(
    override val targetName: String = "",
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinAndroidNative() {

    context(Project)
    override fun applyTo() {
        super.applyTo(
            targetName.takeIf(String::isNotEmpty)?.let(kotlin::androidNativeArm64) ?: kotlin.androidNativeArm64()
        )
    }
}

@Serializable
@SerialName("androidNativeX64")
internal data class KotlinAndroidNativeX64(
    override val targetName: String = "",
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinAndroidNative() {

    context(Project)
    override fun applyTo() {
        super.applyTo(targetName.takeIf(String::isNotEmpty)?.let(kotlin::androidNativeX64) ?: kotlin.androidNativeX64())
    }
}

@Serializable
@SerialName("androidNativeX86")
internal data class KotlinAndroidNativeX86(
    override val targetName: String = "",
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinAndroidNative() {

    context(Project)
    override fun applyTo() {
        super.applyTo(targetName.takeIf(String::isNotEmpty)?.let(kotlin::androidNativeX86) ?: kotlin.androidNativeX86())
    }
}

@Serializable
@SerialName("apple")
internal sealed class KotlinAppleTarget : KotlinNativeTarget() {
    context(Project)
    override fun applyTo() {
        this::class.sealedSubclasses.forEach { clazz ->
            clazz.createInstance().applyTo()
        }
    }
}

@Serializable
@SerialName("ios")
internal sealed class KotlinIosTarget : KotlinAppleTarget() {
    context(Project)
    override fun applyTo() {
        this::class.sealedSubclasses.forEach { clazz ->
            clazz.createInstance().applyTo()
        }
    }
}

@Serializable
@SerialName("iosArm64")
internal data class IosArm64(
    override val targetName: String = "",
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinIosTarget() {

    context(Project)
    override fun applyTo() {
        super.applyTo(targetName.takeIf(String::isNotEmpty)?.let(kotlin::iosArm64) ?: kotlin.iosArm64())
    }
}

@Serializable
@SerialName("iosX64")
internal data class KotlinIosX64Target(
    override val targetName: String = "",
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinIosTarget() {

    context(Project)
    override fun applyTo() {
        super.applyTo(targetName.takeIf(String::isNotEmpty)?.let(kotlin::iosX64) ?: kotlin.iosX64())
    }
}

@Serializable
@SerialName("iosSimulatorArm64")
internal data class KotlinIosSimulatorArm64Target(
    override val targetName: String = "",
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinIosTarget() {

    context(Project)
    override fun applyTo() {
        super.applyTo(
            targetName.takeIf(String::isNotEmpty)?.let(kotlin::iosSimulatorArm64) ?: kotlin.iosSimulatorArm64()
        )
    }
}

@Serializable
@SerialName("watchos")
internal sealed class KotlinWatchosTarget : KotlinAppleTarget() {
    context(Project)
    override fun applyTo() {
        this::class.sealedSubclasses.forEach { clazz ->
            clazz.createInstance().applyTo()
        }
    }
}

@Serializable
@SerialName("watchosArm32")
internal data class KotlinWatchosArm32Target(
    override val targetName: String = "",
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinWatchosTarget() {

    context(Project)
    override fun applyTo() {
        super.applyTo(targetName.takeIf(String::isNotEmpty)?.let(kotlin::watchosArm32) ?: kotlin.watchosArm32())
    }
}

@Serializable
@SerialName("watchosArm64")
internal data class KotlinWatchosArm64Target(
    override val targetName: String = "",
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinWatchosTarget() {

    context(Project)
    override fun applyTo() {
        super.applyTo(targetName.takeIf(String::isNotEmpty)?.let(kotlin::watchosArm64) ?: kotlin.watchosArm64())
    }
}

@Serializable
@SerialName("watchosDeviceArm64")
internal data class KotlinWatchosDeviceArm64Target(
    override val targetName: String = "",
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinWatchosTarget() {

    context(Project)
    override fun applyTo() {
        super.applyTo(
            targetName.takeIf(String::isNotEmpty)?.let(kotlin::watchosDeviceArm64) ?: kotlin.watchosDeviceArm64()
        )
    }
}

@Serializable
@SerialName("watchosSimulatorArm64")
internal data class KotlinWatchosSimulatorX64Target(
    override val targetName: String = "",
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinWatchosTarget() {

    context(Project)
    override fun applyTo() {
        super.applyTo(targetName.takeIf(String::isNotEmpty)?.let(kotlin::watchosX64) ?: kotlin.watchosX64())
    }
}

@Serializable
@SerialName("watchosSimulatorArm64")
internal data class KotlinWatchosSimulatorArm64Target(
    override val targetName: String = "",
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinWatchosTarget() {

    context(Project)
    override fun applyTo() {
        super.applyTo(
            targetName.takeIf(String::isNotEmpty)?.let(kotlin::watchosSimulatorArm64) ?: kotlin.watchosSimulatorArm64()
        )
    }
}

@Serializable
@SerialName("tvos")
internal sealed class KotlinTvosTarget : KotlinAppleTarget() {
    context(Project)
    override fun applyTo() {
        this::class.sealedSubclasses.forEach { clazz ->
            clazz.createInstance().applyTo()
        }
    }
}

@Serializable
@SerialName("tvosArm64")
internal data class KotlinTvosArm64Target(
    override val targetName: String = "",
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinTvosTarget() {

    context(Project)
    override fun applyTo() {
        super.applyTo(targetName.takeIf(String::isNotEmpty)?.let(kotlin::tvosArm64) ?: kotlin.tvosArm64())
    }
}

@Serializable
@SerialName("tvosX64")
internal data class KotlinTvosX64Target(
    override val targetName: String = "",
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinTvosTarget() {

    context(Project)
    override fun applyTo() {
        super.applyTo(targetName.takeIf(String::isNotEmpty)?.let(kotlin::tvosX64) ?: kotlin.tvosX64())
    }
}

@Serializable
@SerialName("tvosSimulatorArm64")
internal data class KotlinTvosSimulatorArm64Target(
    override val targetName: String = "",
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinTvosTarget() {

    context(Project)
    override fun applyTo() {
        super.applyTo(
            targetName.takeIf(String::isNotEmpty)?.let(kotlin::tvosSimulatorArm64) ?: kotlin.tvosSimulatorArm64()
        )
    }
}

@Serializable
@SerialName("macos")
internal sealed class KotlinMacosTarget : KotlinAppleTarget() {
    context(Project)
    override fun applyTo() {
        this::class.sealedSubclasses.forEach { clazz ->
            clazz.createInstance().applyTo()
        }
    }
}

@Serializable
@SerialName("macosArm64")
internal data class KotlinMacosArm64Target(
    override val targetName: String = "",
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinMacosTarget() {

    context(Project)
    override fun applyTo() {
        super.applyTo(targetName.takeIf(String::isNotEmpty)?.let(kotlin::macosArm64) ?: kotlin.macosArm64())
    }
}

@Serializable
@SerialName("macosX64")
internal data class KotlinMacosX64Target(
    override val targetName: String = "",
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinMacosTarget() {

    context(Project)
    override fun applyTo() {
        super.applyTo(targetName.takeIf(String::isNotEmpty)?.let(kotlin::macosX64) ?: kotlin.macosX64())
    }
}

@Serializable
@SerialName("linux")
internal sealed class KotlinLinuxTarget : KotlinNativeTarget() {
    context(Project)
    override fun applyTo() {
        this::class.sealedSubclasses.forEach { clazz ->
            clazz.createInstance().applyTo()
        }
    }
}

@Serializable
@SerialName("linuxArm64")
internal data class KotlinLinuxArm64Target(
    override val targetName: String = "",
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinLinuxTarget() {

    context(Project)
    override fun applyTo() {
        super.applyTo(targetName.takeIf(String::isNotEmpty)?.let(kotlin::linuxArm64) ?: kotlin.linuxArm64())
    }
}

@Serializable
@SerialName("linuxX64")
internal data class KotlinLinuxX64Target(
    override val targetName: String = "",
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinLinuxTarget() {

    context(Project)
    override fun applyTo() {
        super.applyTo(targetName.takeIf(String::isNotEmpty)?.let(kotlin::linuxX64) ?: kotlin.linuxX64())
    }
}

@Serializable
@SerialName("mingw")
internal sealed class KotlinMingwTarget : KotlinNativeTarget() {
    context(Project)
    override fun applyTo() {
        this::class.sealedSubclasses.forEach { clazz ->
            clazz.createInstance().applyTo()
        }
    }
}

@Serializable
@SerialName("mingwX64")
internal data class KotlinMingwX64Target(
    override val targetName: String = "",
    override val compilerOptions: KotlinNativeCompilerOptions? = null,
    override val binaries: KotlinNativeBinaryContainer? = null,
) : KotlinNativeTarget() {

    context(Project)
    override fun applyTo() {
        super.applyTo(targetName.takeIf(String::isNotEmpty)?.let(kotlin::mingwX64) ?: kotlin.mingwX64())
    }
}

@Serializable
@SerialName("web")
internal sealed class KotlinJsTargetDsl : KotlinTarget(), KotlinTargetWithNodeJsDsl,
    HasBinaries<KotlinJsBinaryContainer>, HasConfigurableKotlinCompilerOptions<KotlinJsCompilerOptions> {

    abstract val moduleName: String?

    abstract val browser: KotlinJsBrowserDsl?

    abstract val useCommonJs: Boolean?

    abstract val useEsModules: Boolean?

    /**
     * The function accepts [jsExpression] and puts this expression as the "args: Array<String>" argument in place of main-function call
     */
    abstract val passAsArgumentToMainFunction: String?

    abstract val generateTypeScriptDefinitions: Boolean?

    context(Project)
    @OptIn(ExperimentalMainFunctionArgumentsDsl::class)
    protected fun applyTo(target: org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl) {
        super.applyTo(target)

        target.moduleName = moduleName ?: moduleName

        nodejs?.let { nodejs ->
            target.nodejs {
                nodejs.applyTo(this)
            }
        }

        browser?.let { browser ->
            target.browser {
                browser.applyTo(this)
            }
        }

        useCommonJs?.takeIf { it }?.run { target.useCommonJs() }
        useEsModules?.takeIf { it }?.run { target.useEsModules() }
        passAsArgumentToMainFunction?.let(target::passAsArgumentToMainFunction)
        generateTypeScriptDefinitions?.takeIf { it }?.let { target.generateTypeScriptDefinitions() }

        when (projectProperties.type) {
            ProjectType.APP ->
                binaries.executable.let { binaries ->
                    binaries.compilation?.let { compilation ->
                        target.binaries.executable(target.compilations.getByName(compilation))
                    } ?: target.binaries.executable()
                }

            ProjectType.LIB -> binaries.library.let { binaries ->
                binaries.compilation?.let { compilation ->
                    target.binaries.library(target.compilations.getByName(compilation))
                } ?: target.binaries.library()
            }

            else -> Unit
        }
    }


    context(Project)
    override fun applyTo() {
        this::class.sealedSubclasses.forEach { clazz ->
            clazz.createInstance().applyTo()
        }
    }
}


@Serializable
@SerialName("js")
internal data class KotlinJsTarget(
    override val targetName: String = "",
    override val nodejs: KotlinJsNodeDsl? = null,
    override val moduleName: String? = null,
    override val browser: KotlinJsBrowserDsl? = null,
    override val useCommonJs: Boolean? = null,
    override val useEsModules: Boolean? = null,
    override val passAsArgumentToMainFunction: String? = null,
    override val generateTypeScriptDefinitions: Boolean? = null,
    override val compilerOptions: KotlinJsCompilerOptions? = null,
    override val binaries: KotlinJsBinaryContainer = KotlinJsBinaryContainer(),
) : KotlinJsTargetDsl() {

    context(Project)
    override fun applyTo() {
        super.applyTo(targetName.takeIf(String::isNotEmpty)?.let(kotlin::js) ?: kotlin.js())
    }
}

@Serializable
@SerialName("wasmJs")
internal data class KotlinWasmJsTarget(
    override val targetName: String = "",
    override val nodejs: KotlinJsNodeDsl? = null,
    override val moduleName: String? = null,
    override val browser: KotlinJsBrowserDsl? = null,
    override val useCommonJs: Boolean? = null,
    override val useEsModules: Boolean? = null,
    override val passAsArgumentToMainFunction: String? = null,
    override val generateTypeScriptDefinitions: Boolean? = null,
    override val compilerOptions: KotlinJsCompilerOptions? = null,
    override val binaries: KotlinJsBinaryContainer = KotlinJsBinaryContainer(),
) : KotlinJsTargetDsl() {

    context(Project)
    override fun applyTo() {
        kotlin.wasmWasi()
        super.applyTo(targetName.takeIf(String::isNotEmpty)?.let(kotlin::wasmJs) ?: kotlin.wasmJs())
    }
}
