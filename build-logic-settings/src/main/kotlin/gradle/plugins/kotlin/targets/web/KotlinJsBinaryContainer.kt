package gradle.plugins.kotlin.targets.web

import org.gradle.api.Project

internal typealias KotlinJsBinaryContainer = Set<JsIrBinary<out org.jetbrains.kotlin.gradle.targets.js.ir.JsIrBinary>>

context(Project)
internal fun KotlinJsBinaryContainer.applyTo(receiver: org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsBinaryContainer) {
    forEach { binary ->
        binary as JsIrBinary<org.jetbrains.kotlin.gradle.targets.js.ir.JsIrBinary>
        when (binary) {
            is Executable, is ExecutableWasm -> receiver.executable(receiver.target.compilations.getByName(binary.compilation))
            is Library, is LibraryWasm -> receiver.library(receiver.target.compilations.getByName(binary.compilation))
        }.forEach {
            binary.applyTo(it)
        }
    }
}
