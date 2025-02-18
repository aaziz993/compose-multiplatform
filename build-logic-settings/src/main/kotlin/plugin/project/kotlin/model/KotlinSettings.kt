package plugin.project.kotlin.model

import kotlinx.serialization.Serializable
import plugin.project.kotlin.allopen.model.AllOpenSettings
import plugin.project.kotlin.apollo.model.ApolloSettings
import plugin.project.kotlin.atomicfu.model.AtomicFUSettings
import plugin.project.kotlin.ksp.model.KspSettings
import plugin.project.kotlin.ktorfit.model.KtorfitSettings
import plugin.project.kotlin.noarg.model.NoArgSettings
import plugin.project.kotlin.powerassert.model.PowerAssertSettings
import plugin.project.kotlin.room.model.RoomSettings
import plugin.project.kotlin.rpc.model.RpcSettings
import plugin.project.kotlin.sqldelight.model.SqlDelightSettings

@Serializable
internal data class KotlinSettings(
    val ksp2: KspSettings = KspSettings(),
    val allOpen: AllOpenSettings = AllOpenSettings(),
    val noArg: NoArgSettings = NoArgSettings(),
    val atomicFU: AtomicFUSettings = AtomicFUSettings(),
    val ktorfit: KtorfitSettings = KtorfitSettings(),
    val apollo: ApolloSettings = ApolloSettings(),
    val rpc: RpcSettings = RpcSettings(),
    val sqldelight: SqlDelightSettings = SqlDelightSettings(),
    val room: RoomSettings = RoomSettings(),
    val powerAssert: PowerAssertSettings = PowerAssertSettings(),
)
