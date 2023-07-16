package com.example.natmisic.core.util

import androidx.datastore.preferences.core.stringPreferencesKey

const val TAG = "FILETEST"
class DataStoreKeys {
    companion object{
        val ROOT_FOLDER_KEY = stringPreferencesKey("ROOT_FOLDER_KEY")
    }
}