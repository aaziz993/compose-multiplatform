package klib.data.webrtc

public interface VideoTrack : MediaStreamTrack {

    public suspend fun switchCamera(deviceId: String? = null)
}
