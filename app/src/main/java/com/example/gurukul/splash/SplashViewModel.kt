package com.example.gurukul.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_ui.components.BottomNavItem
import com.example.feature_auth.presentation.SelectRoleScreen
import com.example.gurukul.navigation.AuthRoutes
import com.example.gurukul.navigation.RootRoutes.SPLASH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    private val _startDestination = MutableStateFlow(SPLASH)
    val startDestination = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1800)
            _startDestination.value = AuthRoutes.SELECT_ROLE
        }
    }
}