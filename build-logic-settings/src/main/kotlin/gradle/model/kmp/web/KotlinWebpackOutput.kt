package gradle.model.kmp.web

import gradle.trySet
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackOutput

@Serializable
internal data class KotlinWebpackOutput(
    val library: String? = null,
    val libraryTarget: String? = null,
    val globalObject: String = "globalThis"
) {

    fun applyTo(
        webpackOutput: KotlinWebpackOutput
    ) {
        webpackOutput::library trySet library
        webpackOutput::libraryTarget trySet libraryTarget
        webpackOutput::globalObject trySet globalObject
    }

    fun toKotlinWebPackOutput() = KotlinWebpackOutput(
        library,
        libraryTarget,
        globalObject,
    )
}
