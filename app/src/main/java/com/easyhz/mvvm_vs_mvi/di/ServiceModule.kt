package com.easyhz.mvvm_vs_mvi.di

import com.easyhz.mvvm_vs_mvi.data.api.ExampleService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideExampleService(
        @MainRetrofit retrofit: Retrofit
    ): ExampleService = retrofit.create(ExampleService::class.java)
}