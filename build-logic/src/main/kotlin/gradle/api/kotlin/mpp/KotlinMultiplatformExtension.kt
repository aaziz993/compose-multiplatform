package gradle.api.kotlin.mpp

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

public val Project.kotlin: KotlinMultiplatformExtension get() = the()

public fun Project.kotlin(configure: KotlinMultiplatformExtension.() -> Unit): Unit =
    extensions.configure(configure)

public val KotlinMultiplatformExtension.commonMainSourceSet: KotlinSourceSet
    get() = sourceSets.single { sourceSet -> sourceSet.name == KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME }

public val KotlinMultiplatformExtension.commonTestSourceSet: KotlinSourceSet
    get() = sourceSets.single { sourceSet -> sourceSet.name == KotlinSourceSet.COMMON_TEST_SOURCE_SET_NAME }

