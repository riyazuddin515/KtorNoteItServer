package com.riyazuddin.routes

import com.riyazuddin.data.model.Note
import com.riyazuddin.data.requests.DeleteNoteRequest
import com.riyazuddin.data.response.SimpleResponse
import com.riyazuddin.services.NotesService
import com.riyazuddin.services.UserService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.addNote(
    userService: UserService,
    notesService: NotesService
) {
    authenticate {
        post("/api/note/addNote") {
            val request = call.receiveOrNull<Note>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val userId = call.userId
            if (userId != request.owner) {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    SimpleResponse(
                        false,
                        "You aren't who you say"
                    )
                )
                return@post
            }
            val userExists = userService.userExistWithId(userId)
            if (!userExists) {
                call.respond(
                    HttpStatusCode.NotFound,
                    SimpleResponse(
                        false,
                        "User who attempting to add not found."
                    )
                )
                return@post
            }

            val result = notesService.addNote(request)
            if (result) {
                call.respond(
                    HttpStatusCode.OK,
                    SimpleResponse(
                        true,
                        "Note added"
                    )
                )
                return@post
            } else {
                call.respond(
                    HttpStatusCode.Conflict,
                    SimpleResponse(
                        false,
                        "Failed to Added. Try Later"
                    )
                )
                return@post
            }
        }
    }
}

fun Route.getNotes(
    userService: UserService,
    notesService: NotesService
){
    authenticate {
        get("/api/note/getNotes") {
            val userId = call.userId
            val list = notesService.getNotes(userId)
            call.respond(
                HttpStatusCode.OK,
                list
            )
        }
    }
}

fun Route.deleteNote(
    userService: UserService,
    notesService: NotesService
){
    authenticate {
        post("/api/note/deleteNote") {
            val request = call.receiveOrNull<DeleteNoteRequest>() ?: kotlin.run {
                call.respond(
                    HttpStatusCode.BadRequest
                )
                return@post
            }
            val userId = call.userId
            val userExists = userService.userExistWithId(userId)
            if (!userExists) {
                call.respond(
                    HttpStatusCode.NotFound,
                    SimpleResponse(
                        false,
                        "User who attempting to delete not found."
                    )
                )
                return@post
            }

            val noteExist = notesService.noteExistsWithId(request.id)
            if (!noteExist) {
                call.respond(
                    HttpStatusCode.NotFound,
                    SimpleResponse(
                        false,
                        "Note not found."
                    )
                )
                return@post
            }

            val noteOwner = notesService.getNoteOwner(request.id)
            if (noteOwner != userId) {
                call.respond(
                    HttpStatusCode.OK,
                    SimpleResponse(
                        false,
                        "You are not the note owner."
                    )
                )
                return@post
            }

            val result = notesService.deleteNote(request.id)
            if (result) {
                call.respond(
                    HttpStatusCode.OK,
                    SimpleResponse(
                        true,
                        "Note Deleted."
                    )
                )
                return@post
            }else{
                call.respond(
                    HttpStatusCode.Conflict,
                    SimpleResponse(
                        false,
                        "Note not Deleted. Try later."
                    )
                )
                return@post
            }
        }
    }
}