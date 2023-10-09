package com.diargegaj.recipesharing.data.repository

import DBCollection
import com.diargegaj.recipesharing.data.mappers.mapToDomain
import com.diargegaj.recipesharing.data.models.UserDto
import com.diargegaj.recipesharing.data.utils.safeCall
import com.diargegaj.recipesharing.domain.models.UserModel
import com.diargegaj.recipesharing.domain.repository.UserRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : UserRepository {

    override suspend fun getFollowersForUser(
        userId: String,
        currentUserId: String
    ): Resource<List<UserModel>> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val followersRef = fireStore.collection(DBCollection.Followers.collectionName)
                    .document(userId)
                    .collection(DBCollection.UserFollowers.collectionName)

                val followersSnapshots = followersRef.get().await()

                val followerIds =
                    followersSnapshots.documents.map { it.getString("followerUserId") ?: "" }

                val followers = mutableListOf<UserModel>()
                for (followerId in followerIds) {
                    val userSnapshot =
                        fireStore.collection(DBCollection.User.collectionName).document(followerId)
                            .get().await()
                    val userDto = userSnapshot.toObject(UserDto::class.java)
                    userDto?.userUUID = followerId
                    userDto?.let {
                        val userModel = it.mapToDomain()
//                        val isFollowing = when (
//                            val result = isUserFollowing(
//                                currentUserId,
//                                followerId
//                            ).first()
//                        ) {
//                            is Resource.Success -> result.data
//                            else -> false
//                        }
//                        userModel.isFollowedByCurrentUser = isFollowing
                        followers.add(userModel)
                    }
                }
                Resource.Success(followers)
            }
        }
    }

    override suspend fun getFollowingForUser(
        userId: String,
        currentUserId: String
    ): Resource<List<UserModel>> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val followingRef = fireStore.collection(DBCollection.Following.collectionName)
                    .document(userId)
                    .collection(DBCollection.UserFollowing.collectionName)

                val followingSnapshots = followingRef.get().await()

                val followingIds =
                    followingSnapshots.documents.map { it.getString("followingUserId") ?: "" }

                val followingUsers = mutableListOf<UserModel>()
                for (followingId in followingIds) {
                    val userSnapshot =
                        fireStore.collection(DBCollection.User.collectionName).document(followingId)
                            .get().await()
                    val userDto = userSnapshot.toObject(UserDto::class.java)
                    userDto?.userUUID = followingId
                    userDto?.let {
                        val userModel = it.mapToDomain()
//                        val isFollowing = when (
//                            val result = isUserFollowing(
//                                currentUserId,
//                                followingId
//                            ).first()
//                        ) {
//                            is Resource.Success -> result.data
//                            else -> false
//                        }
//                        userModel.isFollowedByCurrentUser = isFollowing
//                        followingUsers.add(userModel)
                    }
                }
                Resource.Success(followingUsers)
            }
        }
    }


    private suspend fun updateDisplayName(user: FirebaseUser, name: String, lastName: String) =
        withContext(Dispatchers.IO) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName("$name $lastName")
                .build()

            user.updateProfile(profileUpdates).await()
        }

    private suspend fun updateNameToFirestore(user: FirebaseUser, name: String, lastName: String) =
        withContext(Dispatchers.IO) {
            val userDocument =
                fireStore.collection(DBCollection.User.collectionName).document(user.uid)

            userDocument.update("name", name).await()
            userDocument.update("lastName", lastName).await()
        }
}