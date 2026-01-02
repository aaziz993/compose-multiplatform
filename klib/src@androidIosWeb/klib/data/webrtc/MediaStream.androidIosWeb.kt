package klib.data.webrtc

import com.shepeliev.webrtckmp.MediaStream as KotlinMediaStream
import com.shepeliev.webrtckmp.MediaStreamTrack as KotlinMediaStreamTrack

public actual class MediaStream(
    private val delegate: KotlinMediaStream
) {

    public actual constructor() : this(KotlinMediaStream())

    public actual val id: String
        get() = delegate.id
    public actual val tracks: List<MediaStreamTrack>
        get() = delegate.tracks.map(KotlinMediaStreamTrack::toMediaStreamTrack)

    public actual fun addTrack(track: MediaStreamTrack): Unit =
        delegate.addTrack(track.toKotlinMediaStreamTrack())

    public actual fun getTrackById(id: String): MediaStreamTrack? = delegate.getTrackById(id)?.toMediaStreamTrack()

    public actual fun removeTrack(track: MediaStreamTrack): Unit = delegate.removeTrack(track.toKotlinMediaStreamTrack())

    public actual fun release(): Unit = delegate.release()
}

internal fun KotlinMediaStream.toMediaStream(): MediaStream = MediaStream(this)
