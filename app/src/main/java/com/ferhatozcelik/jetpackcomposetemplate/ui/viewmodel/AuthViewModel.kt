package com.ferhatozcelik.jetpackcomposetemplate.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferhatozcelik.jetpackcomposetemplate.data.remote.api.PplApiService
import com.ferhatozcelik.jetpackcomposetemplate.data.remote.model.LoginRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val apiService: PplApiService
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(username: String, password: String) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            try {
                val response = apiService.login(LoginRequest(username, password))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        // In a real app, save token to DataStore here
                        _loginState.value = LoginState.Success(body.userId)
                    } else {
                        _loginState.value = LoginState.Error("Empty response body")
                    }
                } else {
                    _loginState.value = LoginState.Error("Login failed: ${response.code()}")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Network error: ${e.message}")
            }
        }
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val userId: String) : LoginState()
    data class Error(val message: String) : LoginState()
}
