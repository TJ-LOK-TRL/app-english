package com.masterproject.englishapp.audio

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

suspend fun playPcmAudio(
    audioData: ByteArray,
    sampleRate: Int
) = withContext(Dispatchers.IO) {

    val audioTrack = AudioTrack(
        AudioManager.STREAM_MUSIC,
        sampleRate,
        AudioFormat.CHANNEL_OUT_MONO,
        AudioFormat.ENCODING_PCM_16BIT,
        audioData.size,
        AudioTrack.MODE_STATIC
    )

    audioTrack.write(audioData, 0, audioData.size)

    // total frames
    val totalFrames = audioData.size / 2 // PCM 16bit = 2 bytes

    audioTrack.notificationMarkerPosition = totalFrames

    suspendCancellableCoroutine<Unit> { cont ->
        audioTrack.setPlaybackPositionUpdateListener(
            object : AudioTrack.OnPlaybackPositionUpdateListener {
                override fun onMarkerReached(track: AudioTrack) {
                    cont.resume(Unit) { cause, _, _ -> }
                }
                override fun onPeriodicNotification(track: AudioTrack) {}
            }
        )

        audioTrack.play()

        cont.invokeOnCancellation {
            audioTrack.stop()
            audioTrack.release()
        }
    }

    audioTrack.stop()
    audioTrack.release()
}

