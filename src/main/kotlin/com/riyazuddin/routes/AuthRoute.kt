package com.riyazuddin.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.riyazuddin.data.model.User
import com.riyazuddin.data.requests.AccountRequest
import com.riyazuddin.data.response.AuthResponse
import com.riyazuddin.data.response.SimpleResponse
import com.riyazuddin.secure.passwordToHash
import com.riyazuddin.services.UserService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.util.*

fun Route.signUp(userService: UserService) {
    post("api/user/signup") {
        val request = call.receiveOrNull<AccountRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val emailExist = userService.emailAlreadyExists(request.email)
        if (emailExist) {
            call.respond(
                HttpStatusCode.OK,
                SimpleResponse(
                    false,
                    "Email already exists"
                )
            )
            return@post
        }
        when (userService.validateSignUp(request.email, request.password)) {
            is UserService.ValidateEvent.EmptyFieldsError -> {
                call.respond(
                    HttpStatusCode.OK,
                    SimpleResponse(
                        false,
                        "Email and Password are required"
                    )
                )
                return@post
            }
            UserService.ValidateEvent.InvalidEmailError -> {
                call.respond(
                    HttpStatusCode.OK,
                    SimpleResponse(
                        false,
                        "Invalid Email address"
                    )
                )
                return@post
            }
            UserService.ValidateEvent.ShortPasswordError -> {
                call.respond(
                    HttpStatusCode.OK,
                    SimpleResponse(
                        false,
                        "Password must be 8 digits long"
                    )
                )
                return@post
            }
            UserService.ValidateEvent.SuccessCreateAccount -> {
                val result = userService.signUp(
                    User(
                        request.email,
                        passwordToHash(request.password)
                    )
                )
                if (result) {
                    call.respond(
                        HttpStatusCode.OK,
                        SimpleResponse(
                            true,
                            "Account created successfully"
                        )
                    )
                    return@post
                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        SimpleResponse(
                            false,
                            "Failed to create account. Try later"
                        )
                    )
                    return@post
                }
            }
        }
    }
}

fun Route.login(
    userService: UserService,
    jwtAudience: String,
    jwtIssuer: String,
    jwtSecret: String
) {
    post("/api/user/login") {
        val request = call.receiveOrNull<AccountRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val user = userService.getUserWithEmail(request.email) ?: kotlin.run {
            call.respond(
                SimpleResponse(
                    false,
                    "User with this email doesn't exists"
                )
            )
            return@post
        }

        val result = userService.login(request.email, request.password)
        if (result) {
            val expiresIn = 1000L * 60L * 60L * 24L * 365L
            val token = JWT.create()
                .withClaim("userId", user.id)
                .withIssuer(jwtIssuer)
                .withExpiresAt(Date(System.currentTimeMillis() + expiresIn))
                .withAudience(jwtAudience)
                .sign(Algorithm.HMAC256(jwtSecret))
            call.respond(
                AuthResponse(
                    email = user.email,
                    token = token
                )
            )
            return@post
        } else {
            call.respond(
                SimpleResponse(
                    false,
                    "Invalid Credentials"
                )
            )
            return@post
        }
    }
}