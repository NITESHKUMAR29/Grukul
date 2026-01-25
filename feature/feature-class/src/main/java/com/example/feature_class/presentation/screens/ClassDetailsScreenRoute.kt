package com.example.feature_class.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.core_common.resut.UiState
import com.example.core_ui.components.state.GurukulErrorState
import com.example.core_ui.components.state.GurukulLoadingState
import com.example.feature_class.domain.models.ClassModel
import com.example.feature_class.presentation.viewModels.ClassDetailsViewModel

@Composable
fun ClassDetailsScreenRoute(
    classId: String,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onAddStudent: () -> Unit,
    viewModel: ClassDetailsViewModel = hiltViewModel()
) {
    LaunchedEffect(classId) {
        viewModel.loadClass(classId)
    }

    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is UiState.Loading -> GurukulLoadingState()

        is UiState.Success -> {
            ClassDetailsScreen(
                classModel = (uiState as UiState.Success<ClassModel>).data,
                onBack = onBack,
                onEdit = onEdit,
                onAddStudent = onAddStudent
            )
        }

        is UiState.Error -> GurukulErrorState(
            message = (uiState as UiState.Error).message,
            onRetry = { viewModel.loadClass(classId) }
        )

        else -> {}
    }
}
