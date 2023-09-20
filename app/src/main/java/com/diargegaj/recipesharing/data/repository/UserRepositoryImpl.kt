package com.diargegaj.recipesharing.data.repository

import com.diargegaj.recipesharing.data.mappers.mapToDto
import com.diargegaj.recipesharing.domain.models.UserModel
import com.diargegaj.recipesharing.domain.repository.UserRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
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
                fireStore.collection("")
                    .document(userDto.userUUID)
                    .set(userDto)
                    .await()

                Resource.Success(Unit)
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }
}