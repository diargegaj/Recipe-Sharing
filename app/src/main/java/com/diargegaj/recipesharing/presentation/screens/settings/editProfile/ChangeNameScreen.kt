package com.diargegaj.recipesharing.presentation.screens.settings.editProfile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.diargegaj.recipesharing.R
import com.diargegaj.recipesharing.presentation.navigation.RecipeNavigationActions
import com.diargegaj.recipesharing.presentation.utils.DefaultAppBar
import com.diargegaj.recipesharing.presentation.viewModel.settings.changeName.ChangeNameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeNameScreen(
    recipeNavigationActions: RecipeNavigationActions,
    viewModel: ChangeNameViewModel = hiltViewModel()
) {
    val state by viewModel.changeNameState.collectAsState()
    val userMessage by viewModel.userMessageEvent.collectAsState(initial = null)

    val snackbarHostState = remember { SnackbarHostState() }

    userMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message = message)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                DefaultAppBar(
                    title = stringResource(id = R.string.change_name),
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
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                TextField(
                    value = state.name,
                    onValueChange = { name ->
                        viewModel.nameUpdated(name)
                    },
                    label = { Text(stringResource(R.string.first_name)) },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = state.lastName,
                    onValueChange = { lastName ->
                        viewModel.lastNameUpdated(lastName)
                    },
                    label = { Text(stringResource(R.string.last_name)) },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(onClick = {
                    viewModel.onNameChange()
                }) {
                    Text(stringResource(R.string.change_name))
                }
            }
        }
    }
}