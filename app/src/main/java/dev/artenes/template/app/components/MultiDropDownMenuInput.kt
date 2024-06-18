package dev.artenes.template.app.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.artenes.template.core.models.foundation.SelectableItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> MultiDropDownMenuInput(
    label: String,
    options: List<SelectableItem<T>>,
    onOptionSelected: (SelectableItem<T>) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null
) {

    var expanded by remember {
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

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = options.filter { it.selected }.joinToString(", ") { it.label },
            onValueChange = {},
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            label = { Text(text = label) },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) {
                        Icons.Filled.ArrowDropUp
                    } else {
                        Icons.Filled.ArrowDropDown
                    },
                    contentDescription = "",
                )
            },
            isError = error != null,
            supportingText = resolveSupportText {
                Text(
                    text = error!!,
                )
            },
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .exposedDropdownSize()
        ) {
            options.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Row {
                            Text(modifier = Modifier.weight(1f), text = item.label)
                            if (item.selected) {
                                Icon(imageVector = Icons.Filled.Check, contentDescription = "")
                            }
                        }
                    },
                    onClick = {
                        onOptionSelected(item)
                    })
            }
        }
    }

}