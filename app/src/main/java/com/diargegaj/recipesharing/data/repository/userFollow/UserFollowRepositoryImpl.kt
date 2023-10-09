package com.diargegaj.recipesharing.data.repository.userFollow

import com.diargegaj.recipesharing.data.datasource.firestore.FirestoreUserFollowDataSource
import com.diargegaj.recipesharing.data.utils.safeCall
import com.diargegaj.recipesharing.domain.repository.userFollow.UserFollowRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserFollowRepositoryImpl @Inject constructor(
    private val firestoreDataSource: FirestoreUserFollowDataSource
) : UserFollowRepository {

    override fun isUserFollowing(
        loggedInUserId: String,
        otherUserId: String
    ): Flow<Resource<Boolean>> = flow<Resource<Boolean>> {
        val docSnapshot = firestoreDataSource.isUserFollowing(
            loggedInUserId = loggedInUserId,
            otherUserId = otherUserId
        )

        val isFollowing = docSnapshot?.exists() == true
        emit(Resource.Success(isFollowing))
    }.catch { throwable ->
        emit(
            Resource.Error(Exception(throwable))
        )
    }

    override suspend fun followUser(loggedInUserId: String, otherUserId: String): Resource<Unit> {
        return safeCall {
            firestoreDataSource.followUser(
                loggedInUserId = loggedInUserId,
                otherUserId = otherUserId
            )

            Resource.Success(Unit)
        }
    }

    override suspend fun unfollowUser(loggedInUserId: String, otherUserId: String): Resource<Unit> {
        return safeCall {
            firestoreDataSource.unfollowUser(
                loggedInUserId = loggedInUserId,
                otherUserId = otherUserId
            )

            Resource.Success(Unit)
        }
    }
}