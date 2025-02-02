package com.example.comicsappmobile.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.ui.theme.ComicsAppMobileTheme


@Composable
fun ThemedErrorCard(
    errorType: String = "Not allowed",
    message: String = "Any message",
    statusCode: Int = -1,
    modifier: Modifier = Modifier
        .fillMaxWidth().padding(12.dp)
) {
    OutlinedCard(
        modifier = modifier,
        colors = CardDefaults.outlinedCardColors().copy(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
    ) {
        Column(
            modifier = Modifier
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = errorType,
                    modifier = Modifier.width(256.dp),
                    style = MaterialTheme.typography.displaySmall.copy(
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                )
                Text(
                    text = statusCode.toString(),
                    style = MaterialTheme.typography.displaySmall.copy(
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                )
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.error)
            Text(
                text = message,
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            )
        }
    }
}


@Composable
fun ThemedErrorCard(
    uiState: UiState.Error<*>,
    modifier: Modifier = Modifier.fillMaxWidth().padding(12.dp)
){
    ThemedErrorCard(
        errorType = uiState.typeError ?: "UiState.Error.typeError is null",
        message = uiState.message ?: "UiState.Error.message is null",
        statusCode = uiState.statusCode,
        modifier = modifier
    )
}


@Preview
@Composable
fun ThemedErrorCardPreview(){
    ComicsAppMobileTheme {
        val testUiState = UiState.Error(
            statusCode = -1,
            typeError = null,
            message = null,
            data = null
        )
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.align(Alignment.Center)) {
                ThemedErrorCard(modifier = Modifier.fillMaxWidth().padding(12.dp))

                ThemedErrorCard(uiState = testUiState)
            }
        }
    }
}