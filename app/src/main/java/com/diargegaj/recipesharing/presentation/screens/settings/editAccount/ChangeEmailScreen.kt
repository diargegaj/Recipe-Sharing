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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.diargegaj.recipesharing.R
import com.diargegaj.recipesharing.domain.utils.Resource
import com.diargegaj.recipesharing.presentation.navigation.RecipeNavigationActions
import com.diargegaj.recipesharing.presentation.utils.DefaultAppBar
import com.diargegaj.recipesharing.presentation.viewModel.settings.changeEmail.ChangeEmailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeEmailScreen(
    recipeNavigationActions: RecipeNavigationActions,
    viewModel: ChangeEmailViewModel = hiltViewModel()
) {
    val state by viewModel.changeEmailState.collectAsState()
    val userMessage by viewModel.userMessageEvent.collectAsState(initial = null)

    val snackbarHostState = remember { SnackbarHostState() }

    userMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message = message)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                DefaultAppBar(
                    title = stringResource(id = R.string.change_email),
                    navigationIcon = Icons.Default.ArrowBack,
                    onNavigationClick = { recipeNavigationActions.goBack() })
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    Snackbar(
                        action = {
                            TextButton(onClick = {
                                data.dismiss()
                            }) {
                                Text(text = stringResource(id = R.string.dismiss))
                            }
                        },
                        content = {
                            Text(text = data.visuals.message)
                        }
                    )
                }
            }

        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.showAuthDialog) {
                    AuthDialog(
                        email = state.oldEmail,
                        password = state.password,
                        processState = state.reAuthState,
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


                EmailField(
                    email = state.newEmail,
                    state.newEmailState,
                    onEmailChanged = {
                        viewModel.newEmailUpdated(it)
                    }
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthDialog(
    email: String,
    password: String,
    processState: Resource<*>,
    newEmail: (email: String) -> Unit,
    newPassword: (password: String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val isError = processState is Resource.Error
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

                EmailField(email = email, processState, onEmailChanged = {
                    newEmail(it)
                })

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = password,
                    onValueChange = {
                        newPassword(it)
                    },
                    label = { Text(text = stringResource(id = R.string.password)) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = if (isError) TextFieldDefaults.textFieldColors(
                        errorLabelColor = Color.Red,
                        errorCursorColor = Color.Red,
                        textColor = Color.Red
                    ) else TextFieldDefaults.textFieldColors(),
                    isError = isError
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
                            if (email.isNotEmpty() && password.isNotEmpty()) {
                                onConfirm()
                            }
                        }
                    ) {
                        Text(text = stringResource(id = R.string.confirm))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailField(email: String, state: Resource<*>, onEmailChanged: (String) -> Unit) {
    val isError = state is Resource.Error
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = email,
        onValueChange = {
            onEmailChanged(it)
        },
        label = {
            Text(text = stringResource(id = R.string.email))
        },
        colors = if (isError) TextFieldDefaults.textFieldColors(
            errorLabelColor = Color.Red,
            errorCursorColor = Color.Red,
            textColor = Color.Red
        ) else TextFieldDefaults.textFieldColors(),
        isError = isError
    )
    if (isError) {
        val message = (state as Resource.Error).exception.message ?: ""
        Text(text = message, color = Color.Red, fontSize = 12.sp)
    }
}
