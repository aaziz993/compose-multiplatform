package plugin.project.kmp.model.web

import plugin.project.gradle.model.HasBinaries
import plugin.project.kmp.model.KotlinTarget

internal interface KotlinWasmTargetDsl : KotlinTarget, HasBinaries<KotlinJsBinaryContainer> {

}
