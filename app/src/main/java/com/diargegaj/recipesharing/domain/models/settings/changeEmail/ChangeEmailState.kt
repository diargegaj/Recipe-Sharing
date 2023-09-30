package com.diargegaj.recipesharing.domain.models.settings.changeEmail

import com.diargegaj.recipesharing.domain.utils.Resource

data class ChangeEmailState(
    val oldEmail: String = "",
    val newEmail: String = "",
    val password: String = "",
    val reAuthState: Resource<Unit> = Resource.Success(Unit),
    val newEmailState: Resource<Unit> = Resource.Success(Unit),
    val showAuthDialog: Boolean = false
)