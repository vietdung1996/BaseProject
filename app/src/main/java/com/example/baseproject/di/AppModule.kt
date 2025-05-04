package com.example.baseproject.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module Hilt cung cấp các dependency chung cho toàn app
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    /**
     * Cung cấp Context cho các dependency cần sử dụng
     */
    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }
    
    // Thêm các dependency khác dùng chung cho cả project ở đây
    // Ví dụ: SharedPreferences, Resource Provider, Dispatchers, etc.
    
} 