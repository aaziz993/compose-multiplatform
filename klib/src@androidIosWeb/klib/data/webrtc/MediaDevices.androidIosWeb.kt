@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.webrtc

import com.shepeliev.webrtckmp.MediaDevices as KotlinMediaDevices
import com.shepeliev.webrtckmp.MediaDeviceInfo as KotlinMediaDeviceInfo

public actual val mediaDevices: MediaDevices = object : MediaDevices {
    override suspend fun getUserMedia(
        streamConstraints: MediaStreamConstraintsBuilder.() -> Unit,
    ): MediaStream {
        val constraints = MediaStreamConstraintsBuilder().apply(streamConstraints).constraints

        return KotlinMediaDevices.getUserMedia {
            this.constraints = constraints.toKotlinMediaStreamConstraints()
        }.toMediaStream()
    }

    override suspend fun getDisplayMedia(): MediaStream = KotlinMediaDevices.getDisplayMedia().toMediaStream()

    override suspend fun supportsDisplayMedia(): Boolean = KotlinMediaDevices.supportsDisplayMedia()

    override suspend fun enumerateDevices(): List<MediaDeviceInfo> =
        KotlinMediaDevices.enumerateDevices().map(KotlinMediaDeviceInfo::toMediaDeviceInfo)
}

