package gradle.plugins.android

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

public val Project.androidComponents: ApplicationAndroidComponentsExtension get() = the()

public fun Project.androidComponents(configure: ApplicationAndroidComponentsExtension.() -> Unit): Unit =
    extensions.configure(configure)
