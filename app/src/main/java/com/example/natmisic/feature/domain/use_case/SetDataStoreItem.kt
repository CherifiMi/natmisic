package com.example.natmisic.feature.domain.use_case

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.runBlocking

class SetDataStoreItem(
    private val dataStore: DataStore<Preferences>
) {
    operator fun invoke(
        key: Preferences.Key<String>, data: String
    ) {
        runBlocking {
            dataStore.edit {
                it[key] = data
            }
        }
    }
}