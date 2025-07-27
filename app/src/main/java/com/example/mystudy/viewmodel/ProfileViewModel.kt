package com.example.mystudy.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _loggedInName = MutableStateFlow("")
    val loggedInName: StateFlow<String> = _loggedInName

    fun login(name: String) {
        _loggedInName.value = name
        _isLoggedIn.value = true
    }

    fun logout() {
        _loggedInName.value = ""
        _isLoggedIn.value = false
    }
}
