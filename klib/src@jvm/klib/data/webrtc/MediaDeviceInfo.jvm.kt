package klib.data.webrtc

import dev.onvoid.webrtc.media.Device

internal fun Device.toMediaDeviceInfo(kind: MediaDeviceKind): MediaDeviceInfo =
    MediaDeviceInfo(
        descriptor,
        name,
        kind,
    )
