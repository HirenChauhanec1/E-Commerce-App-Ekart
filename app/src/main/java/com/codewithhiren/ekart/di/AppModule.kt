package com.codewithhiren.ekart.di

import com.codewithhiren.ekart.repo.AuthRepo
import com.codewithhiren.ekart.repo.AuthRepoImpl
import com.codewithhiren.ekart.repo.ShoppingRepo
import com.codewithhiren.ekart.repo.ShoppingRepoImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class AppModule {

    companion object {
        @Singleton
        @Provides
        fun getFirebaseAuth(): FirebaseAuth = Firebase.auth

        @Singleton
        @Provides
        fun getFireStore() : FirebaseFirestore = Firebase.firestore

        @StorageProfilePic
        @Singleton
        @Provides
        fun getFirebaseStorageReference(auth: FirebaseAuth) : StorageReference =
            Firebase.storage.reference.child("userProfilePic/${auth.uid!!}")

        @DispatcherIO
        @Singleton
        @Provides
        fun getDispatchIO() : CoroutineDispatcher = Dispatchers.IO
    }

    @Singleton
    @Binds
    abstract fun getAuthRepo(authRepoImpl: AuthRepoImpl) :  AuthRepo

    @Singleton
    @Binds
    abstract fun getShoppingRepo(shoppingRepoImpl: ShoppingRepoImpl) : ShoppingRepo

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DispatcherIO

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class StorageProfilePic