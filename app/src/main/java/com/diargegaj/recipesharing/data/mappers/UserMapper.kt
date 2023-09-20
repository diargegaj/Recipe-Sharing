package com.diargegaj.recipesharing.data.mappers

import com.diargegaj.recipesharing.data.db.entities.UserEntity
import com.diargegaj.recipesharing.data.models.UserDto
import com.diargegaj.recipesharing.domain.models.UserModel

fun UserModel.mapToDto(): UserDto {
    return UserDto(
        userUUID = this.userUUID,
        name = this.name,
        lastName = this.lastName,
        email = this.email,
        profilePhotoUrl = this.profilePhotoUrl
    )
}

fun UserDto.mapToEntity(): UserEntity {
    return UserEntity(
        id = this.userUUID,
        name = this.name,
        lastName = this.lastName,
        email = this.email,
        profilePhotoUrl = this.profilePhotoUrl
    )
}