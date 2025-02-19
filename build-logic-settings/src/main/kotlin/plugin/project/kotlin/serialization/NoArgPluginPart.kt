//package plugin.project.kotlin.serialization
//
//import gradle.moduleProperties
//import gradle.libs
//import plugin.project.BindingPluginPart
//import org.gradle.api.Project
//
//internal class NoArgPluginPart(override val project: Project) : BindingPluginPart {
//
//    override val needToApply: Boolean by lazy {
//        project.moduleProperties.settings.kotlin.noArg.enabled
//    }
//
//    override fun applyAfterEvaluate() {
//        super.applyAfterEvaluate()
//
//        project.plugins.apply(project.libs.plugins.allopen.get().pluginId)
//
//        applySettings()
//    }
//
//    private fun applySettings() = with(project) {
//        configureNoArgExtension()
//    }
//}
