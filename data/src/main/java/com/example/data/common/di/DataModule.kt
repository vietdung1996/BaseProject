package com.example.data.common.di

import android.content.Context
import androidx.room.Room
import com.example.data.local.user.UserLocalDataSource
import com.example.data.remote.user.UserRemoteDataSource
import com.example.data.common.database.AppDatabase
import com.example.data.mapper.UserMapper
import com.example.data.local.user.UserDao
import com.example.data.remote.user.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase) = database.userDao()

    @Provides
    @Singleton
    fun provideUserMapper() = UserMapper()

    @Provides
    @Singleton
    fun provideUserLocalDataSource(userDao: UserDao) =
        UserLocalDataSource(userDao)

    @Provides
    @Singleton
    fun provideUserRemoteDataSource(userApi: UserApi) =
        UserRemoteDataSource(userApi)
} 