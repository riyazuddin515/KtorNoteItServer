package com.riyazuddin.di

import com.riyazuddin.data.repositories.notes.INotesRepository
import com.riyazuddin.data.repositories.notes.NotesRepositoryImpl
import com.riyazuddin.data.repositories.user.IUserRepository
import com.riyazuddin.data.repositories.user.UserRepositoryImpl
import com.riyazuddin.services.NotesService
import com.riyazuddin.services.UserService
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        val client = KMongo.createClient().coroutine
        client.getDatabase("NotesDB")
    }

    single<IUserRepository> {
        UserRepositoryImpl(get())
    }
    single { UserService(get()) }

    single<INotesRepository> {
        NotesRepositoryImpl(get())
    }
    single { NotesService(get()) }
}