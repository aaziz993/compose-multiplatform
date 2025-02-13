package plugin.project.amperlike.init

import java.nio.file.Path
import java.util.*
import org.jetbrains.amper.core.Result
import org.jetbrains.amper.core.UsedInIdePlugin
import org.jetbrains.amper.core.amperFailure
import org.jetbrains.amper.core.flatMap
import org.jetbrains.amper.core.messages.BuildProblemImpl
import org.jetbrains.amper.core.messages.GlobalBuildProblemSource
import org.jetbrains.amper.core.messages.Level
import org.jetbrains.amper.core.messages.NonIdealDiagnostic
import org.jetbrains.amper.core.messages.ProblemReporterContext
import org.jetbrains.amper.frontend.FrontendApiBundle
import org.jetbrains.amper.frontend.VersionCatalog
import plugin.project.amperlike.model.AmperLikeModel
import plugin.project.amperlike.model.AmperLikeTemplate
import org.jetbrains.amper.core.asAmperSuccess

internal interface ModelInit {
    companion object {
        const val MODEL_NAME_ENV = "NON_AMPER_MODEL_TYPE"

        const val MODEL_NAME_PROPERTY = "org.jetbrains.amper.like.model.type"

        context(ProblemReporterContext)
        @OptIn(NonIdealDiagnostic::class)
        private fun load(loader: ClassLoader): Result<ModelInit> {
            val services = ServiceLoader.load(ModelInit::class.java, loader).associateBy { it.name }
            if (services.isEmpty()) {
                problemReporter.reportMessage(
                    BuildProblemImpl(
                        buildProblemId = "no.model.init.service.found",
                        source = GlobalBuildProblemSource,
                        message = FrontendApiBundle.message("no.model.init.service.found"),
                        level = Level.Fatal,
                    )
                )
                return amperFailure()
            }

            val modelName = System.getProperty(MODEL_NAME_PROPERTY)
                ?: System.getenv(MODEL_NAME_ENV)
                ?: "non-amper-schema-based"

            println("Model '$modelName' is loaded with ${services.iterator().asSequence()}")

            val service = services[modelName]
            return if (service == null) {
                problemReporter.reportMessage(
                    BuildProblemImpl(
                        buildProblemId = "model.not.found",
                        source = GlobalBuildProblemSource,
                        message = FrontendApiBundle.message("model.not.found", modelName),
                        level = Level.Fatal,
                    )
                )
                amperFailure()
            } else {
                Result.success(service)
            }
        }

        /**
         * Initializes an Amper model in the context of a Gradle-based project.
         *
         * @param rootProjectDir The directory of the Gradle root project.
         * @param subprojectDirs The directories of all Gradle subprojects declared in the Gradle settings.
         * @param loader The ClassLoader from which to load the implementation of [ModelInit].
         */
        context(ProblemReporterContext)
        fun getGradleAmperLikeModel(
            rootProjectDir: Path,
            subprojectDirs: List<Path>,
            loader: ClassLoader = Thread.currentThread().contextClassLoader,
        ): Result<AmperLikeModel> = load(loader).flatMap { it.getGradleAmperLikeModel(rootProjectDir, subprojectDirs) }
    }

    /**
     * A way to distinguish different models.
     */
    val name: String

    /**
     * Initializes an Amper model in the context of a Gradle-based project.
     *
     * @param rootProjectDir The directory of the Gradle root project.
     * @param subprojectDirs The directories of all Gradle subprojects declared in the Gradle settings.
     */
    context(ProblemReporterContext)
    fun getGradleAmperLikeModel(rootProjectDir: Path, subprojectDirs: List<Path>): Result<AmperLikeModel>

    /**
     * Wrapper class to hold info about requested template.
     */
    data class AmperLikeTemplateHolder(
        val template: AmperLikeTemplate,
        @UsedInIdePlugin
        val chosenCatalog: VersionCatalog?,
    )
}



// The ServiceLoader mechanism requires a no-arg constructor, which doesn't work with Kotlin objects.
// This proxy allows to provide an instantiable class that delegates everything to the SchemaBasedModelImport object.
//internal class SchemaBasedModelImportServiceProxy : ModelInit by SchemaBasedModelImport

//internal object SchemaBasedModelImport : ModelInit {
//    override val name = "schema-based"
//
//    context(ProblemReporterContext)
//    override fun getGradleAmperLikeModel(rootProjectDir: Path, subprojectDirs: List<Path>): Result<AmperLikeModel> =
//        getModel(GradleAmperLikeProjectContext.fromNio(rootProjectDir, subprojectDirs))
//
//    context(ProblemReporterContext)
//    fun getModel(projectContext: AmperProjectContext): Result<AmperLikeModel> {
//        val resultModules = doBuild(projectContext)
//            ?: return amperFailure()
//        val model = DefaultModel(projectContext.projectRootDir.toNioPath(), resultModules)
//        AomModelDiagnosticFactories.forEach { diagnostic ->
//            with(diagnostic) { model.analyze() }
//        }
//        return model.asAmperSuccess()
//    }
//
//    @UsedInIdePlugin
//    fun getTemplate(templatePsiFile: PsiFile, project: Project): ModelInit.TemplateHolder? {
//        return readTemplate(GradleVersionsCatalogFinder(FrontendPathResolver(project = project)), templatePsiFile.virtualFile)
//    }
//}
