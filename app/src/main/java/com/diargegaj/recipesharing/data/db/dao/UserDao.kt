package com.diargegaj.recipesharing.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.diargegaj.recipesharing.data.db.entities.user.UserEntity
import com.diargegaj.recipesharing.data.db.entities.user.UserWithRecipes
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUser(userId: String): Flow<UserEntity?>

    @Query("DELETE FROM users")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserWithRecipes(userId: String): Flow<UserWithRecipes?>

}