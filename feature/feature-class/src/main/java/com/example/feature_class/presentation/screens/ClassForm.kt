package com.example.feature_class.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.core_common.resut.UiState
import com.example.core_ui.components.buttons.BottomActionCard
import com.example.core_ui.components.datePicker.GurukulDateField
import com.example.core_ui.components.feedbackText.ErrorText
import com.example.core_ui.components.spinners.CommonSpinner
import com.example.core_ui.components.state.GurukulErrorState
import com.example.core_ui.components.state.GurukulLoadingState
import com.example.core_ui.ui.textFields.GurukulTextField
import com.example.feature_class.domain.models.ClassModel
import com.example.feature_class.presentation.viewModels.ClassViewModel
import kotlinx.coroutines.launch

@Composable
fun ClassFormScreen(
    classId: String?,
    viewModel: ClassViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val isEditMode = classId != null

    val createState by viewModel.createClassState.collectAsState()
    val classState by viewModel.getClassState.collectAsState()

    var className by remember { mutableStateOf("") }
    var teacherName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var aboutClass by remember { mutableStateOf("") }
    var selectedActive by remember { mutableStateOf("Active") }
    var selectedGender by remember { mutableStateOf("Boys") }
    var selectedDays by remember { mutableStateOf(setOf<Int>()) }
    var startDate by remember { mutableStateOf<Long?>(null) }
    var endDate by remember { mutableStateOf<Long?>(null) }
    var loading by remember { mutableStateOf(false) }


    LaunchedEffect(classId) {
        if (isEditMode) {
            viewModel.loadClass(classId!!)
        }
    }

    LaunchedEffect(classState) {
        if (classState is UiState.Success && isEditMode) {
            val data = (classState as UiState.Success<ClassModel>).data
            className = data.className
            teacherName = data.teacherName
            address = data.address
            aboutClass = data.description
            selectedActive = if (data.isActive) "Active" else "Inactive"
            selectedGender = data.gender
            selectedDays = data.days.toSet()
            startDate = data.startDate
            endDate = data.endDate
        }
    }



    LaunchedEffect(createState) {
        loading = createState is UiState.Loading
        if (createState is UiState.Success) {
            onBack()
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {


        if (isEditMode && classState is UiState.Loading) {
            GurukulLoadingState()
            return@Box
        }

        if (classState is UiState.Error && isEditMode) {
            GurukulErrorState(
                message = (classState as UiState.Error).message
            )
            return@Box
        }

        ClassFormContent(
            isEditMode = isEditMode,

            className = className,
            teacherName = teacherName,
            address = address,
            aboutClass = aboutClass,
            selectedActive = selectedActive,
            selectedGender = selectedGender,
            selectedDays = selectedDays,
            startDate = startDate,
            endDate = endDate,

            onClassNameChange = { className = it },
            onTeacherNameChange = { teacherName = it },
            onAddressChange = { address = it },
            onAboutChange = { aboutClass = it },
            onActiveChange = { selectedActive = it },
            onGenderChange = { selectedGender = it },
            onDayToggle = { day ->
                selectedDays =
                    if (selectedDays.contains(day)) selectedDays - day
                    else selectedDays + day
            },
            onStartDateChange = { startDate = it },
            onEndDateChange = { endDate = it },

            loading = loading,

            onSubmit = {
                if (isEditMode) {
                    viewModel.updateClass(
                        classId = classId!!,
                        className = className,
                        teacherName = teacherName,
                        isActive = selectedActive == "Active",
                        gender = selectedGender,
                        address = address,
                        about = aboutClass,
                        days = selectedDays.toList(),
                        startDate = startDate!!,
                        endDate = endDate!!
                    )
                } else {
                    viewModel.createClass(
                        className = className,
                        teacherName = teacherName,
                        isActive = selectedActive == "Active",
                        gender = selectedGender,
                        address = address,
                        about = aboutClass,
                        days = selectedDays.toList(),
                        startDate = startDate!!,
                        endDate = endDate!!
                    )
                }
            }
        )
    }
}



@Composable
private fun ClassFormContent(
    isEditMode: Boolean,

    className: String,
    teacherName: String,
    address: String,
    aboutClass: String,
    selectedActive: String,
    selectedGender: String,
    selectedDays: Set<Int>,

    startDate: Long?,
    endDate: Long?,

    onClassNameChange: (String) -> Unit,
    onTeacherNameChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onAboutChange: (String) -> Unit,
    onActiveChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
    onDayToggle: (Int) -> Unit,
    onStartDateChange: (Long) -> Unit,
    onEndDateChange: (Long) -> Unit,

    onSubmit: () -> Unit,
    loading: Boolean
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    var showErrors by remember { mutableStateOf(false) }

    var classNameY by remember { mutableStateOf(0f) }
    var teacherNameY by remember { mutableStateOf(0f) }
    var daysY by remember { mutableStateOf(0f) }

    val isFormValid =
        className.isNotBlank() &&
                teacherName.isNotBlank() &&
                selectedDays.isNotEmpty() &&
                startDate != null &&
                endDate != null &&
                endDate >= startDate

    fun scrollToFirstError() {
        scope.launch {
            when {
                className.isBlank() -> scrollState.animateScrollTo(classNameY.toInt())
                teacherName.isBlank() -> scrollState.animateScrollTo(teacherNameY.toInt())
                selectedDays.isEmpty() -> scrollState.animateScrollTo(daysY.toInt())
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(bottom = 88.dp)
        ) {

            Spacer(Modifier.height(16.dp))

            Text(
                text = if (isEditMode) "Edit Class" else "New Class",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(12.dp))

            GurukulTextField(
                value = className,
                onValueChange = onClassNameChange,
                hint = "Class Name *",
                isError = showErrors && className.isBlank(),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .onGloballyPositioned { classNameY = it.positionInParent().y }
            )

            if (showErrors && className.isBlank()) ErrorText("Class name is required")

            GurukulTextField(
                value = teacherName,
                onValueChange = onTeacherNameChange,
                hint = "Teacher Name *",
                isError = showErrors && teacherName.isBlank(),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .onGloballyPositioned { teacherNameY = it.positionInParent().y }
            )

            if (showErrors && teacherName.isBlank()) ErrorText("Teacher name is required")

            Spacer(Modifier.height(12.dp))

            CommonSpinner(
                label = "Status *",
                items = listOf("Active", "Inactive"),
                selectedItem = selectedActive,
                itemLabel = { it },
                onItemSelected = onActiveChange,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(12.dp))

            CommonSpinner(
                label = "Gender *",
                items = listOf("Boys", "Girls"),
                selectedItem = selectedGender,
                itemLabel = { it },
                onItemSelected = onGenderChange,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(12.dp))

            GurukulTextField(
                value = aboutClass,
                onValueChange = onAboutChange,
                hint = "Full Description (Optional)",
                keyboardType = KeyboardType.Text,
                modifier = Modifier.padding(horizontal = 16.dp).height(100.dp)
            )

            Spacer(Modifier.height(12.dp))

            GurukulTextField(
                value = address,
                onValueChange = onAddressChange,
                hint = "Full Address (Optional)",
                keyboardType = KeyboardType.Text,
                modifier = Modifier.padding(horizontal = 16.dp).height(100.dp)
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GurukulDateField(
                    label = "Start Date",
                    dateMillis = startDate,
                    onDateSelected = onStartDateChange,
                    modifier = Modifier.weight(1f)
                )
                GurukulDateField(
                    label = "End Date",
                    dateMillis = endDate,
                    minDateMillis = startDate,
                    onDateSelected = onEndDateChange,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                "Class Days *",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .onGloballyPositioned { daysY = it.positionInParent().y }
            )

            Spacer(Modifier.height(8.dp))

            FlowRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf(
                    1 to "Mon", 2 to "Tue", 3 to "Wed",
                    4 to "Thu", 5 to "Fri", 6 to "Sat", 7 to "Sun"
                ).forEach { (day, label) ->
                    val selected = selectedDays.contains(day)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (selected)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                            .clickable { onDayToggle(day) }
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = label,
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

        BottomActionCard(
            buttonText = if (isEditMode) "Update Class" else "Submit",
            onClick = {
                showErrors = true
                if (isFormValid) onSubmit()
                else scrollToFirstError()
            },
            modifier = Modifier.align(Alignment.BottomCenter),
            loading = loading,
            enabled = !loading
        )
    }
}
