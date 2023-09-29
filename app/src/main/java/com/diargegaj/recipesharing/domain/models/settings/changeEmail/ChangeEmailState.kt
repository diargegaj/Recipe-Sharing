package com.diargegaj.recipesharing.domain.models.settings.changeEmail

data class ChangeEmailState(
    val oldEmail: String = "",
    val newEmail: String = "",
    val password: String = "",
    val showAuthDialog: Boolean = false
)