package com.example.baseproject.di

import com.example.data.di.DataSourceModule
import com.example.data.di.RepositoryModule
import com.example.data.common.di.NetworkModule
import com.example.data.common.di.DataModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * DI abstraction module that includes all necessary data layer modules
 * This allows the app layer to access data implementations without direct dependencies
 */
@Module(
    includes = [
        RepositoryModule::class,
        DataSourceModule::class,
        NetworkModule::class,
        DataModule::class
    ]
)
@InstallIn(SingletonComponent::class)
abstract class DataLayerModule