package com.diargegaj.recipesharing.domain.repository.userProfile

import com.diargegaj.recipesharing.domain.models.UserModel
import com.diargegaj.recipesharing.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {

    suspend fun addUserAdditionalInformation(userModel: UserModel): Resource<Unit>

    suspend fun getUserInfoFromFirestore(userId: String): Resource<UserModel>

    fun getUserInfoFromCache(userId: String): Flow<Resource<UserModel>>

    suspend fun saveUserInfoOnCache(userModel: UserModel): Resource<Unit>

    suspend fun updateUserProfilePhotoUrl(userId: String, imageUrl: String): Resource<Unit>

    suspend fun updateUserName(name: String, lastName: String): Resource<Unit>

    suspend fun updateUserInfoFromFirestore(userId: String): Resource<Unit>

}