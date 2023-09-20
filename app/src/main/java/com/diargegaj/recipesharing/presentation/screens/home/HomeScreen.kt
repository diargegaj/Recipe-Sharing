package com.diargegaj.recipesharing.presentation.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.diargegaj.recipesharing.R
import com.diargegaj.recipesharing.presentation.utils.TopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    titleText = stringResource(id = R.string.app_name),
                    navigationIcon = Icons.Default.Menu,
                    navigationIconCLick = {
                        TODO(" Handle icon click...")
                    }
                )
            }
        ) {

            Column(modifier = Modifier.padding(paddingValues = it)) {

            }

        }

    }

}