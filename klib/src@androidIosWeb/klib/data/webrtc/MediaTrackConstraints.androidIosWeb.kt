package klib.data.webrtc

import com.shepeliev.webrtckmp.MediaTrackConstraints as KotlinMediaTrackConstraints
import com.shepeliev.webrtckmp.ValueOrConstrain as KotlinValueOrConstrain
import com.shepeliev.webrtckmp.FacingMode as KotlinFacingMode

public fun MediaTrackConstraints.toKotlinMediaTrackConstraints(): KotlinMediaTrackConstraints =
    KotlinMediaTrackConstraints(
        aspectRatio?.toKotlinValueOrConstrain(),
        autoGainControl?.toKotlinValueOrConstrain(),
        channelCount?.toKotlinValueOrConstrain(),
        deviceId,
        echoCancellation?.toKotlinValueOrConstrain(),
        facingMode?.toKotlinValueOrConstrain(FacingMode::toKotlinFacingMode),
        frameRate?.toKotlinValueOrConstrain(),
        groupId,
        height?.toKotlinValueOrConstrain(),
        latency?.toKotlinValueOrConstrain(),
        noiseSuppression?.toKotlinValueOrConstrain(),
        sampleRate?.toKotlinValueOrConstrain(),
        sampleSize?.toKotlinValueOrConstrain(),
        width?.toKotlinValueOrConstrain(),
        highpassFilter?.toKotlinValueOrConstrain(),
        typingNoiseDetection?.toKotlinValueOrConstrain(),
    )

public fun KotlinMediaTrackConstraints.toMediaTrackConstraints(): MediaTrackConstraints =
    MediaTrackConstraints(
        aspectRatio?.toValueOrConstrain(),
        autoGainControl?.toValueOrConstrain(),
        channelCount?.toValueOrConstrain(),
        deviceId,
        echoCancellation?.toValueOrConstrain(),
        facingMode?.toValueOrConstrain(KotlinFacingMode::toFacingMode),
        frameRate?.toValueOrConstrain(),
        groupId,
        height?.toValueOrConstrain(),
        latency?.toValueOrConstrain(),
        noiseSuppression?.toValueOrConstrain(),
        sampleRate?.toValueOrConstrain(),
        sampleSize?.toValueOrConstrain(),
        width?.toValueOrConstrain(),
        highpassFilter?.toValueOrConstrain(),
        typingNoiseDetection?.toValueOrConstrain(),
    )

public fun FacingMode.toKotlinFacingMode(): KotlinFacingMode =
    when (this) {
        FacingMode.User -> KotlinFacingMode.User
        FacingMode.Environment -> KotlinFacingMode.Environment
    }

public fun KotlinFacingMode.toFacingMode(): FacingMode =
    when (this) {
        KotlinFacingMode.User -> FacingMode.User
        KotlinFacingMode.Environment -> FacingMode.Environment
    }

@Suppress("UNCHECKED_CAST")
public fun <T, R> ValueOrConstrain<T>.toKotlinValueOrConstrain(
    transform: (T) -> R = { it as R }
): KotlinValueOrConstrain<R> =
    when (this) {
        is ValueOrConstrain.Value -> KotlinValueOrConstrain.Value(transform(value))
        is ValueOrConstrain.Constrain ->
            KotlinValueOrConstrain.Constrain(exact?.let(transform), ideal?.let(transform))
    }

@Suppress("UNCHECKED_CAST")
public fun <T, R> KotlinValueOrConstrain<T>.toValueOrConstrain(
    transform: (T) -> R = { it as R }
): ValueOrConstrain<R> =
    when (this) {
        is KotlinValueOrConstrain.Value -> ValueOrConstrain.Value(transform(value))
        is KotlinValueOrConstrain.Constrain ->
            ValueOrConstrain.Constrain(exact?.let(transform), ideal?.let(transform))
    }
