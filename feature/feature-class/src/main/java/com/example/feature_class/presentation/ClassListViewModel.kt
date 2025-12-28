package com.example.feature_class.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature_class.domain.useCase.ObserveClassesUseCase
import com.example.feature_class.domain.useCase.SyncClassesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ClassListViewModel @Inject constructor(
    observeClassesUseCase: ObserveClassesUseCase,
    private val syncClassesUseCase: SyncClassesUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _activeOnly = MutableStateFlow(false)
    val activeOnly = _activeOnly.asStateFlow()

    val classes = combine(
        observeClassesUseCase(),
        searchQuery,
        activeOnly
    ) { classes, query, activeOnly ->

        classes
            .filter {
                it.className.contains(query, ignoreCase = true)
            }
            .filter {
                !activeOnly || it.isActive
            }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList()
    )

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        refresh()
    }

    fun updateSearch(query: String) {
        _searchQuery.value = query
    }

    fun toggleActiveOnly(enabled: Boolean) {
        _activeOnly.value = enabled
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            syncClassesUseCase()
            _isRefreshing.value = false
        }
    }
}

