package com.riyazuddin.services

import com.riyazuddin.data.model.Note
import com.riyazuddin.data.repositories.notes.INotesRepository
import org.litote.kmongo.not

class NotesService(
    private val notesRepository: INotesRepository
) {

    suspend fun addNote(note: Note): Boolean {
        return notesRepository.addNote(note)
    }

    suspend fun getNoteOwner(noteId: String): String{
        return notesRepository.getNoteOwner(noteId)
    }

    suspend fun getNotes(userId: String): List<Note>{
        return notesRepository.getNotes(userId)
    }

    suspend fun deleteNote(noteId: String): Boolean{
        return notesRepository.deleteNote(noteId)
    }

    suspend fun noteExistsWithId(noteId: String): Boolean{
        return notesRepository.noteExistsWithId(noteId)
    }
}