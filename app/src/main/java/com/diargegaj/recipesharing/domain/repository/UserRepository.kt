package com.diargegaj.recipesharing.domain.repository

import com.diargegaj.recipesharing.domain.models.UserModel
import com.diargegaj.recipesharing.domain.utils.Resource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun registerUser(email: String, password: String): Resource<FirebaseUser>

    suspend fun addUserAdditionalInformation(userModel: UserModel): Resource<Unit>

    fun isUserLoggedIn(): Flow<Boolean>

}