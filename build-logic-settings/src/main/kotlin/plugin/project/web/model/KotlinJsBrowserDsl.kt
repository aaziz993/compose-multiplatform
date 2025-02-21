package plugin.project.web.model

internal interface KotlinJsBrowserDsl : KotlinJsSubTargetDsl {
    val commonWebpackConfig:KotlinWebpackConfig?

    val runTask:KotlinWebpack?

    val webpackTask:KotlinWebpack?
}
