// AndroidAudioRecorder.kt
package com.masterproject.englishapp.recorder

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Process
import androidx.core.content.ContextCompat
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentLinkedQueue

class AndroidAudioRecorder(
    private val context: Context
) : AudioRecorder {

    // Configurações do áudio para o whisper.cpp
    private val SAMPLE_RATE = 16000 // 16kHz
    private val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
    private val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT

    private var audioRecord: AudioRecord? = null
    private var recordingJob: Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val audioBufferQueue = ConcurrentLinkedQueue<ShortArray>()

    override var isRecording: Boolean = false
        private set

    override fun startRecording(onAudioData: (FloatArray) -> Unit) {
        if (isRecording) return

        // Verifica permissão
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            println("Erro: Permissão RECORD_AUDIO não concedida.")
            return
        }

        // 1. Calcula o tamanho mínimo do buffer
        val bufferSize = AudioRecord.getMinBufferSize(
            SAMPLE_RATE,
            CHANNEL_CONFIG,
            AUDIO_FORMAT
        )
        val audioBuffer = ShortArray(bufferSize)

        // 2. Inicializa AudioRecord
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            SAMPLE_RATE,
            CHANNEL_CONFIG,
            AUDIO_FORMAT,
            bufferSize
        )

        audioBufferQueue.clear()

        audioRecord?.startRecording()
        isRecording = true

        // 3. Inicia a gravação numa coroutine em background
        recordingJob = coroutineScope.launch {
            // Define a prioridade da thread para áudio em tempo real
            Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO)

            while (isActive && isRecording) {
                val read = audioRecord?.read(audioBuffer, 0, audioBuffer.size) ?: 0
                if (read > 0) {
                    val readBuffer = audioBuffer.copyOf(read)

                    // Converte PCM 16-bit (Short) para Float normalizado
                    val floatArray = readBuffer.toNormalizedFloatArray()

                    withContext(Dispatchers.Main) {
                        onAudioData(floatArray) // Envia o buffer em tempo real (opcional)
                    }
                    audioBufferQueue.add(readBuffer) // Adiciona o buffer bruto para o resultado final
                }
            }
        }
    }

    override fun stopRecording(): FloatArray? {
        if (!isRecording) return null

        isRecording = false
        recordingJob?.cancel()

        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null

        // 1. Concatena todos os buffers brutos (ShortArray)
        val totalShorts = audioBufferQueue.sumOf { it.size }
        val finalShorts = ShortArray(totalShorts)
        var offset = 0
        while (audioBufferQueue.isNotEmpty()) {
            val buffer = audioBufferQueue.poll()
            System.arraycopy(buffer, 0, finalShorts, offset, buffer.size)
            offset += buffer.size
        }

        // 2. Converte o áudio bruto final para FloatArray
        return finalShorts.toNormalizedFloatArray()
    }

    /**
     * Extensão para converter ShortArray (PCM 16-bit) para FloatArray normalizado (-1.0 a 1.0).
     * Este é o formato tipicamente exigido pelo whisper.cpp.
     */
    private fun ShortArray.toNormalizedFloatArray(): FloatArray {
        return FloatArray(size) { i ->
            // Normaliza dividindo pelo valor máximo de um Short (2^15 - 1)
            this[i].toFloat() / Short.MAX_VALUE
        }
    }
}