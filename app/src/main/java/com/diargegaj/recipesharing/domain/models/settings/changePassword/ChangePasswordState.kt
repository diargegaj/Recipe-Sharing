package com.diargegaj.recipesharing.domain.models.settings.changePassword

data class ChangePasswordState(
    var oldPassword: String = "",
    var newPassword: String = "",
    var confirmPassword: String = ""
)
