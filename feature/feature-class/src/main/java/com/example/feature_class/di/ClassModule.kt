package com.example.feature_class.di

import com.example.feature_class.data.ClassRepositoryImpl
import com.example.feature_class.domain.repository.ClassRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ClassModule {
    @Binds
    @Singleton
    abstract fun bindClassRepository(
        impl: ClassRepositoryImpl
    ): ClassRepository

}