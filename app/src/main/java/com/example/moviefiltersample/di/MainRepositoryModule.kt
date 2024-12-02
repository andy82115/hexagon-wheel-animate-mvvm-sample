package com.example.moviefiltersample.di

import com.example.moviefiltersample.api.MovieApi
import com.example.moviefiltersample.repository.MainRepository
import com.example.moviefiltersample.repository.MainRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainRepositoryModule {
    @Provides
    @Singleton
    fun provideMainRepository(
        movieApi: MovieApi
    ): MainRepository {
        return MainRepositoryImpl(movieApi)
    }
}