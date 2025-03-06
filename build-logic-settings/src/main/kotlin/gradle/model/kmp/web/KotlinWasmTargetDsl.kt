package gradle.model.kmp.web

import gradle.model.HasBinaries
import gradle.model.kmp.KotlinTarget

internal interface KotlinWasmTargetDsl : KotlinTarget, HasBinaries<KotlinJsBinaryContainer> {

}
