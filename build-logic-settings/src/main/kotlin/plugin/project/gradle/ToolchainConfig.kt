package plugin.project.gradle

import gradle.versionCatalog
import org.gradle.api.initialization.Settings
import org.gradle.api.toolchain.management.ToolchainManagement
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.jvm

@Suppress("UnstableApiUsage")
internal fun Settings.configureToolchain() {
    apply(plugin = versionCatalog.getTable("plugins")!!.getTable("foojay-resolver-convention")!!.getString("id")!!)

    extensions.configure<ToolchainManagement>(::configureToolchainManagement)
}

@Suppress("UnstableApiUsage")
internal fun Settings.configureToolchainManagement(toolchainManagement: ToolchainManagement) = toolchainManagement.apply {
    jvm {
        javaRepositories {
//            repository("foojay") {
//                resolverClass=FoojayToolchainResolver::class.java
//            }
        }
    }
}

