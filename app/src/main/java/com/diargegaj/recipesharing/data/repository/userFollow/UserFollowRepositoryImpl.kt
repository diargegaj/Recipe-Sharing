package com.diargegaj.recipesharing.data.repository.userFollow

import DBCollection
import com.diargegaj.recipesharing.data.utils.safeCall
import com.diargegaj.recipesharing.domain.repository.userFollow.UserFollowRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserFollowRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : UserFollowRepository {

    override fun isUserFollowing(
        loggedInUserId: String,
        otherUserId: String
    ): Flow<Resource<Boolean>> = flow<Resource<Boolean>> {
        val docSnapshot = fireStore.collection("following")
            .document(loggedInUserId)
            .collection("userFollowing")
            .document(otherUserId)
            .get()
            .await()

        emit(Resource.Success(docSnapshot.exists()))
    }.catch { throwable ->
        emit(
            Resource.Error(Exception(throwable))
        )
    }

    override suspend fun followUser(loggedInUserId: String, otherUserId: String): Resource<Unit> {
        return safeCall {
            val batch = fireStore.batch()

            val loggedInUserRef =
                fireStore.collection(DBCollection.User.collectionName).document(loggedInUserId)
            batch.update(loggedInUserRef, "followingCount", FieldValue.increment(1))

            val otherUserRef =
                fireStore.collection(DBCollection.User.collectionName).document(otherUserId)
            batch.update(otherUserRef, "followersCount", FieldValue.increment(1))

            val followingRef =
                fireStore.collection(DBCollection.Following.collectionName).document(loggedInUserId)
                    .collection(DBCollection.UserFollowing.collectionName).document(otherUserId)
            batch.set(followingRef, mapOf("followingUserId" to otherUserId))

            val followersRef =
                fireStore.collection(DBCollection.Followers.collectionName).document(otherUserId)
                    .collection(DBCollection.UserFollowers.collectionName)
                    .document(loggedInUserId)
            batch.set(followersRef, mapOf("followerUserId" to loggedInUserId))

            batch.commit().await()

            Resource.Success(Unit)
        }
    }

    override suspend fun unfollowUser(loggedInUserId: String, otherUserId: String): Resource<Unit> {
        return safeCall {
            val batch = fireStore.batch()

            val loggedInUserRef =
                fireStore.collection(DBCollection.User.collectionName).document(loggedInUserId)
            batch.update(loggedInUserRef, "followingCount", FieldValue.increment(-1))

            val otherUserRef =
                fireStore.collection(DBCollection.User.collectionName).document(otherUserId)
            batch.update(otherUserRef, "followersCount", FieldValue.increment(-1))

            val followingRef =
                fireStore.collection(DBCollection.Following.collectionName).document(loggedInUserId)
                    .collection(DBCollection.UserFollowing.collectionName).document(otherUserId)
            batch.delete(followingRef)

            val followersRef =
                fireStore.collection(DBCollection.Followers.collectionName).document(otherUserId)
                    .collection(DBCollection.UserFollowers.collectionName)
                    .document(loggedInUserId)
            batch.delete(followersRef)

            batch.commit().await()

            Resource.Success(Unit)
        }
    }
}