package gradle.model.android.application

/**
 * AppExtension is used directly by build.gradle.kts when configuring project so adding generics
 * declaration is not possible.
 */
internal abstract class AppExtension : AbstractAppExtension()
