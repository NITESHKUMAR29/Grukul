package com.example.feature_class.presentation.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_common.resut.UiState
import com.example.feature_auth.domain.repositories.UserLocalRepository
import com.example.feature_class.domain.models.ClassModel
import com.example.feature_class.domain.useCase.ObserveClassesUseCase
import com.example.feature_class.domain.useCase.SyncClassesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassListViewModel @Inject constructor(
    observeClassesUseCase: ObserveClassesUseCase,
    private val syncClassesUseCase: SyncClassesUseCase,
    private val userLocalRepository: UserLocalRepository
) : ViewModel() {

    enum class StatusFilter {
        ALL, ACTIVE, INACTIVE
    }

    enum class BatchStatusFilter {
        ALL, ONGOING, UPCOMING
    }

    // -------------------- UI Inputs --------------------

    private val searchQuery = MutableStateFlow("")
    val searchText = searchQuery.asStateFlow()

    private val genderFilter = MutableStateFlow("All")
    val selectedGender = genderFilter.asStateFlow()



    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _selectedDay = MutableStateFlow<Int?>(null)
    val selectedDay = _selectedDay.asStateFlow()

    private val statusFilter = MutableStateFlow(StatusFilter.ALL)
    val selectedStatus = statusFilter.asStateFlow()

    private val batchStatusFilter = MutableStateFlow(BatchStatusFilter.ALL)
    val selectedBatchStatus = batchStatusFilter.asStateFlow()


    // -------------------- UI State --------------------

    private val _uiState =
        MutableStateFlow<UiState<List<ClassModel>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    // -------------------- Data pipeline --------------------
    private val createdByFlow = flow {
        emit(userLocalRepository.observeUserId().first()
            ?: throw IllegalStateException("User not logged in"))
    }
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val filteredClassesFlow =
        createdByFlow.flatMapLatest { createdBy ->

            combine(
                observeClassesUseCase(createdBy),
                filterState
            ) { classes, filter ->

                val now = System.currentTimeMillis()

                classes
                    .filter { it.className.contains(filter.query, ignoreCase = true) }
                    .filter { filter.gender == "All" || it.gender == filter.gender }
                    .filter {
                        when (filter.status) {
                            StatusFilter.ALL -> true
                            StatusFilter.ACTIVE -> it.isActive
                            StatusFilter.INACTIVE -> !it.isActive
                        }
                    }
                    .filter {
                        filter.day == null ||
                                it.schedules.any { schedule -> schedule.day == filter.day }
                    }

                    .filter {
                        when (filter.batchStatus) {
                            BatchStatusFilter.ALL -> true
                            BatchStatusFilter.ONGOING ->
                                now in it.startDate..it.endDate
                            BatchStatusFilter.UPCOMING ->
                                now < it.startDate
                        }
                    }
            }
        }

    @OptIn(FlowPreview::class)
    private val filterState = combine(
        searchQuery.debounce(400).distinctUntilChanged(),
        genderFilter,
        statusFilter,
        selectedDay,
        selectedBatchStatus
    ) { query, gender, status, day, batchStatus ->
        ClassFilterState(
            query = query,
            gender = gender,
            status = status,
            day = day,
            batchStatus = batchStatus
        )
    }

    init {
        observeClasses()
        sync()
    }

    private fun observeClasses() {
        viewModelScope.launch {
            filteredClassesFlow.collect { list ->
                _uiState.value =
                    if (list.isEmpty()) UiState.Empty
                    else UiState.Success(list)
            }
        }
    }

    // -------------------- Actions --------------------

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                val createdBy = createdByFlow.first()
                syncClassesUseCase(createdBy)
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    private fun sync() {
        viewModelScope.launch {
            try {
                val createdBy = createdByFlow.first()
                syncClassesUseCase(createdBy)
            } catch (e: Exception) {
                _uiState.value =
                    UiState.Error(e.message ?: "Sync failed")
            }
        }
    }

    fun updateSearch(query: String) {
        searchQuery.value = query
    }

    fun changeStatusFilter(filter: StatusFilter) {
        statusFilter.value = filter
    }

    fun changeBatchStatusFilter(filter: BatchStatusFilter) {
        batchStatusFilter.value = filter
    }

    fun selectGender(gender: String) {
        genderFilter.value = gender
    }

    fun selectDay(day: Int?) {
        Log.d("ViewModel", "selectDay called with: $day")
        _selectedDay.value = day
    }

}

data class ClassFilterState(
    val query: String,
    val gender: String,
    val status: ClassListViewModel.StatusFilter,
    val day: Int?,
    val batchStatus: ClassListViewModel.BatchStatusFilter
)






