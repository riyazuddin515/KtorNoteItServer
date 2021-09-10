package com.riyazuddin.plugins

import com.riyazuddin.routes.*
import com.riyazuddin.services.NotesService
import com.riyazuddin.services.UserService
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val userService: UserService by inject()
    val noteService: NotesService by inject()

    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtIssuer = environment.config.property("jwt.domain").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()

    routing {
        signUp(userService)
        login(userService, jwtAudience, jwtIssuer, jwtSecret)
        addNote(userService, noteService)
        getNotes(userService, noteService)
        deleteNote(userService, noteService)
    }
}
