package com.diargegaj.recipesharing.data.datasource.firestore

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserAuthDataSource @Inject constructor(
    private val auth: FirebaseAuth
) {
    suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult? {
        return auth.createUserWithEmailAndPassword(email, password).await()
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult? {
        return auth.signInWithEmailAndPassword(email, password).await()
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}