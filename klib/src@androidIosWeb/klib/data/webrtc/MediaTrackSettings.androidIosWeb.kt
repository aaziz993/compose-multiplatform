package klib.data.webrtc

import com.shepeliev.webrtckmp.MediaTrackSettings as KotlinMediaTrackSettings

internal fun MediaTrackSettings.toKotlinMediaTrackSettings(): KotlinMediaTrackSettings =
    KotlinMediaTrackSettings(
        aspectRatio,
        autoGainControl,
        channelCount,
        deviceId,
        echoCancellation,
        facingMode?.toKotlinFacingMode(),
        frameRate,
        groupId,
        height,
        latency,
        noiseSuppression,
        sampleRate,
        sampleSize,
        width,
    )

internal fun KotlinMediaTrackSettings.toMediaTrackSettings(): MediaTrackSettings =
    MediaTrackSettings(
        aspectRatio,
        autoGainControl,
        channelCount,
        deviceId,
        echoCancellation,
        facingMode?.toFacingMode(),
        frameRate,
        groupId,
        height,
        latency,
        noiseSuppression,
        sampleRate,
        sampleSize,
        width,
    )
