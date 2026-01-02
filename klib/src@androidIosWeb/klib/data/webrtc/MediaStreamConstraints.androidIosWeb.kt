package klib.data.webrtc

import com.shepeliev.webrtckmp.MediaStreamConstraints as KotlinMediaStreamConstraints

public fun MediaStreamConstraints.toKotlinMediaStreamConstraints(): KotlinMediaStreamConstraints =
    KotlinMediaStreamConstraints(
        audio?.toKotlinMediaTrackConstraints(),
        video?.toKotlinMediaTrackConstraints(),
    )

public fun KotlinMediaStreamConstraints.toMediaStreamConstraints(): MediaStreamConstraints =
    MediaStreamConstraints(
        audio?.toMediaTrackConstraints(),
        video?.toMediaTrackConstraints(),
    )
