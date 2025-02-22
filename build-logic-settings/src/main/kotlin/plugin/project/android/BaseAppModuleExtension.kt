package plugin.project.android

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project

internal fun Project.configureBaseAppModuleExtension(extension: BaseAppModuleExtension) = extension.apply {
    defaultConfig {
        applicationId = group.toString()
    }

    buildTypes {
        getByName("release") {
            isShrinkResources = true
        }
        getByName("debug") {
            isShrinkResources = false
        }
    }

    buildFeatures {
        compose = true
    }
}
