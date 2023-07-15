package com.example.natmisic.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /*@Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase{
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.Database_Name
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(dp: NoteDatabase): NoteRepository{
        return NoteRepositoryImp(dp.notDao)
    }*/


    /*@Provides
    @Singleton
    fun provideUseCases(repository: NoteRepository): NoteUseCase {
        return NoteUseCase(
            getNote = GetNote(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository),
            getNotes = GetNotes(repository)
        )
    }*/
}