package com.example.core_firebase.di

import com.example.core_firebase.auth.FirebaseAuthManager
import com.example.core_firebase.firestore.user.FirebaseUserMapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth =
        FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore =
        FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideStorage(): FirebaseStorage =
        FirebaseStorage.getInstance()

    // ✅ PROVIDE MAPPER
    @Provides
    @Singleton
    fun provideFirebaseUserMapper(): FirebaseUserMapper =
        FirebaseUserMapper()

    // ✅ PROVIDE AUTH MANAGER
    @Provides
    @Singleton
    fun provideFirebaseAuthManager(
        auth: FirebaseAuth,
        mapper: FirebaseUserMapper
    ): FirebaseAuthManager =
        FirebaseAuthManager(auth, mapper)
}