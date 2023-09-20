package com.diargegaj.recipesharing.data.repository

import com.diargegaj.recipesharing.data.db.dao.UserDao
import com.diargegaj.recipesharing.data.mappers.mapToDto
import com.diargegaj.recipesharing.data.mappers.mapToEntity
import com.diargegaj.recipesharing.domain.models.UserModel
import com.diargegaj.recipesharing.domain.repository.UserRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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
                fireStore.collection("users")
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

    override fun isUserLoggedIn(): Flow<Boolean> {
        val currentUser = auth.currentUser
        return if (currentUser != null) {
            userDao.getUser(currentUser.uid).map { it != null }
        } else {
            flowOf(false)
        }
    }
}