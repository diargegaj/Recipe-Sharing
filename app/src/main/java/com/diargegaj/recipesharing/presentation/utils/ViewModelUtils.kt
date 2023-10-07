package com.diargegaj.recipesharing.presentation.utils

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry

@Composable
inline fun <reified VM: ViewModel> hiltViewModelFromEntry(entry: NavBackStackEntry?) : VM {
    return entry?.let { hiltViewModel<VM>(it) } ?: hiltViewModel<VM>()
}