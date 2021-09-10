package com.riyazuddin.data.repositories.user

import com.riyazuddin.data.model.User
import com.riyazuddin.secure.checkHashForPassword
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class UserRepositoryImpl(
    db: CoroutineDatabase
): IUserRepository {

    private val usersCollection = db.getCollection<User>()

    override suspend fun emailAlreadyExists(email: String): Boolean {
        return usersCollection.findOne(User::email eq email) != null
    }

    override suspend fun signUp(user: User): Boolean {
        return usersCollection.insertOne(user).wasAcknowledged()
    }

    override suspend fun getUserWithEmail(email: String): User? {
        return usersCollection.findOne(User::email eq email)
    }

    override suspend fun userExistWithId(id: String): Boolean {
        return usersCollection.findOneById(id) != null
    }

    override suspend fun login(email: String, password: String): Boolean {
        val actualPassword = getUserWithEmail(email)?.password ?: return false
        return checkHashForPassword(password, actualPassword)
    }
}