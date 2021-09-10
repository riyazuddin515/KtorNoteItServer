package com.riyazuddin.services

import com.riyazuddin.data.model.User
import com.riyazuddin.data.repositories.user.IUserRepository
import java.util.regex.Pattern

class UserService(
    private val userRepository: IUserRepository
) {

    suspend fun emailAlreadyExists(email: String): Boolean {
        return userRepository.emailAlreadyExists(email)
    }

    suspend fun signUp(user: User): Boolean {
        return userRepository.signUp(user)
    }

    fun validateSignUp(email: String, password: String): ValidateEvent {
        if (email.isBlank() || password.isBlank()) {
            return ValidateEvent.EmptyFieldsError
        }
        val regex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$"
        val pattern = Pattern.compile(regex)
        if (!pattern.matcher(email).matches()) {
            return ValidateEvent.InvalidEmailError
        }
        if (password.length < 8) {
            return ValidateEvent.ShortPasswordError
        }
        return ValidateEvent.SuccessCreateAccount
    }

    suspend fun getUserWithEmail(email: String): User? {
        return userRepository.getUserWithEmail(email)
    }

    suspend fun userExistWithId(id: String): Boolean {
        return userRepository.userExistWithId(id)
    }

    suspend fun login(email: String, password: String): Boolean {
        return userRepository.login(email, password)
    }

    sealed class ValidateEvent {
        object EmptyFieldsError : ValidateEvent()
        object InvalidEmailError : ValidateEvent()
        object ShortPasswordError : ValidateEvent()
        object SuccessCreateAccount : ValidateEvent()
    }
}