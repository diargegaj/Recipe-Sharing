package com.diargegaj.recipesharing.data.datasource.firestore

import DBCollection
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreUserFollowDataSource @Inject constructor(
    private val fireStore: FirebaseFirestore
) {
    suspend fun isUserFollowing(loggedInUserId: String, otherUserId: String): DocumentSnapshot? {
        return fireStore.collection(DBCollection.Following.collectionName)
            .document(loggedInUserId)
            .collection(DBCollection.UserFollowing.collectionName)
            .document(otherUserId)
            .get()
            .await()
    }

    suspend fun followUser(loggedInUserId: String, otherUserId: String) {
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
    }

    suspend fun unfollowUser(loggedInUserId: String, otherUserId: String) {

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
    }
}