package com.example.natmisic.feature.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.natmisic.core.util.DataStoreKeys
import com.example.natmisic.feature.domain.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: UseCases,
) : ViewModel() {
    //val books: ImmutableMap<BookOverviewCategory, List<BookOverviewItemViewState>>,
    fun getRootFolderName(): String {
        return useCase.getDataStoreItem(DataStoreKeys.ROOT_FOLDER_KEY)?:""
    }


}

/*
data class BookOverviewItemViewState(
    val name: String,
    val author: String?,
    val cover: ImmutableFile?,
    val progress: Float,
    val id: BookId,
    val remainingTime: String,
)
fun Book.progress(): Float {
    val globalPosition = position
    val totalDuration = duration
    val progress = globalPosition.toFloat() / totalDuration.toFloat()
    if (progress < 0F) {
        Logger.w("Couldn't determine progress for book=$this")
    }
    return progress.coerceIn(0F, 1F)
}
*/
