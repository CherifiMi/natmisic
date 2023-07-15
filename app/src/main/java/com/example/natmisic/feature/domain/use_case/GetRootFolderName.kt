package com.example.natmisic.feature.domain.use_case

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.natmisic.core.util.DataStoreKeys
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class GetRootFolderName(
    private val dataStore: DataStore<Preferences>
) {
    operator fun invoke(
    ): String =
        runBlocking {
            dataStore.data.map {
                it[DataStoreKeys.ROOT_FOLDER_KEY] ?: ""
            }.first()
        }
}