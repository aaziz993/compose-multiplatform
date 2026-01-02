package klib.data.webrtc

public interface MediaDevices {
    public suspend fun getUserMedia(
        streamConstraints: MediaStreamConstraintsBuilder.() -> Unit = {},
    ): MediaStream

    public suspend fun getUserMedia(
        audio: Boolean = false,
        video: Boolean = false,
    ): MediaStream =
        getUserMedia {
            if (audio) audio()
            if (video) video()
        }

    public suspend fun getDisplayMedia(): MediaStream

    public suspend fun supportsDisplayMedia(): Boolean

    public suspend fun enumerateDevices(): List<MediaDeviceInfo>

    public companion object : MediaDevices by mediaDevices
}

internal expect val mediaDevices: MediaDevices
