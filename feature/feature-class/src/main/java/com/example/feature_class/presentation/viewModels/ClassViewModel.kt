package com.example.feature_class.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_common.resut.UiState
import com.example.feature_auth.domain.repositories.UserLocalRepository
import com.example.feature_class.domain.models.ClassModel
import com.example.feature_class.domain.repository.ClassRepository
import com.example.feature_class.domain.useCase.CreateClassUseCase
import com.example.feature_class.domain.useCase.GetClassByIdUseCase
import com.example.feature_class.domain.useCase.SyncClassesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ClassViewModel @Inject constructor(
    private val createClassUseCase: CreateClassUseCase,
    private val userLocalRepository: UserLocalRepository,
    private val syncClassesUseCase: SyncClassesUseCase,
    private val getClassByIdUseCase: GetClassByIdUseCase,
    private val classRepository: ClassRepository
) : ViewModel() {

    private val _getClassDetailsState =
        MutableStateFlow<UiState<ClassModel>>(UiState.Loading)
    val getClassState = _getClassDetailsState.asStateFlow()

    private val _createClassState =
        MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val createClassState = _createClassState.asStateFlow()

    fun createClass(
        className: String,
        teacherName: String,
        isActive: Boolean,
        gender: String,
        address: String,
        about: String,
        days: List<Int>,
        startDate: Long,
        endDate: Long
    ) {
        viewModelScope.launch {
            try {
                _createClassState.value = UiState.Loading

                val userId = userLocalRepository.observeUserId().first()
                    ?: throw IllegalStateException("User not logged in")

                val classModel = ClassModel(
                    id = UUID.randomUUID().toString(),
                    className = className,
                    teacherName = teacherName,
                    isActive = isActive,
                    gender = gender,
                    address = address,
                    description = about,
                    createdBy = userId,
                    days = days,
                    startDate = startDate,
                    endDate = endDate
                )
                Log.d("ClassViewModelClassData", "createClass: $classModel")

                createClassUseCase(classModel)
                syncClassesUseCase(userId)

                _createClassState.value = UiState.Success(Unit)
            } catch (e: Exception) {
                _createClassState.value =
                    UiState.Error(e.message ?: "Create failed")
            }
        }
    }

    fun updateClass(
        classId: String,
        className: String,
        teacherName: String,
        isActive: Boolean,
        gender: String,
        address: String,
        about: String,
        days: List<Int>,
        startDate: Long,
        endDate: Long
    ) {
        viewModelScope.launch {
            _createClassState.value = UiState.Loading

            try {

                val existing = classRepository.getClassById(classId)

                val updatedClass = existing.copy(
                    className = className,
                    teacherName = teacherName,
                    isActive = isActive,
                    gender = gender,
                    address = address,
                    description = about,
                    days = days,
                    startDate = startDate,
                    endDate = endDate,
                    updatedAt = System.currentTimeMillis()
                )

                classRepository.updateClass(updatedClass)
                classRepository.syncClasses(existing.createdBy)

                _createClassState.value = UiState.Success(Unit)
            } catch (e: Exception) {
                _createClassState.value =
                    UiState.Error(e.message ?: "Failed to update class")
            }
        }
    }


    fun loadClass(classId: String) {
        viewModelScope.launch {
            try {
                val classModel = getClassByIdUseCase(classId)
                _getClassDetailsState.value = UiState.Success(classModel)
            } catch (e: Exception) {
                _getClassDetailsState.value =
                    UiState.Error(e.message ?: "Failed to load class")
            }
        }
    }
}