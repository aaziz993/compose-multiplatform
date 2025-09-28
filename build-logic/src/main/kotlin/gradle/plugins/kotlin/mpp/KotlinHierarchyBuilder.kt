package gradle.plugins.kotlin.mpp

import gradle.api.project.kotlin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyBuilder

context(project: Project)
public fun KotlinHierarchyBuilder.withAndroidTarget(group: String) {
    // Trick to integrate androidTarget with default hierarchy template when used new android kotlin multiplatform library plugin.
    if (project.pluginManager.hasPlugin("com.android.kotlin.multiplatform.library")) {
        project.kotlin.sourceSets.apply {
            val androidMain by getting
            val androidHostTest by getting
            val androidDeviceTest by getting

            val groupMain = getByName("${group}Main")
            val groupTest = getByName("${group}Test")

            androidMain.dependsOn(groupMain)
            androidHostTest.dependsOn(groupTest)
            androidDeviceTest.dependsOn(groupTest)
        }
    }
    else withAndroidTarget()
}
