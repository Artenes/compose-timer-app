package dev.artenes.template.app.samples.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainSamplesScreen(
    navigateToFieldsSample: () -> Unit,
    navigateToNotificationsSample: () -> Unit,
    navigateToServicesSample: () -> Unit,
) {

    Scaffold { edges ->

        Column(modifier = Modifier
            .padding(edges)
            .padding(all = 30.dp)) {

            Button(onClick = navigateToFieldsSample) {

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    text = "Fields Sample",
                    style = MaterialTheme.typography.bodyLarge
                )

            }

            Button(
                onClick = navigateToNotificationsSample,
                modifier = Modifier.padding(top = 20.dp)
            ) {

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    text = "Notifications Sample",
                    style = MaterialTheme.typography.bodyLarge
                )

            }

            Button(
                onClick = navigateToServicesSample,
                modifier = Modifier.padding(top = 20.dp)
            ) {

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    text = "Services Sample",
                    style = MaterialTheme.typography.bodyLarge
                )

            }

        }

    }

}