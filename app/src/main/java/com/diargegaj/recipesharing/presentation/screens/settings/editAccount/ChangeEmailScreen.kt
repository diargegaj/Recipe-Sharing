package com.diargegaj.recipesharing.presentation.screens.settings.editAccount

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.diargegaj.recipesharing.R
import com.diargegaj.recipesharing.presentation.viewModel.settings.changeEmail.ChangeEmailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeEmailScreen(
    viewModel: ChangeEmailViewModel = hiltViewModel()
) {
    val state by viewModel.changeEmailState.collectAsState()
    var newEmail by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.showAuthDialog) {
            AuthDialog(
                email = state.oldEmail,
                password = state.password,
                newEmail = {
                    viewModel.oldEmailUpdated(it)
                },
                newPassword = {
                    viewModel.passwordUpdated(it)
                },
                onDismiss = { viewModel.onDismissDialog() },
                onConfirm = {
                    viewModel.confirmOldEmail()
                }
            )
        }
        TextField(
            value = state.newEmail,
            onValueChange = { newEmail = it },
            label = { Text(text = stringResource(id = R.string.new_email)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.onEmailChange()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.update_email))
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthDialog(
    email: String,
    password: String,
    newEmail: (email: String) -> Unit,
    newPassword: (password: String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.authentication),
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = email,
                    onValueChange = { newEmail(it) },
                    label = { Text(text = stringResource(id = R.string.old_email)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = password,
                    onValueChange = { newPassword(it) },
                    label = { Text(text = stringResource(id = R.string.password)) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            onDismiss()
                        }
                    ) {
                        Text(text = stringResource(id = R.string.cancel))
                    }

                    Button(
                        onClick = {
                            onConfirm()
                        }
                    ) {
                        Text(text = stringResource(id = R.string.confirm))
                    }
                }
            }
        }
    }
}
