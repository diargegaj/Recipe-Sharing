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
import com.diargegaj.recipesharing.domain.models.UserModel
import com.diargegaj.recipesharing.domain.repository.UserRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                Resource.Success(result.user!!)
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }

    override suspend fun addUserAdditionalInformation(userModel: UserModel): Resource<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val userDto = userModel.mapToDto()
                fireStore.collection(DBCollection.User.collectionName)
                    .document(userDto.userUUID)
                    .set(userDto)
                    .await()

                val userEntity = userDto.mapToEntity()
                userDao.insert(userEntity)

                Resource.Success(Unit)
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }

    override suspend fun logIn(email: String, password: String): Resource<FirebaseUser> =
        withContext(Dispatchers.IO) {
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                Resource.Success(result.user!!)
            } catch (e: Exception) {
                Resource.Error(e)
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
            try {
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
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }

    override suspend fun getUserInfoFromCache(userId: String): Resource<UserModel> =
        withContext(Dispatchers.IO) {
            try {
                val userEntity = userDao.getUser(userId).first()
                if (userEntity != null) {
                    Resource.Success(userEntity.mapToUserModel())
                } else {
                    Resource.Error(NotFoundException("User not found in cache"))
                }
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }

    override suspend fun saveUserInfoOnCache(userModel: UserModel): Resource<Any> =
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

}