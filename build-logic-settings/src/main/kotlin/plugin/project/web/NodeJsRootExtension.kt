package plugin.project.web

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

internal fun Project.configureNodeJsRootExtension(extension: NodeJsRootExtension) = extension.apply {
    version = providers.gradleProperty("node.version").get() // https://nodejs.org/en/download/package-manager

    versions.apply {
        webpack.version = providers.gradleProperty("webpack.version").get() // https://www.npmjs.com/package/webpack
        webpackCli.version = providers.gradleProperty("webpackCli.version").get() // https://www.npmjs.com/package/webpack-cli
        karma.version = providers.gradleProperty("karma.version").get() // https://www.npmjs.com/package/karma

        // synchronize TypeScript version
        // to simplify converting of kotlin-typescript subproject
        typescript.version = providers.gradleProperty("typescript.version").get()
    }
}
