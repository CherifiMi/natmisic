package com.example.natmisic.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.exoplayer.MusicServiceConnection
import com.example.natmisic.R
import com.example.natmisic.feature.data.RepositoryImp
import com.example.natmisic.feature.data.local.BookDatabase
import com.example.natmisic.feature.domain.reposetory.Repository
import com.example.natmisic.feature.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = {
                appContext.preferencesDataStoreFile("settings")
            }
        )

    @Provides
    @Singleton
    fun provideBookDatabase(app: Application): BookDatabase {
        return Room.databaseBuilder(
            app,
            BookDatabase::class.java,
            BookDatabase.Database_Name
        ).build()
    }

    @Provides
    @Singleton
    fun provideUseCases(
        dataStore: DataStore<Preferences>,
        repository: Repository,
        @ApplicationContext context: Context
    ) = UseCases(
        getDataStoreItem = GetDataStoreItem(dataStore),
        setDataStoreItem = SetDataStoreItem(dataStore),
        updateAndGetBooks = UpdateAndGetBooks(repository, dataStore, context),
        getAllBooks = GetAllBooks(repository),
        getBookById = GetBookById(repository),
        updateBookById = UpdateBookById(repository)
    )

    @Provides
    @Singleton
    fun provideRepository(dp: BookDatabase): Repository {
        return RepositoryImp(dp.bookDao)
    }

    @Singleton
    @Provides
    fun provideMusicServiceConnection(
        @ApplicationContext context: Context
    ) = MusicServiceConnection(
        context
    )
    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
    )
}



