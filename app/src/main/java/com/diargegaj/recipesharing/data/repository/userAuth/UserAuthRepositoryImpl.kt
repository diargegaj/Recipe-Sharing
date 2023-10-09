package com.diargegaj.recipesharing.data.repository.userAuth

import android.content.res.Resources
import com.diargegaj.recipesharing.data.db.dao.UserDao
import com.diargegaj.recipesharing.data.utils.safeCall
import com.diargegaj.recipesharing.domain.repository.userAuth.UserAuthRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val userDao: UserDao
) : UserAuthRepository {

    override suspend fun registerUser(email: String, password: String): Resource<FirebaseUser> =
        withContext(Dispatchers.IO) {
            safeCall {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                Resource.Success(result.user!!)
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

    override fun getUserId(): Resource<String> {
        val userId = auth.currentUser?.uid

        return if (userId != null) {
            Resource.Success(userId)
        } else {
            Resource.Error(Resources.NotFoundException("User Not Found"))
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
                    Resource.Error(Resources.NotFoundException("No current user to re-authenticate"))
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
                    Resource.Error(Resources.NotFoundException("Can not find user."))
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

}