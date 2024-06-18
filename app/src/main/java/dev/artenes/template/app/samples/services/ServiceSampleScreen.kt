package dev.artenes.template.app.samples.services

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@SuppressLint("InlinedApi")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ServiceSampleScreen(
    back: () -> Unit,
    viewModel: ServiceSampleViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Services Sample") },
                navigationIcon = {
                    IconButton(onClick = back) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ) { edges ->

        val notificationPermission =
            rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

        Column(
            modifier = Modifier
                .padding(edges)
                .padding(20.dp)
        ) {

            Button(modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (!notificationPermission.status.isGranted) {
                        notificationPermission.launchPermissionRequest()
                        return@Button
                    }
                    viewModel.startTimer()
                }) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "Start timer"
                )
            }

            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
                onClick = {
                    if (!notificationPermission.status.isGranted) {
                        notificationPermission.launchPermissionRequest()
                        return@Button
                    }
                    viewModel.stopTimer()
                }) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "Stop timer"
                )
            }

        }

    }


}