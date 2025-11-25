package gradle.api.kotlin.mpp

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

public val Project.kotlin: KotlinMultiplatformExtension get() = the()

public fun Project.kotlin(configure: KotlinMultiplatformExtension.() -> Unit): Unit =
    extensions.configure(configure)

public fun NamedDomainObjectContainer<KotlinSourceSet>.commonMain(
    action: KotlinSourceSet.() -> Unit,
): NamedDomainObjectProvider<KotlinSourceSet> =
    named(KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME, action)

public fun NamedDomainObjectContainer<KotlinSourceSet>.commonTest(
    action: KotlinSourceSet.() -> Unit,
): NamedDomainObjectProvider<KotlinSourceSet> =
    named(KotlinSourceSet.COMMON_TEST_SOURCE_SET_NAME, action)

