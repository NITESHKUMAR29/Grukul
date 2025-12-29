package com.example.feature_class.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_common.resut.UiState
import com.example.feature_class.domain.models.ClassModel
import com.example.feature_class.domain.useCase.ObserveClassesUseCase
import com.example.feature_class.domain.useCase.SyncClassesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassListViewModel @Inject constructor(
    observeClassesUseCase: ObserveClassesUseCase,
    private val syncClassesUseCase: SyncClassesUseCase
) : ViewModel() {

    enum class StatusFilter {
        ALL, ACTIVE, INACTIVE
    }

    // -------------------- UI Inputs --------------------

    private val searchQuery = MutableStateFlow("")
    val searchText = searchQuery.asStateFlow()

    private val genderFilter = MutableStateFlow("ALL")
    val selectedGender = genderFilter.asStateFlow()

    private val statusFilter = MutableStateFlow(StatusFilter.ALL)
    val selectedStatus = statusFilter.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    // -------------------- UI State --------------------

    private val _uiState =
        MutableStateFlow<UiState<List<ClassModel>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    // -------------------- Data pipeline --------------------

    @OptIn(FlowPreview::class)
    private val filteredClassesFlow = combine(
        observeClassesUseCase(),
        searchQuery.debounce(400).distinctUntilChanged(),
        genderFilter,
        statusFilter
    ) { classes, query, gender, status ->

        classes
            .filter { it.className.contains(query, ignoreCase = true) }
            .filter { gender == "ALL" || it.gender == gender }
            .filter {
                when (status) {
                    StatusFilter.ALL -> true
                    StatusFilter.ACTIVE -> it.isActive
                    StatusFilter.INACTIVE -> !it.isActive
                }
            }
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
                syncClassesUseCase()
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    private fun sync() {
        viewModelScope.launch {
            try {
                syncClassesUseCase()
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

    fun selectGender(gender: String) {
        genderFilter.value = gender
    }
}






