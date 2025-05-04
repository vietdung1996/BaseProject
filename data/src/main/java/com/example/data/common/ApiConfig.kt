package com.example.data.common

import android.content.Context
//import com.example.data.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Configuration for API endpoints and settings
 */
@Singleton
class ApiConfig @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        // API Versions
        const val API_VERSION = "v1"
        
        // Environments
        private const val ENV_DEV = "dev"
        private const val ENV_STAGING = "staging"
        private const val ENV_PRODUCTION = "prod"
        
        // Base URLs for different environments
        private const val BASE_URL_DEV = "https://dev-api.example.com/"
        private const val BASE_URL_STAGING = "https://staging-api.example.com/"
        private const val BASE_URL_PRODUCTION = "https://api.example.com/"
        
        // Default timeouts in seconds
        const val DEFAULT_CONNECT_TIMEOUT = 30L
        const val DEFAULT_READ_TIMEOUT = 30L
        const val DEFAULT_WRITE_TIMEOUT = 30L
        
        // Maximum number of retries
        const val MAX_RETRIES = 3
        
        // Cache settings
        const val CACHE_SIZE = 10 * 1024 * 1024L // 10 MB
        const val CACHE_MAX_AGE = 60 * 5 // 5 minutes cache
        const val CACHE_MAX_STALE = 60 * 60 * 24 * 7 // 1 week stale
    }
    
    // Current environment - would normally come from BuildConfig or remote config
//    private val currentEnvironment: String = if (BuildConfig.DEBUG) ENV_DEV else ENV_PRODUCTION
    
    /**
     * Gets the base URL based on the current environment
//     */
//    fun getBaseUrl(): String {
//        return when (currentEnvironment) {
//            ENV_DEV -> BASE_URL_DEV
//            ENV_STAGING -> BASE_URL_STAGING
//            ENV_PRODUCTION -> BASE_URL_PRODUCTION
//            else -> BASE_URL_DEV
//        }
//    }
    
    /**
     * Gets the full URL for a specific API endpoint
     * @param endpoint The API endpoint path
     * @return The complete URL
     */
//    fun getApiUrl(endpoint: String): String {
//        return "${getBaseUrl()}$API_VERSION/$endpoint"
//    }
    
    /**
     * Gets the API key (example method)
     */
    fun getApiKey(): String {
        // In a real app, you might retrieve this from secure storage
        return "your-api-key-here"
    }
    
    /**
     * Gets the common headers that should be sent with every request
     */
    fun getCommonHeaders(): Map<String, String> {
        return mapOf(
            "Accept" to "application/json",
            "Content-Type" to "application/json",
            "x-api-version" to API_VERSION,
            "x-app-version" to getAppVersion(),
            "x-platform" to "android"
        )
    }
    
    /**
     * Gets the application version
     */
    private fun getAppVersion(): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            "${packageInfo.versionName} (${packageInfo.versionCode})"
        } catch (e: Exception) {
            "unknown"
        }
    }
    
    /**
     * Gets the connect timeout in seconds
     */
    fun getConnectTimeout(): Long = DEFAULT_CONNECT_TIMEOUT
    
    /**
     * Gets the read timeout in seconds
     */
    fun getReadTimeout(): Long = DEFAULT_READ_TIMEOUT
    
    /**
     * Gets the write timeout in seconds
     */
    fun getWriteTimeout(): Long = DEFAULT_WRITE_TIMEOUT
    
    /**
     * Determines if API requests should be logged
     */
//    fun shouldLogRequests(): Boolean {
//        return currentEnvironment != ENV_PRODUCTION
//    }
} 