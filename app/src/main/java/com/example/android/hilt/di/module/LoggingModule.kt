package com.example.android.hilt.di.module

import com.example.android.hilt.data.LoggerDataSource
import com.example.android.hilt.data.LoggerInMemoryDataSource
import com.example.android.hilt.data.LoggerLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Hilt doesn't know which implementation to use when LoggerDataSource interface is requested
 * (can be either LoggerInMemoryDataSource or LoggerLocalDataSource).
 *
 * Since the different implementations of LoggerDataSource are
 * scoped to different containers, we cannot use the same module.
 * We have to define bindings for both modules and create Qualifiers
 * annotations, so Hilt knows which binding to use:
 */

// @Qualifiers for each binding module:

@Qualifier
annotation class DatabaseLogger
@Qualifier
annotation class InMemoryLogger

// Modules for different implementations ot the interface

@InstallIn(ApplicationComponent::class)
@Module
abstract class LoggingDatabaseModule {
    @DatabaseLogger
    @Singleton
    @Binds
    abstract fun bindDatabaseLogger(impl: LoggerLocalDataSource): LoggerDataSource
}


@InstallIn(ActivityComponent::class)
@Module
abstract class LoggingInMemoryModule {
    @InMemoryLogger
    @ActivityScoped
    @Binds
    abstract fun bindInMemoryLogger(impl: LoggerInMemoryDataSource): LoggerDataSource
}