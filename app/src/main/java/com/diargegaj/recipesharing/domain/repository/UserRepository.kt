package com.diargegaj.recipesharing.domain.repository

import com.diargegaj.recipesharing.domain.models.UserModel
import com.diargegaj.recipesharing.domain.utils.Resource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface UserRepository {


    suspend fun addUserAdditionalInformation(userModel: UserModel): Resource<Unit>



    suspend fun getUserInfoFromFirestore(userId: String): Resource<UserModel>

    fun getUserInfoFromCache(userId: String): Flow<Resource<UserModel>>

    suspend fun saveUserInfoOnCache(userModel: UserModel): Resource<Unit>


    suspend fun updateUserProfilePhotoUrl(userId: String, imageUrl: String): Resource<Unit>





    suspend fun updateUserName(name: String, lastName: String): Resource<Unit>

    fun isUserFollowing(loggedInUserId: String, otherUserId: String): Flow<Resource<Boolean>>

    suspend fun followUser(loggedInUserId: String, otherUserId: String): Resource<Unit>

    suspend fun unfollowUser(loggedInUserId: String, otherUserId: String): Resource<Unit>

    suspend fun updateUserInfoFromFirestore(userId: String): Resource<Unit>

    suspend fun getFollowersForUser(
        userId: String,
        currentUserId: String
    ): Resource<List<UserModel>>

    suspend fun getFollowingForUser(
        userId: String,
        currentUserId: String
    ): Resource<List<UserModel>>
}