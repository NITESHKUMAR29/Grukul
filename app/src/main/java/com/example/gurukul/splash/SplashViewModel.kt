package com.example.gurukul.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_ui.components.navigationBar.BottomNavItem
import com.example.feature_auth.domain.useCase.CheckAuthStatusUseCase
import com.example.feature_auth.presentation.SelectRoleScreen
import com.example.gurukul.navigation.AuthRoutes
import com.example.gurukul.navigation.RootRoutes
import com.example.gurukul.navigation.RootRoutes.SPLASH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkAuthStatusUseCase: CheckAuthStatusUseCase
) : ViewModel() {

    private val _startDestination = MutableStateFlow(RootRoutes.SPLASH)
    val startDestination = _startDestination.asStateFlow()

    init {
        decideStartDestination()
    }

    private fun decideStartDestination() {
        Log.d("ENterHere",checkAuthStatusUseCase.toString())
        _startDestination.value =
            if (checkAuthStatusUseCase()) {
                RootRoutes.MAIN_GRAPH
            } else {
                RootRoutes.AUTH_GRAPH
            }
    }
}