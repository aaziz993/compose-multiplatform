package gradle.api.rpc

import kotlinx.rpc.RpcExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

public val Project.rpc: RpcExtension get() = the()

public fun Project.rpc(configure: RpcExtension.() -> Unit): Unit = extensions.configure(configure)
