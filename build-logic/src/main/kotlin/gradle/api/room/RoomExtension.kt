package gradle.api.room

import androidx.room.gradle.RoomExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

public val Project.room: RoomExtension get() = the()

public fun Project.room(configure: RoomExtension.() -> Unit): Unit = extensions.configure(configure)
