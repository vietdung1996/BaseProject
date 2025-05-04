package com.example.data.common.auth

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager class to handle authentication tokens
 */
@Singleton
class TokenManager @Inject constructor(@ApplicationContext context: Context) {
    
    companion object {
        private const val PREF_NAME = "auth_prefs"
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_TOKEN_EXPIRY = "token_expiry"
    }
    
    private val prefs: SharedPreferences = 
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    
    /**
     * Save authentication token
     * @param token JWT token
     * @param refreshToken Refresh token
     * @param expiryTimeInMillis Token expiry time in milliseconds
     */
    fun saveToken(token: String, refreshToken: String, expiryTimeInMillis: Long) {
        prefs.edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_REFRESH_TOKEN, refreshToken)
            .putLong(KEY_TOKEN_EXPIRY, expiryTimeInMillis)
            .apply()
    }
    
    /**
     * Get the stored JWT token
     * @return The token string or null if not found
     */
    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }
    
    /**
     * Get the stored refresh token
     * @return The refresh token string or null if not found
     */
    fun getRefreshToken(): String? {
        return prefs.getString(KEY_REFRESH_TOKEN, null)
    }
    
    /**
     * Check if we have a valid token
     * @return true if token exists and is not expired
     */
    fun hasToken(): Boolean {
        val token = getToken()
        val expiryTime = prefs.getLong(KEY_TOKEN_EXPIRY, 0)
        
        return token != null && expiryTime > System.currentTimeMillis()
    }
    
    /**
     * Check if token is expired
     * @return true if token exists but is expired
     */
    fun isTokenExpired(): Boolean {
        val token = getToken()
        val expiryTime = prefs.getLong(KEY_TOKEN_EXPIRY, 0)
        
        return token != null && expiryTime <= System.currentTimeMillis()
    }
    
    /**
     * Clear all authentication data
     */
    fun clearTokens() {
        prefs.edit()
            .remove(KEY_TOKEN)
            .remove(KEY_REFRESH_TOKEN)
            .remove(KEY_TOKEN_EXPIRY)
            .apply()
    }
} 