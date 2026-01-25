package com.example.feature_class.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.core_common.resut.UiState
import com.example.core_model.models.ClassSchedule
import com.example.core_ui.components.buttons.BottomActionCard
import com.example.core_ui.components.datePicker.GurukulDateField
import com.example.core_ui.components.datePicker.GurukulTimeField
import com.example.core_ui.components.spinners.CommonSpinner
import com.example.core_ui.components.state.GurukulErrorState
import com.example.core_ui.components.state.GurukulLoadingState
import com.example.core_ui.ui.textFields.GurukulTextField
import com.example.feature_class.domain.models.ClassModel
import com.example.feature_class.presentation.viewModels.ClassViewModel

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

    var schedules by remember {
        mutableStateOf<Map<Int, Pair<Long?, Long?>>>(emptyMap())
    }

    var startDate by remember { mutableStateOf<Long?>(null) }
    var endDate by remember { mutableStateOf<Long?>(null) }
    var loading by remember { mutableStateOf(false) }

    // Load class for edit
    LaunchedEffect(classId) {
        if (isEditMode) {
            viewModel.loadClass(classId)
        }
    }

    // Prefill data
    LaunchedEffect(classState) {
        if (classState is UiState.Success && isEditMode) {
            val data = (classState as UiState.Success<ClassModel>).data

            className = data.className
            teacherName = data.teacherName
            address = data.address
            aboutClass = data.description
            selectedActive = if (data.isActive) "Active" else "Inactive"
            selectedGender = data.gender
            startDate = data.startDate
            endDate = data.endDate

            schedules = data.schedules.associate {
                it.day to (it.startTime to it.endTime)
            }
        }
    }

    // Handle submit state
    LaunchedEffect(createState) {
        Log.d("ClassFormScreen", "createState: $createState")
        loading = createState is UiState.Loading
        if (createState is UiState.Success) {
            viewModel.resetCreateState()
            onBack()
        }
    }

    Box(Modifier.fillMaxSize()) {

        if (isEditMode && classState is UiState.Loading) {
            GurukulLoadingState()
            return@Box
        }

        if (isEditMode && classState is UiState.Error) {
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
            schedules = schedules,
            startDate = startDate,
            endDate = endDate,

            onClassNameChange = { className = it },
            onTeacherNameChange = { teacherName = it },
            onAddressChange = { address = it },
            onAboutChange = { aboutClass = it },
            onActiveChange = { selectedActive = it },
            onGenderChange = { selectedGender = it },

            onDayToggle = { day ->
                schedules =
                    if (schedules.containsKey(day)) {
                        schedules - day
                    } else {
                        schedules + (day to (null to null))
                    }
            },


            onTimeChange = { day, start, end ->
                val current = schedules[day] ?: (null to null)

                schedules = schedules + (
                        day to Pair(
                            start ?: current.first,
                            end ?: current.second
                        )
                        )
            },


                    onStartDateChange = { startDate = it },
            onEndDateChange = { endDate = it },

            loading = loading,

            onSubmit = {
                val finalSchedules = schedules.mapNotNull { (day, times) ->
                    val (start, end) = times
                    if (start != null && end != null && end > start) {
                        ClassSchedule(day, start, end)
                    } else null
                }

                if (isEditMode) {
                    viewModel.updateClass(
                        classId = classId!!,
                        className = className,
                        teacherName = teacherName,
                        isActive = selectedActive == "Active",
                        gender = selectedGender,
                        address = address,
                        about = aboutClass,
                        schedules = finalSchedules,
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
                        schedules = finalSchedules,
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
    schedules: Map<Int, Pair<Long?, Long?>>,

    startDate: Long?,
    endDate: Long?,

    onClassNameChange: (String) -> Unit,
    onTeacherNameChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onAboutChange: (String) -> Unit,
    onActiveChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
    onDayToggle: (Int) -> Unit,
    onTimeChange: (Int, Long?, Long?) -> Unit,
    onStartDateChange: (Long) -> Unit,
    onEndDateChange: (Long) -> Unit,

    onSubmit: () -> Unit,
    loading: Boolean
) {
    val scrollState = rememberScrollState()
    var showErrors by remember { mutableStateOf(false) }

    val isFormValid =
        className.isNotBlank() &&
                teacherName.isNotBlank() &&
                schedules.isNotEmpty() &&
                schedules.values.all {
                    it.first != null &&
                            it.second != null &&
                            it.second!! > it.first!!
                } &&
                startDate != null &&
                endDate != null &&
                endDate >= startDate

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

            // -------- BASIC INFO --------

            GurukulTextField(
                value = className,
                onValueChange = onClassNameChange,
                hint = "Class Name *",
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            GurukulTextField(
                value = teacherName,
                onValueChange = onTeacherNameChange,
                hint = "Teacher Name *",
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(12.dp))

            CommonSpinner(
                label = "Status",
                items = listOf("Active", "Inactive"),
                selectedItem = selectedActive,
                itemLabel = { it },
                onItemSelected = onActiveChange,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(12.dp))

            CommonSpinner(
                label = "Gender",
                items = listOf("Boys", "Girls"),
                selectedItem = selectedGender,
                itemLabel = { it },
                onItemSelected = onGenderChange,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(12.dp))

            // -------- DESCRIPTION --------

            GurukulTextField(
                value = aboutClass,
                onValueChange = onAboutChange,
                hint = "Description",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .height(100.dp)
            )

            Spacer(Modifier.height(12.dp))

            GurukulTextField(
                value = address,
                onValueChange = onAddressChange,
                hint = "Address",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .height(100.dp)
            )

            Spacer(Modifier.height(16.dp))

            // -------- DATE RANGE --------

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GurukulDateField(
                    label = "Start Date",
                    dateMillis = startDate,
                    onDateSelected = onStartDateChange,
                    modifier = Modifier.weight(1f),
                    isError = showErrors && startDate == null
                )

                GurukulDateField(
                    label = "End Date",
                    dateMillis = endDate,
                    minDateMillis = startDate,
                    onDateSelected = onEndDateChange,
                    modifier = Modifier.weight(1f),
                    isError = showErrors && (
                            endDate == null ||
                                    (startDate != null && endDate < startDate)
                            )
                )
            }

            Spacer(Modifier.height(16.dp))



            Text(
                "Weekly Schedule *",
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(8.dp))

            FlowRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listOf(
                    1 to "Mon", 2 to "Tue", 3 to "Wed",
                    4 to "Thu", 5 to "Fri", 6 to "Sat", 7 to "Sun"
                ).forEach { (day, label) ->

                    val times = schedules[day]
                    val fullySelected =
                        times?.first != null && times.second != null

                    Column {

                        // Day chip
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    if (fullySelected)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.surfaceVariant
                                )
                                .clickable { onDayToggle(day) }
                                .padding(horizontal = 20.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = label,
                                color = if (fullySelected)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Time pickers
                        if (times != null) {
                            Spacer(Modifier.height(6.dp))

                            GurukulTimeField(
                                label = "Start Time",
                                timeMillis = times.first,
                                onTimeSelected = {
                                    onTimeChange(day, it, times.second)
                                },
                                isError = showErrors && times.first == null
                            )

                            Spacer(Modifier.height(6.dp))

                            GurukulTimeField(
                                label = "End Time",
                                timeMillis = times.second,
                                onTimeSelected = {
                                    onTimeChange(day, times.first, it)
                                },
                                isError = showErrors && (
                                        times.second == null ||
                                                (times.first != null && times.second!! <= times.first!!)
                                        )
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }

        BottomActionCard(
            buttonText = if (isEditMode) "Update Class" else "Create Class",
            onClick = {
                showErrors = true
                if (isFormValid) onSubmit()
            },
            modifier = Modifier.align(Alignment.BottomCenter),
            loading = loading,
            enabled = !loading
        )
    }
}


