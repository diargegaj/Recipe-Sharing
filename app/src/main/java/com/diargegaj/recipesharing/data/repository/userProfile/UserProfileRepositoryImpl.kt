package com.diargegaj.recipesharing.data.repository.userProfile

import DBCollection
import android.content.res.Resources
import com.diargegaj.recipesharing.data.db.dao.UserDao
import com.diargegaj.recipesharing.data.mappers.mapToDomain
import com.diargegaj.recipesharing.data.mappers.mapToDto
import com.diargegaj.recipesharing.data.mappers.mapToEntity
import com.diargegaj.recipesharing.data.mappers.mapToUserModel
import com.diargegaj.recipesharing.data.models.UserDto
import com.diargegaj.recipesharing.data.utils.Constants
import com.diargegaj.recipesharing.data.utils.safeCall
import com.diargegaj.recipesharing.domain.models.UserModel
import com.diargegaj.recipesharing.domain.repository.userProfile.UserProfileRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val userDao: UserDao
) : UserProfileRepository {

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
                    Resource.Error(Resources.NotFoundException("User not found!"))
                }
            }
        }

    override fun getUserInfoFromCache(userId: String): Flow<Resource<UserModel>> {
        return userDao.getUserWithRecipes(userId)
            .map { userEntity ->
                if (userEntity != null) {
                    Resource.Success(userEntity.mapToUserModel())
                } else {
                    Resource.Error(Resources.NotFoundException("User not found"))
                }
            }
            .catch {
                emit(Resource.Error(Exception(it.message)))
            }
    }

    override suspend fun saveUserInfoOnCache(userModel: UserModel): Resource<Unit> =
        withContext(Dispatchers.IO) {
            val result = userDao.insert(userModel.mapToDto().mapToEntity())
            if (result != Constants.INSERTION_FAILED) {
                Resource.Success(Unit)
            } else {
                Resource.Error(Exception("Failed to insert user info into cache."))
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

    override suspend fun updateUserName(name: String, lastName: String): Resource<Unit> =
        withContext(Dispatchers.IO) {
            safeCall {
//                val user = getCurrentUser()
//
//                if (user != null) {
//                    updateNameToFirestore(user, name, lastName)
//                    updateDisplayName(user, name, lastName)

                Resource.Success(Unit)
//                } else {
//                    Resource.Error(Exception("Can not find user."))
//                }
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
}