package dev.artenes.template.app.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import dev.artenes.template.core.models.foundation.FormattedValue
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerInput(
    label: String,
    value: FormattedValue<LocalDate>,
    onDateSelected: (LocalDate) -> Unit,
    buttonLabel: String,
    modifier: Modifier = Modifier,
    error: String? = null
) {

    val focusManager = LocalFocusManager.current
    var showDatePickerDialog by remember {
        mutableStateOf(false)
    }

    val resolveSupportText: (content: @Composable (() -> Unit)) -> @Composable (() -> Unit)? =
        { content ->
            if (error != null) {
                content
            } else {
                null
            }
        }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = OffsetDateTime.of(
            value.original,
            LocalTime.now(),
            ZoneOffset.UTC
        )
            .toInstant().toEpochMilli()
    )

    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            onDateSelected(
                                Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC"))
                                    .toLocalDate()
                            )
                        }
                        showDatePickerDialog = false
                    },
                ) {
                    Text(
                        text = buttonLabel,
                    )
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    OutlinedTextField(
        value = value.formatted,
        onValueChange = { },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusEvent {
                if (it.isFocused) {
                    showDatePickerDialog = true
                    focusManager.clearFocus(force = true)
                }
            }
            .then(modifier),
        label = {
            Text(label)
        },
        isError = error != null,
        supportingText = resolveSupportText {
            Text(
                text = error!!,
            )
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Filled.CalendarMonth,
                contentDescription = "",
            )
        },
        readOnly = true
    )

}