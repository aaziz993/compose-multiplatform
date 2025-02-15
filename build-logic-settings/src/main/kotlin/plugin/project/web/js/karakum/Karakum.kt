package plugin.project.web.js.karakum

import io.github.sgrishchenko.karakum.gradle.plugin.KarakumExtension
import io.github.sgrishchenko.karakum.gradle.plugin.tasks.KarakumGenerate
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

internal fun Project.configureKarakum(){
    extensions.configure<KarakumExtension>(::configureKarakumExtension)

    tasks.withType<KarakumGenerate>(::configureKarakumGenerateTask)
}
