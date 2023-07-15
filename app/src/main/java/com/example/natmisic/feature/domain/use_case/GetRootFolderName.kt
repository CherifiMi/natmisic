package com.example.natmisic.feature.domain.use_case

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class GetRootFolderName(
    private val dataStore: DataStore<Preferences>
) {
    operator fun invoke(
        pref: Preferences.Key<String>
    ): String? =
        runBlocking {
            dataStore.data.map {
                it[pref]
            }.first()
        }
}