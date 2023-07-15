package com.example.natmisic.feature.domain.use_case

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.natmisic.core.util.DataStoreKeys
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class GetDataStoreItem(
    private val dataStore: DataStore<Preferences>
) {
    operator fun invoke(
        key: Preferences.Key<String>
    ):String? =
        runBlocking {
            dataStore.data.map {
                it[key]
            }.first()
        }
}