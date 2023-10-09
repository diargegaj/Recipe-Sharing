package com.diargegaj.recipesharing.domain.repository.userAuth

import com.diargegaj.recipesharing.domain.utils.Resource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface UserAuthRepository {

    suspend fun registerUser(email: String, password: String): Resource<FirebaseUser>

    suspend fun logIn(email: String, password: String): Resource<FirebaseUser>

    fun isUserLoggedIn(): Flow<Boolean>

    suspend fun reAuthenticateUser(email: String, password: String): Resource<Unit>

    fun getUserId(): Resource<String>

    fun getCurrentUser(): FirebaseUser?

    suspend fun changeUserPassword(newPassword: String): Resource<Unit>

    suspend fun changeUserEmail(email: String): Resource<Unit>

}