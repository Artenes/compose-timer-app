package dev.artenes.template.app.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import dev.artenes.template.core.models.foundation.FormattedValue
import dev.artenes.template.core.models.foundation.SelectableItem
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthPicker(
    value: FormattedValue<LocalDate>,
    onMonthSelected: (LocalDate) -> Unit,
    label: String,
    buttonLabel: String,
    modifier: Modifier = Modifier,
    error: String? = null
) {

    val resolveSupportText: (content: @Composable (() -> Unit)) -> @Composable (() -> Unit)? =
        { content ->
            if (error != null) {
                content
            } else {
                null
            }
        }

    val focusManager = LocalFocusManager.current
    val coroutine = rememberCoroutineScope()

    var visible by remember {
        mutableStateOf(false)
    }

    val onDismiss = {
        visible = false
    }

    var years by remember {
        mutableStateOf(makeYears(value.original))
    }

    var months by remember {
        mutableStateOf(makeMonths(value.original))
    }

    val yearListState = rememberLazyListState(
        initialFirstVisibleItemIndex = calculateInitialIndex(years.first { it.selected })
    )

    val monthListState = rememberLazyListState(
        initialFirstVisibleItemIndex = calculateInitialIndex(months.first { it.value == LocalDate.now().monthValue })
    )

    val dismissDialog = {
        years = makeYears(value.original)
        months = makeMonths(value.original)
        coroutine.launch {
            yearListState.scrollToItem(calculateInitialIndex(years.first { it.selected }))
            monthListState.scrollToItem(calculateInitialIndex(months.first { it.value == LocalDate.now().monthValue }))
        }
        onDismiss()
    }

    if (visible) {
        BasicAlertDialog(onDismissRequest = dismissDialog) {
            Surface(
                color = DatePickerDefaults.colors().containerColor,
                shape = DatePickerDefaults.shape,
                modifier = Modifier
                    .heightIn(max = 580.dp)
                    .requiredWidth(360.dp)
            ) {

                Column(
                    modifier = Modifier.padding(30.dp)
                ) {

                    /*
                    Years
                     */
                    DateRow(state = yearListState, items = years) { yearItem ->
                        years =
                            years.map { item -> item.copy(selected = yearItem == item) }.toList()
                    }

                    /*
                    Months
                     */
                    DateRow(state = monthListState, items = months) { monthItem ->
                        months =
                            months.map { item -> item.copy(selected = monthItem == item) }.toList()
                    }

                    Button(
                        onClick = {
                            val year = years.first { it.selected }.value
                            val month = months.first { it.selected }.value
                            onMonthSelected(LocalDate.of(year, month, 1))
                            dismissDialog()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        enabled = years.find { it.selected } != null && months.find { it.selected } != null
                    ) {
                        Text(text = buttonLabel)
                    }

                }

            }
        }
    }

    OutlinedTextField(
        value = value.formatted,
        onValueChange = { },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusEvent {
                if (it.isFocused) {
                    visible = true
                    focusManager.clearFocus(force = true)
                }
            }
            .then(modifier),
        label = {
            Text(label)
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Filled.CalendarMonth,
                contentDescription = "",
            )
        },
        isError = error != null,
        supportingText = resolveSupportText {
            Text(
                text = error!!,
            )
        },
        readOnly = true
    )

}

private fun calculateInitialIndex(item: SelectableItem<Int>): Int {
    val intendedPosition = item.position - 2
    return if (intendedPosition >= 0) {
        intendedPosition
    } else {
        0
    }
}

private fun makeMonths(value: LocalDate?): List<SelectableItem<Int>> {
    val months = Month.entries.mapIndexed { index, month ->
        val label = month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val monthValue = month.value
        SelectableItem(
            value = monthValue,
            label = label,
            selected = monthValue == value?.month?.value,
            position = index
        )
    }
    return months
}

private fun makeYears(value: LocalDate?): List<SelectableItem<Int>> {
    val realValue = value ?: LocalDate.now()
    val middle = realValue.year
    val start = middle - 5
    val end = middle + 5
    return (start..end).mapIndexed { index, item ->
        SelectableItem(
            value = item,
            label = item.toString(),
            selected = item == middle,
            position = index
        )
    }.toList()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateRow(
    state: LazyListState,
    items: List<SelectableItem<Int>>,
    onClick: (SelectableItem<Int>) -> Unit
) {

    LazyRow(
        state = state
    ) {

        items(
            count = items.size,
            key = { index -> items[index].value }
        ) { index ->

            val dateItem = items[index]

            Surface(
                color = if (dateItem.selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                onClick = {
                    onClick(dateItem)
                },
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = dateItem.label
                )
            }

        }

    }
}