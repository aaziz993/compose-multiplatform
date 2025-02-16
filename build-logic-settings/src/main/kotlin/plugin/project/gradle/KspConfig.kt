package plugin.project.gradle

import com.google.devtools.ksp.gradle.KspExtension
import gradle.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.apply

internal fun Project.configureKsp() {
    apply(plugin=libs.plugins.ksp.get().pluginId)

    extensions.configure<KspExtension>(::configureKspExtension)
}

private fun Project.configureKspExtension(extension: KspExtension): KspExtension =
    extension.apply {
        // 0 - Turn off all Ktorfit related error checking, 1 - Check for errors, 2 - Turn errors into warnings
        arg("Ktorfit_Errors", "1")
        // Compile Safety - check your Koin config at compile time (since 1.3.0)
        arg("KOIN_CONFIG_CHECK", "true")
        arg("KOIN_DEFAULT_MODULE", "false")
        // to generate viewModel Koin definition with org.koin.compose.viewmodel.dsl.viewModel instead of regular org.koin.androidx.viewmodel.dsl.viewModel
        arg("KOIN_USE_COMPOSE_VIEWMODEL", "true")
    }

