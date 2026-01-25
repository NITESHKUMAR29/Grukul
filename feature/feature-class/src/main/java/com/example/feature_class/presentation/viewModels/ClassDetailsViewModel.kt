package com.example.feature_class.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_common.resut.UiState
import com.example.feature_class.domain.models.ClassModel
import com.example.feature_class.domain.useCase.GetClassByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassDetailsViewModel @Inject constructor(
    private val getClassByIdUseCase: GetClassByIdUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiState<ClassModel>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun loadClass(classId: String) {
        viewModelScope.launch {
            try {
                val classModel = getClassByIdUseCase(classId)
                _uiState.value = UiState.Success(classModel)
            } catch (e: Exception) {
                _uiState.value =
                    UiState.Error(e.message ?: "Failed to load class")
            }
        }
    }
}
