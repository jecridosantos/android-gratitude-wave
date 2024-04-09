package com.jdosantos.gratitudewavev1.core.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.jdosantos.gratitudewavev1.app.repository.AuthRepository
import com.jdosantos.gratitudewavev1.app.repository.GoalsRepository
import com.jdosantos.gratitudewavev1.app.repository.NoteRepository
import com.jdosantos.gratitudewavev1.app.repository.TagRepository
import com.jdosantos.gratitudewavev1.app.repository.UserRepository
import com.jdosantos.gratitudewavev1.app.repository.impl.AuthFirebaseImpl
import com.jdosantos.gratitudewavev1.app.repository.impl.GoalsFirebaseImpl
import com.jdosantos.gratitudewavev1.app.repository.impl.NoteFirebaseImpl
import com.jdosantos.gratitudewavev1.app.repository.impl.TagFirebaseImpl
import com.jdosantos.gratitudewavev1.app.repository.impl.UserFirebaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFirebseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Singleton
    @Provides
    fun provideFirebseFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Singleton
    @Provides
    fun providesLoginFirebase(auth: FirebaseAuth): AuthRepository {
        return AuthFirebaseImpl(auth)
    }

    @Singleton
    @Provides
    fun providesNoteFirebase(auth: FirebaseAuth?, db: FirebaseFirestore): NoteRepository {
        return NoteFirebaseImpl(auth, db)
    }

    @Singleton
    @Provides
    fun providesUserFirebase(auth: FirebaseAuth?, db: FirebaseFirestore): UserRepository {
        return UserFirebaseImpl(auth, db)
    }

    @Singleton
    @Provides
    fun provideTagFirebase(db: FirebaseFirestore): TagRepository {
        return TagFirebaseImpl(db)
    }

    @Singleton
    @Provides
    fun provideGoalsFirebase(auth: FirebaseAuth?, db: FirebaseFirestore): GoalsRepository {
        return GoalsFirebaseImpl(auth, db)
    }

}