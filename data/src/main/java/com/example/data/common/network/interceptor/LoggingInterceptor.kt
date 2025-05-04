package com.example.data.common.network.interceptor

import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interceptor for logging network requests and responses
 */
@Singleton
class LoggingInterceptor @Inject constructor() {
    
    fun create(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
} 