package com.diargegaj.recipesharing.presentation.screens.settings.editProfile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.diargegaj.recipesharing.R
import com.diargegaj.recipesharing.presentation.navigation.RecipeNavigationActions
import com.diargegaj.recipesharing.presentation.utils.DefaultAppBar
import com.diargegaj.recipesharing.presentation.utils.ImagePicker
import com.diargegaj.recipesharing.presentation.utils.LoadImage
import com.diargegaj.recipesharing.presentation.viewModel.home.profile.UserProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeProfilePictureScreen(
    recipeNavigationActions: RecipeNavigationActions,
    viewModel: UserProfileViewModel = hiltViewModel()
) {
    val state by viewModel.userState.collectAsState()
    val userMessage by viewModel.messages.collectAsState(initial = null)

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
                    title = stringResource(id = R.string.change_profile_picture),
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(160.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    LoadImage(
                        imageUrl = state.profilePhotoUrl,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                    ImagePicker(
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.BottomEnd),
                        image = null,
                        editView = {
                            Image(
                                painter = painterResource(id = R.drawable.ic_edit),
                                contentDescription = "Edit icon"
                            )
                        },
                        onImagePicked = { pickedImage ->
                            viewModel.updateProfileImage(pickedImage)
                        }
                    )
                }
            }
        }
    }
}