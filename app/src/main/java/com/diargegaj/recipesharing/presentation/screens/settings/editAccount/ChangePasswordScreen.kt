package com.diargegaj.recipesharing.presentation.screens.settings.editAccount

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.diargegaj.recipesharing.R
import com.diargegaj.recipesharing.presentation.navigation.RecipeNavigationActions
import com.diargegaj.recipesharing.presentation.utils.DefaultAppBar
import com.diargegaj.recipesharing.presentation.viewModel.settings.changeEmail.ChangePasswordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    recipeNavigationActions: RecipeNavigationActions,
    viewModel: ChangePasswordViewModel = hiltViewModel()
) {
    val state by viewModel.changePasswordState.collectAsState()
    val userMessage by viewModel.userMessageEvent.collectAsState(initial = null)

    val snackbarHostState = remember { SnackbarHostState() }

    userMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message = message)
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        Scaffold(
            topBar = {
                DefaultAppBar(
                    title = stringResource(id = R.string.change_password),
                    navigationIcon = Icons.Default.ArrowBack,
                    onNavigationClick = { recipeNavigationActions.goBack() }
                )
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
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                OutlinedTextField(
                    value = state.oldPassword,
                    onValueChange = {
                        viewModel.oldPasswordUpdated(it)
                    },
                    label = { Text(stringResource(id = R.string.old_password)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                OutlinedTextField(
                    value = state.newPassword,
                    onValueChange = {
                        viewModel.newPasswordUpdated(it)
                    },
                    label = { Text(stringResource(id = R.string.new_password)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                OutlinedTextField(
                    value = state.confirmPassword,
                    onValueChange = {
                        viewModel.confirmPasswordUpdated(it)
                    },
                    label = { Text(stringResource(id = R.string.confirm_password)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Button(onClick = {
                    viewModel.changePassword()
                }) {
                    Text(text = stringResource(id = R.string.change_password))
                }

            }
        }
    }

}