package com.example.android.hilt.di.module

import com.example.android.hilt.navigator.AppNavigator
import com.example.android.hilt.navigator.AppNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * Module with bindings information for AppNavigator.
 *
 * AppNavigator needs information specific from the Activity (as AppNavigatorImpl
 * has an Activity as a dependency). Therefore, it must be installed in the
 * Activity container instead of the Application container.
 */

@InstallIn(ActivityComponent::class)
@Module
abstract class NavigationModule {


    /**
     * Abstract function that returns the AppNavigator interface
     * and the parameter is the implementation of that interface.
     *
     * Since AppNavigatorImpl is a plain class we define constructor
     * injection inside that class.
     */
    @Binds
    abstract fun bindNavigator(impl: AppNavigatorImpl): AppNavigator

}