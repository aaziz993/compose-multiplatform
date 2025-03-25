package gradle.plugins.android.device

import com.android.build.api.dsl.ManagedDevices
import gradle.api.applyTo
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/** Options for Managed Devices */
@Serializable
@Suppress("UnstableApiUsage")
internal data class ManagedDevices(
    /**
     * List of test devices for this project for use with the Unified Test Platform
     *
     * These APIs are experimental and may change without notice.
     */
    val allDevices: Set<DeviceImpl>? = null,
    /**
     * List of test devices for this project for use with the Unified Test Platform
     *
     * This is replaced with [allDevices]
     */
    val devices: Set<DeviceImpl>? = null,
    /**
     * Convenience container for specifying managed devices of type [ManagedVirtualDevice].
     *
     * This list is managed in sync with [allDevices]. [ManagedVirtualDevice] definitions added or
     * removed in this container are correspondingly handled in [allDevices], and vice versa.
     */
    val localDevices: Set<ManagedVirtualDevice>? = null,
    /**
     * List of DeviceGroups to create tasks for.
     *
     * These APIs are experimental and may change without notice.
     */
    val groups: Set<DeviceGroup>? = null,
) {

    context(Project)
    fun applyTo(receiver: ManagedDevices) {
        allDevices?.forEach { device ->
            device.applyTo(receiver.allDevices)
        }

        devices?.forEach { device ->
            device.applyTo(receiver.devices)
        }

        localDevices?.forEach { localDevice ->
            localDevice.applyTo(receiver.localDevices)
        }

        groups?.forEach { group ->
            group.applyTo(receiver.groups)
        }
    }
}
