package com.diargegaj.recipesharing.domain.repository.userFollow

import com.diargegaj.recipesharing.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface UserFollowRepository {

    fun isUserFollowing(loggedInUserId: String, otherUserId: String): Flow<Resource<Boolean>>

    suspend fun followUser(loggedInUserId: String, otherUserId: String): Resource<Unit>

    suspend fun unfollowUser(loggedInUserId: String, otherUserId: String): Resource<Unit>

}