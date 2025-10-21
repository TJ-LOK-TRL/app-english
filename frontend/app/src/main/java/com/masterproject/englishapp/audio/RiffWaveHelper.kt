package com.masterproject.englishapp.audio

import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

fun encodeWaveToBytes(data: FloatArray, sampleRate: Int = 16000): ByteArray {
    // Convert FloatArray (-1..1) to PCM16 ShortArray
    val shortData = ShortArray(data.size) { i ->
        (data[i].coerceIn(-1f, 1f) * 32767f).toInt().toShort()
    }

    // Calculate total length of WAV file
    val totalDataLength = 44 + shortData.size * 2
    val header = createWaveHeader(totalDataLength, sampleRate)

    // Write header + PCM data to memory
    val out = ByteArrayOutputStream()
    out.write(header)

    val buffer = ByteBuffer.allocate(shortData.size * 2)
    buffer.order(ByteOrder.LITTLE_ENDIAN)
    buffer.asShortBuffer().put(shortData)
    out.write(buffer.array())

    return out.toByteArray()
}

private fun createWaveHeader(totalLength: Int, sampleRate: Int): ByteArray {
    ByteBuffer.allocate(44).apply {
        order(ByteOrder.LITTLE_ENDIAN)

        put("RIFF".toByteArray(Charsets.US_ASCII))
        putInt(totalLength - 8)
        put("WAVE".toByteArray(Charsets.US_ASCII))
        put("fmt ".toByteArray(Charsets.US_ASCII))
        putInt(16)              // Subchunk1Size
        putShort(1)             // AudioFormat = PCM
        putShort(1)             // NumChannels
        putInt(sampleRate)            // SampleRate
        putInt(sampleRate * 2)  // ByteRate = SampleRate * NumChannels * BitsPerSample/8
        putShort(2)             // BlockAlign
        putShort(16)            // BitsPerSample
        put("data".toByteArray(Charsets.US_ASCII))
        putInt(totalLength - 44)
        position(0)
    }.also {
        val bytes = ByteArray(it.limit())
        it.get(bytes)
        return bytes
    }
}
