package com.diargegaj.recipesharing.data.db.entities.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String = "",
    val name: String,
    val lastName: String,
    val email: String,
    val profilePhotoUrl: String
)
