package com.riyazuddin.data.repositories.user

import com.riyazuddin.data.model.User

interface IUserRepository {

    suspend fun emailAlreadyExists(email: String): Boolean

    suspend fun signUp(user: User): Boolean

    suspend fun getUserWithEmail(email: String): User?

    suspend fun userExistWithId(id: String): Boolean

    suspend fun login(email: String, password: String): Boolean
}