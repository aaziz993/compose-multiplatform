package plugin.project.apple.cocoapods

import gradle.libs
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension

internal fun Project.configureCocoapodsExtension(
    extension: CocoapodsExtension
) = extension.apply {

    version = libs.versions.cocoapods.version.get().toString() // cocoapods gradle.version

    authors = providers.gradleProperty("project.developer.name").get()

    podfile = project.file("../iosApp/Podfile")

    name = this@configureCocoapodsExtension.name

//    license = providers.gradleProperty("project.license.text.url").get().readUrlText(
//        providers.gradleProperty("project.license.fallback.file").get(),
//    )

    summary = providers.gradleProperty("cocoapods.summary").get()

    homepage = providers.gradleProperty("cocoapods.homepage").get()

    source = providers.gradleProperty("cocoapods.source").orNull

    ios.deploymentTarget = libs.versions.ios.deployment.target.get().toString() // iOS deployment target

    framework {
        baseName = this@apply.name
        isStatic = true //static or dynamic according to your project
        // Add it to avoid sqllite3 issues in iOS. Required when using NativeSQLiteDriver
        linkerOpts.add("-lsqlite3")
    }
}
