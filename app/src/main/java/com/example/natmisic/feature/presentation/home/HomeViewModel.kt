package com.example.natmisic.feature.presentation.home

import android.content.Context
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaMuxer
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.natmisic.core.util.TAG
import com.example.natmisic.feature.domain.model.Book
import com.example.natmisic.feature.domain.use_case.UseCases
import com.example.natmisic.feature.presentation.util.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit
import javax.inject.Inject

sealed class HomeEvent {
    data class OpenSettings(val navController: NavController) : HomeEvent()
    data class Init(val context: Context) : HomeEvent()
}

data class HomeState(
    val books: List<Book> = emptyList(),
    val loading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: UseCases,
) : ViewModel() {

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state


    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OpenSettings -> {
                event.navController.navigate(Screens.SETTINGS.name)
            }
            is HomeEvent.Init -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCase.updateAndGetBooks(event.context).first().let {
                        withContext(Dispatchers.Main) {
                            delay(3000)
                            _state.value = state.value.copy(books = it, loading = false)
                        }
                    }
                }
            }
        }
    }

    /*fun slice(context: Context) {
        val p = "content://com.android.externalstorage.documents/tree/primary%3AAutodesk/document/primary%3AAutodesk%2FNever%20Split%20the%20Difference.m4a"
        val uri = Uri.parse(p)

        val path = "/storage/emulated/0/Autodesk/Never Split the Difference.m4a"
    }

    fun extract(startTimeMs: Long, endTimeMs: Long, path: String){
        val extractor = MediaExtractor()
        extractor.setDataSource(path)

        val trackIndex = extractor.sampleTrackIndex
        extractor.selectTrack(trackIndex)
        val format = extractor.getTrackFormat(trackIndex)

        val startTimeUs = TimeUnit.MILLISECONDS.toMicros(startTimeMs)
        val endTimeUs = TimeUnit.MILLISECONDS.toMicros(endTimeMs)

        val muxer = MediaMuxer("path/to/new/audio/file", MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
        val newTrackIndex = muxer.addTrack(format)
        muxer.start()

        val bufferInfo = MediaCodec.BufferInfo()
        val sampleData = ByteBuffer.allocate(1024 * 1024)

        while (true) {
            bufferInfo.offset = 0
            bufferInfo.size = extractor.readSampleData(sampleData, 0)
            if (bufferInfo.size < 0) {
                break
            }
            bufferInfo.presentationTimeUs = extractor.sampleTime
            if (bufferInfo.presentationTimeUs >= startTimeUs && bufferInfo.presentationTimeUs <= endTimeUs) {
                muxer.writeSampleData(newTrackIndex, sampleData, bufferInfo)
            }
            extractor.advance()
        }

        muxer.stop()
        muxer.release()
        extractor.release()
    }
    */


}

