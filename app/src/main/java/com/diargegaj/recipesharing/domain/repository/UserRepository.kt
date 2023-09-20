package com.diargegaj.recipesharing.domain.repository

import com.diargegaj.recipesharing.domain.models.UserModel
import com.diargegaj.recipesharing.domain.utils.Resource
import com.google.firebase.auth.FirebaseUser

interface UserRepository {

    suspend fun registerUser(email: String, password: String): Resource<FirebaseUser>

    suspend fun addUserAdditionalInformation(userModel: UserModel): Resource<Unit>

}