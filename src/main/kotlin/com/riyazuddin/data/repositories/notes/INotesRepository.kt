package com.riyazuddin.data.repositories.notes

import com.riyazuddin.data.model.Note

interface INotesRepository {

    suspend fun addNote(note: Note): Boolean

    suspend fun getNoteOwner(noteId: String): String

    suspend fun getNotes(userId: String): List<Note>

    suspend fun deleteNote(noteId: String): Boolean

    suspend fun noteExistsWithId(noteId: String): Boolean
}