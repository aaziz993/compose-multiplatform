package plugin.multiplatform.web.model

import org.gradle.api.Project

internal enum class JsPlatform {
    WEB,
    BROWSER,
    NODE
}
internal val Project.jsPlatform: JsPlatform
    get() = (findProperty("js.platform") as String?)?.let(JsPlatform::valueOf) ?: JsPlatform.BROWSER
