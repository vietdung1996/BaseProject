package com.example.data.common.di

import com.example.data.local.user.UserLocalDataSource
import com.example.data.remote.user.UserRemoteDataSource
import com.example.data.mapper.UserMapper
import com.example.data.repository.user.UserRepositoryImpl
import com.example.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        remoteDataSource: UserRemoteDataSource,
        localDataSource: UserLocalDataSource,
        mapper: UserMapper
    ): UserRepository {
        return UserRepositoryImpl(remoteDataSource, localDataSource, mapper)
    }
} 