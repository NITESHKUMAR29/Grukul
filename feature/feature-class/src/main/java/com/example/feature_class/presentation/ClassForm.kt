package com.example.feature_class.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.core_common.resut.UiState
import com.example.core_ui.components.buttons.GurukulPrimaryButton
import com.example.core_ui.components.feedbackText.ErrorText
import com.example.core_ui.components.spinners.CommonSpinner
import com.example.core_ui.ui.textFields.GurukulTextField
import kotlinx.coroutines.launch

/* ---------------------------------------------------------
   SCREEN
--------------------------------------------------------- */

@Composable
fun ClassFormScreen(
    viewModel: ClassViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val createState by viewModel.createClassState.collectAsState()

    var className by remember { mutableStateOf("") }
    var teacherName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var selectedActive by remember { mutableStateOf("Active") }
    var selectedGender by remember { mutableStateOf("Boys") }
    var selectedDays by remember { mutableStateOf(setOf<String>()) }
    var loading by remember { mutableStateOf(false) }
    val dayMap = mapOf(
        "Mon" to "M",
        "Tue" to "T",
        "Wed" to "W",
        "Thu" to "Th",
        "Fri" to "F",
        "Sat" to "S",
        "Sun" to "Su"
    )

    Log.d("ClassForm", "state = $createState")

    when (createState) {
        is UiState.Loading -> loading = true

        is UiState.Success -> {
            loading = false
            LaunchedEffect(Unit) { onBack() }
        }

        is UiState.Error -> {
            loading = false
            Log.e("ClassForm", (createState as UiState.Error).message)
        }

        UiState.Idle -> Unit
        else -> {}
    }

    ClassFormContent(
        className = className,
        teacherName = teacherName,
        address = address,
        selectedActive = selectedActive,
        selectedGender = selectedGender,
        selectedDays = selectedDays,
        onClassNameChange = { className = it },
        onTeacherNameChange = { teacherName = it },
        onAddressChange = { address = it },
        onActiveChange = { selectedActive = it },
        onGenderChange = { selectedGender = it },
        onDayToggle = { day ->
            selectedDays =
                if (selectedDays.contains(day)) selectedDays - day
                else selectedDays + day
        },
        loading = loading,
        onSubmit = {
            viewModel.createClass(
                className = className,
                teacherName = teacherName,
                isActive = selectedActive == "Active",
                gender = selectedGender,
                address = address,
                schedule = selectedDays
                    .map { dayMap[it] ?: it.first().uppercaseChar().toString() }
                    .joinToString("/")
            )
        }
    )
}



@Composable
fun ClassFormContent(
    className: String,
    teacherName: String,
    address: String,
    selectedActive: String,
    selectedGender: String,
    selectedDays: Set<String>,
    onClassNameChange: (String) -> Unit,
    onTeacherNameChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onActiveChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
    onDayToggle: (String) -> Unit,
    onSubmit: () -> Unit,
    loading: Boolean
) {
    val active = listOf("Active", "Inactive")
    val gender = listOf("Boys", "Girls")
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")


    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    var showErrors by remember { mutableStateOf(false) }

    // Positions for auto-scroll
    var classNameY by remember { mutableStateOf(0f) }
    var teacherNameY by remember { mutableStateOf(0f) }
    var daysY by remember { mutableStateOf(0f) }

    val isFormValid = remember(
        className,
        teacherName,
        selectedActive,
        selectedGender,
        selectedDays
    ) {
        className.isNotBlank() &&
                teacherName.isNotBlank() &&
                selectedActive.isNotBlank() &&
                selectedGender.isNotBlank() &&
                selectedDays.isNotEmpty()
    }

    fun scrollToFirstError() {
        scope.launch {
            when {
                className.isBlank() ->
                    scrollState.animateScrollTo(classNameY.toInt())

                teacherName.isBlank() ->
                    scrollState.animateScrollTo(teacherNameY.toInt())

                selectedDays.isEmpty() ->
                    scrollState.animateScrollTo(daysY.toInt())
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 88.dp)
        ) {

            Spacer(Modifier.height(16.dp))

            Text(
                "New Class",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(8.dp))


            GurukulTextField(
                value = className,
                onValueChange = onClassNameChange,
                hint = "Class Name *",
                isError = showErrors && className.isBlank(),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .onGloballyPositioned {
                        classNameY = it.positionInParent().y
                    }
            )

            if (showErrors && className.isBlank()) {
                ErrorText("Class name is required")
            }


            GurukulTextField(
                value = teacherName,
                onValueChange = onTeacherNameChange,
                hint = "Teacher Name *",
                isError = showErrors && teacherName.isBlank(),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .onGloballyPositioned {
                        teacherNameY = it.positionInParent().y
                    }
            )

            if (showErrors && teacherName.isBlank()) {
                ErrorText("Teacher name is required")
            }

            Spacer(Modifier.height(8.dp))


            CommonSpinner(
                label = "Status *",
                items = active,
                selectedItem = selectedActive,
                itemLabel = { it },
                onItemSelected = onActiveChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(16.dp))


            CommonSpinner(
                label = "Gender *",
                items = gender,
                selectedItem = selectedGender,
                itemLabel = { it },
                onItemSelected = onGenderChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(16.dp))

            GurukulTextField(
                value = address,
                onValueChange = onAddressChange,
                hint = "Full Address (Optional)",
                keyboardType = KeyboardType.Text,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .height(100.dp)
            )

            Spacer(Modifier.height(16.dp))


            Text(
                "Class Days *",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .onGloballyPositioned {
                        daysY = it.positionInParent().y
                    }
            )

            FlowRow(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                days.forEach { day ->
                    val selected = selectedDays.contains(day)

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                if (selected)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                            .clickable { onDayToggle(day) }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = day,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (selected)
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }



            if (showErrors && selectedDays.isEmpty()) {
                ErrorText("Please select at least one day")
            }

            Spacer(Modifier.height(24.dp))
        }


        GurukulPrimaryButton(
            text = "Submit",
            loading = loading,
            enabled = !loading,
            onClick = {
                showErrors = true
                if (isFormValid) onSubmit()
                else scrollToFirstError()
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}


