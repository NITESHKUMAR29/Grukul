package com.example.feature_class.presentation

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.core_common.resut.UiState
import com.example.core_ui.components.buttons.GurukulPrimaryButton
import com.example.core_ui.components.spinners.CommonSpinner
import com.example.core_ui.ui.textFields.GurukulTextField



@Composable
fun ClassFormScreen(
    viewModel: ClassViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val createState = viewModel.createClassState.collectAsState()

    // UI state
    var className by remember { mutableStateOf("") }
    var teacherName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var selectedActive by remember { mutableStateOf("Active") }
    var selectedGender by remember { mutableStateOf("Boys") }
    var loaded by remember { mutableStateOf(false) }
    Log.d("createState", "state: $createState")

    // Handle side effects (optional but recommended)
    when (val state = createState.value) {

        is UiState.Loading -> {
            // show loader if you want
            loaded=true
        }

        is UiState.Success -> {
            // reset form / navigate back / show toast
            // Example:
             LaunchedEffect(Unit) { onBack() }
            loaded=false
        }

        is UiState.Error -> {
            // show error snackbar/toast
            Log.e("ClassForm", state.message)
            loaded=false
        }

        UiState.Idle -> Unit
    }

    ClassFormContent(
        className = className,
        teacherName = teacherName,
        address = address,
        selectedActive = selectedActive,
        selectedGender = selectedGender,
        onClassNameChange = { className = it },
        onTeacherNameChange = { teacherName = it },
        onAddressChange = { address = it },
        onActiveChange = { selectedActive = it },
        onGenderChange = { selectedGender = it },
        loading = loaded,
        onSubmit = {
            viewModel.createClass(
                className = className,
                teacherName = teacherName,
                isActive = selectedActive == "Active",
                gender = selectedGender,
                address = address
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
    onClassNameChange: (String) -> Unit,
    onTeacherNameChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onActiveChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
    onSubmit: () -> Unit,
    loading: Boolean = false
) {
    val active = listOf("Active", "Inactive")
    val gender = listOf("Boys", "Girls")

    Column {
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
            hint = "Class Name",
            keyboardType = KeyboardType.Text,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        GurukulTextField(
            value = teacherName,
            onValueChange = onTeacherNameChange,
            hint = "Teacher Name",
            keyboardType = KeyboardType.Text,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(8.dp))

        CommonSpinner(
            label = "Status",
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
            label = "Gender",
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
            hint = "Full Address",
            keyboardType = KeyboardType.Text,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(100.dp)
        )

        Spacer(Modifier.height(16.dp))

        GurukulPrimaryButton(
            text = "Submit",
            onClick = onSubmit,
            loading = loading

        )

        Spacer(Modifier.height(16.dp))
    }
}


