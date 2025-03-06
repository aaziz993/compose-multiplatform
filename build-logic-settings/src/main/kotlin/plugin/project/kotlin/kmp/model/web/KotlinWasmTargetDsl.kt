package plugin.project.kotlin.kmp.model.web

import plugin.project.gradle.model.HasBinaries
import plugin.project.kotlin.kmp.model.KotlinTarget

internal interface KotlinWasmTargetDsl : KotlinTarget, HasBinaries<KotlinJsBinaryContainer> {

}
