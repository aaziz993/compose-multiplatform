package gradle.plugins.android.application

import com.android.build.api.dsl.ApplicationBuildFeatures
import com.android.build.api.dsl.ApplicationBuildType
import com.android.build.api.dsl.ApplicationDefaultConfig
import com.android.build.api.dsl.ApplicationProductFlavor
import gradle.plugins.android.TestedExtension

/**
 * The `android` extension for application plugins.
 *
 *
 * For the base module, see [com.android.build.gradle.BaseExtension]
 *
 *
 * For optional apks, this class is used directly.
 */
internal abstract class AbstractAppExtension : TestedExtension<
    ApplicationBuildFeatures,
    ApplicationBuildType,
    ApplicationDefaultConfig,
    ApplicationProductFlavor>()
