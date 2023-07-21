package com.example.natmisic

import androidx.lifecycle.ViewModel
import com.example.natmisic.core.util.DataStoreKeys
import com.example.natmisic.feature.domain.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: UseCases,
) : ViewModel() {
    fun hasRootFolder(): Boolean {
        return useCase.getDataStoreItem(DataStoreKeys.ROOT_FOLDER_KEY) != null
    }
}