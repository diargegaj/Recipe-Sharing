package com.diargegaj.recipesharing.data.models

data class UserDto(
    var userUUID: String = "",
    val name: String = "",
    val lastName: String = "",
    val email: String = "",
    val profilePhotoUrl: String = "",
    val followersCount: Int? = 0,
    val followingCount: Int? = 0
)