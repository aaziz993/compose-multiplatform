package gradle.model.kotlin.kmp.web

import gradle.model.HasBinaries
import gradle.model.kotlin.kmp.KotlinTarget

internal interface KotlinWasmTargetDsl : KotlinTarget, HasBinaries<KotlinJsBinaryContainer> {

}
