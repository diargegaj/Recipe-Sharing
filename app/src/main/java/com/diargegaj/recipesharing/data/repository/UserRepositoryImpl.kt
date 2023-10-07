package com.diargegaj.recipesharing.data.repository

import DBCollection
import android.content.res.Resources.NotFoundException
import com.diargegaj.recipesharing.data.db.dao.UserDao
import com.diargegaj.recipesharing.data.mappers.mapToDomain
import com.diargegaj.recipesharing.data.mappers.mapToDto
import com.diargegaj.recipesharing.data.mappers.mapToEntity
import com.diargegaj.recipesharing.data.mappers.mapToUserModel
import com.diargegaj.recipesharing.data.models.UserDto
import com.diargegaj.recipesharing.data.utils.Constants.INSERTION_FAILED
import com.diargegaj.recipesharing.data.utils.safeCall
import com.diargegaj.recipesharing.domain.models.UserModel
import com.diargegaj.recipesharing.domain.repository.UserRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val userDao: UserDao
) : UserRepository {

    override suspend fun registerUser(email: String, password: String): Resource<FirebaseUser> =
        withContext(Dispatchers.IO) {
            safeCall {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                Resource.Success(result.user!!)
            }
        }

    override suspend fun addUserAdditionalInformation(userModel: UserModel): Resource<Unit> =
        withContext(Dispatchers.IO) {
            safeCall {
                val userDto = userModel.mapToDto()
                fireStore.collection(DBCollection.User.collectionName)
                    .document(userDto.userUUID)
                    .set(userDto)
                    .await()

                val userEntity = userDto.mapToEntity()
                userDao.insert(userEntity)

                Resource.Success(Unit)
            }
        }

    override suspend fun logIn(email: String, password: String): Resource<FirebaseUser> =
        withContext(Dispatchers.IO) {
            safeCall {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                Resource.Success(result.user!!)
            }
        }

    override fun isUserLoggedIn(): Flow<Boolean> {
        val currentUser = auth.currentUser
        return if (currentUser != null) {
            userDao.getUser(currentUser.uid).map { it != null }
        } else {
            flowOf(false)
        }
    }

    override suspend fun getUserInfoFromFirestore(userId: String): Resource<UserModel> =
        withContext(Dispatchers.IO) {
            safeCall {
                val document = fireStore.collection(DBCollection.User.collectionName)
                    .document(userId)
                    .get()
                    .await()

                val userDto = document.toObject(UserDto::class.java)
                userDto?.userUUID = userId

                if (userDto != null) {
                    Resource.Success(userDto.mapToDomain())
                } else {
                    Resource.Error(NotFoundException("User not found!"))
                }
            }
        }

    override fun getUserInfoFromCache(userId: String): Flow<Resource<UserModel>> {
        return userDao.getUserWithRecipes(userId)
            .map { userEntity ->
                if (userEntity != null) {
                    Resource.Success(userEntity.mapToUserModel())
                } else {
                    Resource.Error(NotFoundException("User not found"))
                }
            }
            .catch {
                emit(Resource.Error(Exception(it.message)))
            }
    }

    override suspend fun saveUserInfoOnCache(userModel: UserModel): Resource<Unit> =
        withContext(Dispatchers.IO) {
            val result = userDao.insert(userModel.mapToDto().mapToEntity())
            if (result != INSERTION_FAILED) {
                Resource.Success(Unit)
            } else {
                Resource.Error(Exception("Failed to insert user info into cache."))
            }
        }

    override fun getUserId(): Resource<String> {
        val userId = auth.currentUser?.uid

        return if (userId != null) {
            Resource.Success(userId)
        } else {
            Resource.Error(NotFoundException("User Not Found"))
        }
    }

    override suspend fun updateUserProfilePhotoUrl(
        userId: String,
        imageUrl: String
    ): Resource<Unit> {
        return safeCall {
            val userDocument =
                fireStore.collection(DBCollection.User.collectionName).document(userId)
            userDocument.update("profilePhotoUrl", imageUrl).await()
            Resource.Success(Unit)
        }
    }

    override suspend fun reAuthenticateUser(email: String, password: String): Resource<Unit> =
        withContext(Dispatchers.IO) {
            safeCall {
                val user = auth.currentUser
                if (user != null) {
                    val credential = EmailAuthProvider.getCredential(email, password)
                    user.reauthenticate(credential).await()
                    Resource.Success(Unit)
                } else {
                    Resource.Error(NotFoundException("No current user to re-authenticate"))
                }
            }
        }

    override suspend fun changeUserEmail(email: String): Resource<Unit> =
        withContext(Dispatchers.IO) {
            safeCall {
                val user = auth.currentUser

                if (user != null) {
                    user.updateEmail(email).await()
                    Resource.Success(Unit)
                } else {
                    Resource.Error(NotFoundException("Can not find user."))
                }
            }
        }

    override fun getCurrentUser() = auth.currentUser

    override suspend fun changeUserPassword(newPassword: String): Resource<Unit> =
        withContext(Dispatchers.IO) {
            safeCall {
                val user = getCurrentUser()

                if (user != null) {
                    user.updatePassword(newPassword).await()
                    Resource.Success(Unit)
                } else {
                    Resource.Error(Exception("Can not find user."))
                }
            }
        }

    override suspend fun updateUserName(name: String, lastName: String): Resource<Unit> =
        withContext(Dispatchers.IO) {
            safeCall {
                val user = getCurrentUser()

                if (user != null) {
                    updateNameToFirestore(user, name, lastName)
                    updateDisplayName(user, name, lastName)
                    Resource.Success(Unit)
                } else {
                    Resource.Error(Exception("Can not find user."))
                }
            }
        }

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
                        val isFollowing = when (
                            val result = isUserFollowing(
                                currentUserId,
                                followerId
                            ).first()
                        ) {
                            is Resource.Success -> result.data
                            else -> false
                        }
                        userModel.isFollowedByCurrentUser = isFollowing
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
                        val isFollowing = when (
                            val result = isUserFollowing(
                                currentUserId,
                                followingId
                            ).first()
                        ) {
                            is Resource.Success -> result.data
                            else -> false
                        }
                        userModel.isFollowedByCurrentUser = isFollowing
                        followingUsers.add(userModel)
                    }
                }
                Resource.Success(followingUsers)
            }
        }
    }

    override suspend fun updateUserInfoFromFirestore(userId: String): Resource<Unit> =
        withContext(Dispatchers.IO) {
            safeCall {
                val document = fireStore.collection(DBCollection.User.collectionName)
                    .document(userId)
                    .get()
                    .await()

                val userDto = document.toObject(UserDto::class.java)
                userDto?.userUUID = userId

                if (userDto != null) {
                    saveUserInfoOnCache(userDto.mapToDomain())
                } else {
                    Resource.Error(Exception("No user found"))
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