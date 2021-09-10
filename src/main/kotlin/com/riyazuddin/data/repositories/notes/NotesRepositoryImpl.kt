package com.riyazuddin.data.repositories.notes

import com.riyazuddin.data.model.Note
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class NotesRepositoryImpl(
    db: CoroutineDatabase
) : INotesRepository {

    private val notesCollection = db.getCollection<Note>()

    override suspend fun getNoteOwner(noteId: String): String {
        return notesCollection.findOneById(noteId)?.owner ?: ""
    }

    override suspend fun addNote(note: Note): Boolean {
        return notesCollection.insertOne(note).wasAcknowledged()
    }

    override suspend fun updateNote(note: Note): Boolean {
        return notesCollection.updateOneById(note.id, note).wasAcknowledged()
    }

    override suspend fun getNotes(userId: String): List<Note> {
        return notesCollection
            .find(Note::owner eq userId).toList()
    }

    override suspend fun deleteNote(noteId: String): Boolean {
        return notesCollection.deleteOneById(noteId).wasAcknowledged()
    }

    override suspend fun noteExistsWithId(noteId: String): Boolean {
        return notesCollection.findOneById(noteId) != null
    }
}