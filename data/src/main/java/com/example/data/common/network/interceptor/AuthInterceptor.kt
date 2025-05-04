package com.example.data.common.network.interceptor

import com.example.data.common.auth.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interceptor that adds authentication headers to requests
 */
@Singleton
class AuthInterceptor @Inject constructor(private val tokenManager: TokenManager) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = if (tokenManager.hasToken()) {
            val token = tokenManager.getToken()
            request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build()
        } else {
            request.newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build()
        }
        return chain.proceed(newRequest)
    }
} 