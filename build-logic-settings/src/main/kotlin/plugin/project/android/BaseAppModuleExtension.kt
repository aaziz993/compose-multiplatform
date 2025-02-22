package plugin.project.android

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project

internal fun Project.configureBaseAppModuleExtension(extension: BaseAppModuleExtension) = extension.apply {
    defaultConfig {
        applicationId = group.toString()
    }

    buildTypes {
        getByName(BuildType.RELEASE.applicationIdSuffix) {
            isShrinkResources = true
        }
        getByName(BuildType.DEBUG.applicationIdSuffix) {
            isShrinkResources = false
        }
    }

    buildFeatures {
        compose = true
    }
}
