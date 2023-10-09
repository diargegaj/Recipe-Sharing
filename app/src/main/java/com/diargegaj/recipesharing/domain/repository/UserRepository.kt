package com.diargegaj.recipesharing.domain.repository

import com.diargegaj.recipesharing.domain.models.UserModel
import com.diargegaj.recipesharing.domain.utils.Resource

interface UserRepository {

    suspend fun getFollowersForUser(
        userId: String,
        currentUserId: String
    ): Resource<List<UserModel>>

    suspend fun getFollowingForUser(
        userId: String,
        currentUserId: String
    ): Resource<List<UserModel>>

}