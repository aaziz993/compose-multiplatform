package klib.data.webrtc

import klib.data.type.collections.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import com.shepeliev.webrtckmp.MediaStreamTrack as KotlinMediaStreamTrack
import com.shepeliev.webrtckmp.MediaStreamTrackKind as KotlinMediaStreamTrackKind
import com.shepeliev.webrtckmp.MediaTrackConstraints as KotlinMediaTrackConstraints
import com.shepeliev.webrtckmp.MediaStreamTrackState as KotlinMediaStreamTrackState
import com.shepeliev.webrtckmp.MediaTrackSettings as KotlinMediaTrackSettings

internal fun KotlinMediaStreamTrack.toMediaStreamTrack(): MediaStreamTrack =
    MediaStreamTrackImpl(this)

private class MediaStreamTrackImpl(
    val delegate: KotlinMediaStreamTrack,
) : MediaStreamTrack {

    override val id: String
        get() = delegate.id

    override val kind: MediaStreamTrackKind
        get() = delegate.kind.toMediaStreamTrackKind()

    override val label: String
        get() = delegate.label

    override var enabled: Boolean
        get() = delegate.enabled
        set(value) {
            delegate.enabled = value
        }

    override val state: StateFlow<MediaStreamTrackState> =
        delegate.state
            .map(
                CoroutineScope(Dispatchers.Default),
                delegate.state.value.toMediaStreamTrackState(),
                KotlinMediaStreamTrackState::toMediaStreamTrackState,
            )

    override val constraints: MediaTrackConstraints
        get() = delegate.constraints.toMediaTrackConstraints()

    override val settings: MediaTrackSettings
        get() = delegate.settings.toMediaTrackSettings()

    override fun stop() = delegate.stop()
}

internal fun MediaStreamTrack.toKotlinMediaStreamTrack(): KotlinMediaStreamTrack =
    KotlinMediaStreamTrackImpl(this)

private class KotlinMediaStreamTrackImpl(
    val delegate: MediaStreamTrack,
) : KotlinMediaStreamTrack {

    override val id: String
        get() = delegate.id

    override val kind: KotlinMediaStreamTrackKind
        get() = delegate.kind.toKotlinMediaStreamTrackKind()

    override val label: String
        get() = delegate.label

    override var enabled: Boolean
        get() = delegate.enabled
        set(value) {
            delegate.enabled = value
        }

    override val state: StateFlow<KotlinMediaStreamTrackState> =
        delegate.state
            .map(
                CoroutineScope(Dispatchers.Default),
                delegate.state.value.toKotlinMediaStreamTrackState(),
                MediaStreamTrackState::toKotlinMediaStreamTrackState,
            )

    override val constraints: KotlinMediaTrackConstraints
        get() = delegate.constraints.toKotlinMediaTrackConstraints()

    override val settings: KotlinMediaTrackSettings
        get() = delegate.settings.toKotlinMediaTrackSettings()

    override fun stop() = delegate.stop()
}

internal fun MediaStreamTrackKind.toKotlinMediaStreamTrackKind(): KotlinMediaStreamTrackKind =
    when (this) {
        MediaStreamTrackKind.Audio -> KotlinMediaStreamTrackKind.Audio
        MediaStreamTrackKind.Video -> KotlinMediaStreamTrackKind.Video
    }

internal fun KotlinMediaStreamTrackKind.toMediaStreamTrackKind(): MediaStreamTrackKind =
    when (this) {
        KotlinMediaStreamTrackKind.Audio -> MediaStreamTrackKind.Audio
        KotlinMediaStreamTrackKind.Video -> MediaStreamTrackKind.Video
    }

internal fun MediaStreamTrackState.toKotlinMediaStreamTrackState(): KotlinMediaStreamTrackState =
    when (this) {
        is MediaStreamTrackState.Live -> KotlinMediaStreamTrackState.Live(muted)
        is MediaStreamTrackState.Ended -> KotlinMediaStreamTrackState.Ended(muted)
    }

internal fun KotlinMediaStreamTrackState.toMediaStreamTrackState(): MediaStreamTrackState =
    when (this) {
        is KotlinMediaStreamTrackState.Live -> MediaStreamTrackState.Live(muted)
        is KotlinMediaStreamTrackState.Ended -> MediaStreamTrackState.Ended(muted)
    }
