package com.ferhatozcelik.jetpackcomposetemplate.util

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(@ApplicationContext context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("ppl_prefs", Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        prefs.edit().putString("auth_token", token).apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString("auth_token", null)
    }

    fun saveUserDetails(userId: String, name: String, role: String) {
        prefs.edit()
            .putString("user_id", userId)
            .putString("user_name", name)
            .putString("user_role", role)
            .apply()
    }

    fun getUserId(): String? = prefs.getString("user_id", null)
    fun getUserName(): String? = prefs.getString("user_name", null)
    fun getUserRole(): String? = prefs.getString("user_role", null)

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
