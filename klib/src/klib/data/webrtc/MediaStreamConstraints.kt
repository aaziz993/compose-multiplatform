package klib.data.webrtc

public data class MediaStreamConstraints(
    val audio: MediaTrackConstraints? = null,
    val video: MediaTrackConstraints? = null,
)

public class MediaStreamConstraintsBuilder {

    public var constraints: MediaStreamConstraints = MediaStreamConstraints()
        private set

    public fun audio(enabled: Boolean = true) {
        if (enabled) {
            audio { }
        }
    }

    public fun audio(build: MediaTrackConstraintsBuilder.() -> Unit) {
        val trackConstraintsBuilder =
            MediaTrackConstraintsBuilder(
                constraints.audio ?: MediaTrackConstraints(),
            )
        build(trackConstraintsBuilder)
        constraints = constraints.copy(audio = trackConstraintsBuilder.constraints)
    }

    public fun video(enabled: Boolean = true) {
        if (enabled) {
            video { }
        }
    }

    public fun video(build: MediaTrackConstraintsBuilder.() -> Unit) {
        val trackConstraintsBuilder =
            MediaTrackConstraintsBuilder(
                constraints.video ?: MediaTrackConstraints(),
            )
        build(trackConstraintsBuilder)
        constraints = constraints.copy(video = trackConstraintsBuilder.constraints)
    }
}
