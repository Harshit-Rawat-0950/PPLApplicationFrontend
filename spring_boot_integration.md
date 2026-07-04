# Spring Boot Integration Guide

This guide outlines how to connect the PPL Operations Portal frontend (built with Jetpack Compose) to a Spring Boot backend.

## 1. Add Dependencies
First, add Retrofit, OkHttp, and serialization dependencies to your `app/build.gradle.kts`:

```kotlin
dependencies {
    // Retrofit & OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}
```

## 2. Define Data Models
Create Kotlin data classes that match the JSON structure returned by your Spring Boot `@RestController`s. 

**Example (Login Request/Response):**
```kotlin
package com.ferhatozcelik.jetpackcomposetemplate.data.remote.model

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val userId: String,
    val role: String
)
```

## 3. Create the API Interface
Define the API endpoints using Retrofit annotations.

```kotlin
package com.ferhatozcelik.jetpackcomposetemplate.data.remote.api

import com.ferhatozcelik.jetpackcomposetemplate.data.remote.model.LoginRequest
import com.ferhatozcelik.jetpackcomposetemplate.data.remote.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}
```

## 4. Provide the Retrofit Instance via Hilt
Update your Hilt Network Module to provide Retrofit and the API interfaces to your ViewModels.

```kotlin
package com.ferhatozcelik.jetpackcomposetemplate.di

import com.ferhatozcelik.jetpackcomposetemplate.data.remote.api.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl("http://YOUR_SPRING_BOOT_IP:8080") // Replace with actual backend IP
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }
}
```

## 5. Consume API in ViewModel
Inject the `AuthApi` into your ViewModel and make network requests using Coroutines.

```kotlin
package com.ferhatozcelik.jetpackcomposetemplate.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferhatozcelik.jetpackcomposetemplate.data.remote.api.AuthApi
import com.ferhatozcelik.jetpackcomposetemplate.data.remote.model.LoginRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authApi: AuthApi
) : ViewModel() {

    fun attemptLogin(username: String, password: String) {
        viewModelScope.launch {
            try {
                val request = LoginRequest(username, password)
                val response = authApi.login(request)
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    // Save token (e.g., DataStore/SharedPreferences)
                    // Trigger navigation state update
                } else {
                    // Handle error (e.g., incorrect credentials)
                }
            } catch (e: Exception) {
                // Handle network failure
            }
        }
    }
}
```

## 6. Update Compose UI
In `LoginScreen.kt`, instantiate the ViewModel and call `attemptLogin` when the Login button is pressed, observing UI state to trigger the actual navigation event upon success.
