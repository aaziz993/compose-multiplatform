package gradle.plugins.compose

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.android.AndroidExtension
import org.jetbrains.compose.desktop.DesktopExtension
import org.jetbrains.compose.resources.ResourcesExtension

public val ComposeExtension.desktop: DesktopExtension get() = the()

public fun ComposeExtension.desktop(configure: DesktopExtension.() -> Unit): Unit =
    extensions.configure(configure)

public val ComposeExtension.android: AndroidExtension get() = the()

public fun ComposeExtension.android(configure: AndroidExtension.() -> Unit): Unit =
    extensions.configure(configure)


public val ComposeExtension.resources: ResourcesExtension get() = the()

public fun ComposeExtension.resources(configure: ResourcesExtension.() -> Unit): Unit =
    extensions.configure(configure)

public val Project.compose: ComposeExtension get() = the()

public fun Project.compose(configure: ComposeExtension.() -> Unit): Unit = extensions.configure(configure)

