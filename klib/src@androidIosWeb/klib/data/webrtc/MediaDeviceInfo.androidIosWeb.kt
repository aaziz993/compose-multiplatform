package klib.data.webrtc

import com.shepeliev.webrtckmp.MediaDeviceInfo as KotlinMediaDeviceInfo
import com.shepeliev.webrtckmp.MediaDeviceKind as KotlinMediaDeviceKind

internal fun MediaDeviceInfo.toKotlinMediaDeviceInfo(): KotlinMediaDeviceInfo =
    KotlinMediaDeviceInfo(
        deviceId,
        label,
        kind.toKotlinMediaDeviceKind(),
    )

internal fun KotlinMediaDeviceInfo.toMediaDeviceInfo(): MediaDeviceInfo =
    MediaDeviceInfo(
        deviceId,
        label,
        kind.toMediaDeviceKind(),
    )

internal fun MediaDeviceKind.toKotlinMediaDeviceKind(): KotlinMediaDeviceKind = when (this) {
    MediaDeviceKind.VideoInput -> KotlinMediaDeviceKind.VideoInput
    MediaDeviceKind.AudioInput -> KotlinMediaDeviceKind.AudioInput
}

internal fun KotlinMediaDeviceKind.toMediaDeviceKind(): MediaDeviceKind = when (this) {
    KotlinMediaDeviceKind.VideoInput -> MediaDeviceKind.VideoInput
    KotlinMediaDeviceKind.AudioInput -> MediaDeviceKind.AudioInput
}
