package gradle

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

private fun KotlinMultiplatformExtension.maybeCreateSourceSet(
    kotlinSourceSetName: String,
    block: KotlinSourceSet.() -> Unit,
) {
    val sourceSet = sourceSets.maybeCreate(kotlinSourceSetName)
    sourceSet.block()
}
