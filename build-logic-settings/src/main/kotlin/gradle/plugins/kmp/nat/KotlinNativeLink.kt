package gradle.plugins.kmp.nat

import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompileTool
import org.jetbrains.kotlin.gradle.tasks.CompilerPluginOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilerPluginData
import org.jetbrains.kotlin.project.model.LanguageSettings

/**
 * A task producing a final binary from a compilation.
 */
@Serializable
internal data class KotlinNativeLink(

    @Transient // This property can't be accessed in the execution phase
    val binary: NativeBinary,
) : AbstractKotlinCompileTool<K2NativeCompilerArguments>(objectFactory),
    UsesKonanPropertiesBuildService,
    UsesBuildMetricsService,
    UsesClassLoadersCachingBuildService,
    KotlinToolTask<KotlinCommonCompilerToolOptions>,
    UsesKotlinNativeBundleBuildService,
    UsesBuildFusService
{


    final override val toolOptions: KotlinCommonCompilerToolOptions = objectFactory
        .newInstance<KotlinCommonCompilerToolOptionsDefault>()

    override val destinationDirectory: DirectoryProperty = binary.outputDirectoryProperty

    override val libraries: ConfigurableFileCollection = objectFactory.fileCollection().from(
        {
            // Avoid resolving these dependencies during task graph construction when we can't build the target:
            @Suppress("DEPRECATION")
            if (konanTarget.enabledOnCurrentHostForBinariesCompilation()) compilation.compileDependencyFiles
            else objectFactory.fileCollection()
        }
    )





    val apiFiles: List<String>?=null

    val compilerPluginOptions:CompilerPluginOptions?=null,



     var compilerPluginClasspath: List<String>? = null

    /**
     * Plugin Data provided by [KpmCompilerPlugin]
     */
    @get:Optional
    @get:Nested
    var kotlinPluginData: KotlinCompilerPluginData? = null

}
