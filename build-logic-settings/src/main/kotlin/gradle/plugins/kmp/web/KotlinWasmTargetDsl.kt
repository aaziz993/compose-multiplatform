package gradle.plugins.kmp.web

import gradle.plugins.HasBinaries
import gradle.plugins.kmp.KotlinTarget

internal interface KotlinWasmTargetDsl : KotlinTarget, HasBinaries<KotlinJsBinaryContainer> {

}
