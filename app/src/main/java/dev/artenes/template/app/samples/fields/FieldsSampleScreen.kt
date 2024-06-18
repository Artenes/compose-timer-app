package dev.artenes.template.app.samples.fields

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.artenes.template.app.components.DatePickerInput
import dev.artenes.template.app.components.DropDownMenuInput
import dev.artenes.template.app.components.MonthPicker
import dev.artenes.template.app.components.MultiDropDownMenuInput
import dev.artenes.template.core.models.foundation.FormattedValue
import dev.artenes.template.core.models.foundation.SelectableItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FieldsSampleScreen(
    back: () -> Unit
) {

    var firstText by remember {
        mutableStateOf("")
    }

    var secondText by remember {
        mutableStateOf("")
    }

    var thirdText by remember {
        mutableStateOf("")
    }

    var options by remember {
        mutableStateOf(listOf(SelectableItem("Value A"), SelectableItem("Value B")))
    }

    var multiOptions by remember {
        mutableStateOf(
            listOf(
                SelectableItem("Value A"),
                SelectableItem("Value B"),
                SelectableItem("Value C")
            )
        )
    }

    var date by remember {
        mutableStateOf(
            FormattedValue(
                LocalDate.now(),
                LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
            )
        )
    }

    var month by remember {
        mutableStateOf(
            FormattedValue(
                LocalDate.now(),
                LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM yyyy"))
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Fields Sample") },
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

        Column(
            modifier = Modifier
                .padding(edges)
                .padding(20.dp)
        ) {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = firstText,
                onValueChange = { value ->
                    firstText = value
                },
                label = {
                    Text(text = "Sample Label")
                },
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                value = secondText,
                onValueChange = { value ->
                    secondText = value
                },
                label = {
                    Text(text = "Field with error")
                },
                isError = true,
                supportingText = {
                    Text(text = "There is an error")
                }
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                value = thirdText,
                onValueChange = { value ->
                    thirdText = value
                },
                placeholder = {
                    Text(text = "Sample placeholder")
                },
            )

            DropDownMenuInput(
                modifier = Modifier.padding(top = 20.dp),
                label = "Drop down input",
                options = options,
                onOptionSelected = { value ->
                    options = options.map { it.copy(selected = it == value) }
                }
            )

            MultiDropDownMenuInput(
                modifier = Modifier.padding(top = 20.dp),
                label = "Multi Drop down input",
                options = multiOptions,
                onOptionSelected = { value ->
                    multiOptions = multiOptions.map {
                        if (it == value) {
                            it.copy(selected = !value.selected)
                        } else {
                            it
                        }
                    }
                }
            )

            DatePickerInput(
                modifier = Modifier.padding(top = 20.dp),
                label = "Select a date",
                buttonLabel = "Select date",
                value = date,
                onDateSelected = { value ->
                    date = FormattedValue(
                        value,
                        value.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
                    )
                }
            )

            MonthPicker(
                modifier = Modifier.padding(top = 20.dp),
                value = month,
                onMonthSelected = { value ->
                    month = FormattedValue(
                        value,
                        value.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
                    )
                },
                label = "Select a month",
                buttonLabel = "Select month"
            )

        }

    }

}