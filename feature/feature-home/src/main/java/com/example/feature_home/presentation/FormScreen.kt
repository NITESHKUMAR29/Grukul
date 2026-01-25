package com.example.feature_home.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.core_ui.ui.theme.GurukulTheme
import com.example.feature_class.presentation.screens.ClassFormScreen

@Composable
fun FormScreenHost(
    classId: String? = null,
    onBack: () -> Unit
) {
    var currentForm by rememberSaveable {
        mutableStateOf(
            if (classId != null) FormType.CLASS else FormType.CLASS
        )
    }

    FormScreen(
        formType = currentForm,
        onFormChange = { currentForm = it },
        onBack = onBack,
        classId = classId
    )
}


@Composable
fun FormScreen(
    formType: FormType,
    onFormChange: (FormType) -> Unit,
    onBack: () -> Unit,
    classId: String?
) {
    Column {
        FormScreenToolbar(
            title =
                if (formType == FormType.CLASS) {
                    if (classId != null) "Edit Class" else "New Class"
                } else {
                    "New Student"
                },
            showBack = true,
            onBackClick = onBack,
            showFormToggle = classId == null, // ❗ disable toggle in edit
            currentForm = formType,
            onFormToggle = onFormChange
        )

        when (formType) {
            FormType.CLASS -> ClassFormScreen(
                classId = classId,
                onBack = onBack
            )

            FormType.STUDENT -> StudentForm()
        }
    }
}



@Composable
fun StudentForm() {

}

@Preview(showBackground = true)
@Composable
fun FormScreenPreview() {
    GurukulTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            FormScreenHost(onBack = {})
        }
    }
}








