package com.jdosantos.gratitudewavev1.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.vertexai.GenerativeModel
import com.google.firebase.vertexai.vertexAI
import com.jdosantos.gratitudewavev1.data.firebase.AuthFirebaseRepository
import com.jdosantos.gratitudewavev1.data.firebase.ConfigUserFirebaseRepository
import com.jdosantos.gratitudewavev1.data.firebase.FeedbackFirebaseRepository
import com.jdosantos.gratitudewavev1.data.firebase.GoalsFirebaseRepository
import com.jdosantos.gratitudewavev1.data.firebase.NoteFirebaseRepository
import com.jdosantos.gratitudewavev1.data.firebase.TagFirebaseRepository
import com.jdosantos.gratitudewavev1.data.firebase.UserFirebaseRepository
import com.jdosantos.gratitudewavev1.data.firebase.UserPreferencesFirebaseRepository
import com.jdosantos.gratitudewavev1.domain.repository.AuthRepository
import com.jdosantos.gratitudewavev1.domain.repository.ConfigUserRepository
import com.jdosantos.gratitudewavev1.domain.repository.FeedbackRepository
import com.jdosantos.gratitudewavev1.domain.repository.GoalsRepository
import com.jdosantos.gratitudewavev1.domain.repository.NoteRepository
import com.jdosantos.gratitudewavev1.domain.repository.TagRepository
import com.jdosantos.gratitudewavev1.domain.repository.UserPreferencesRepository
import com.jdosantos.gratitudewavev1.domain.repository.UserRepository
import com.jdosantos.gratitudewavev1.domain.services.LanguageProvider
import com.jdosantos.gratitudewavev1.domain.services.impl.DefaultLanguageProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

//    @Singleton
    @Provides
    fun provideFirebseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Singleton
    @Provides
    fun provideGenerativeModel(): GenerativeModel {
        return Firebase.vertexAI.generativeModel("gemini-1.5-flash-preview-0514")
    }

    @Singleton
    @Provides
    fun provideFirebseFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Singleton
    @Provides
    fun providesLoginFirebase(auth: FirebaseAuth): AuthRepository {
        return AuthFirebaseRepository(auth)
    }

    @Singleton
    @Provides
    fun providesNoteFirebase(auth: FirebaseAuth?, db: FirebaseFirestore): NoteRepository {

        return NoteFirebaseRepository(auth, db)
    }

    @Singleton
    @Provides
    fun providesUserFirebase(auth: FirebaseAuth?, db: FirebaseFirestore): UserRepository {
        return UserFirebaseRepository(auth, db)
    }

    @Singleton
    @Provides
    fun provideUserPreferencesFirebase(
        auth: FirebaseAuth?,
        db: FirebaseFirestore
    ): UserPreferencesRepository {
        return UserPreferencesFirebaseRepository(auth, db)
    }

    @Singleton
    @Provides
    fun provideTagFirebase(db: FirebaseFirestore): TagRepository {
        return TagFirebaseRepository(db)
    }

    @Singleton
    @Provides
    fun provideGoalsFirebase(auth: FirebaseAuth?, db: FirebaseFirestore): GoalsRepository {
        return GoalsFirebaseRepository(auth, db)
    }

    @Singleton
    @Provides
    fun provideConfigUserFirebase(
        auth: FirebaseAuth?,
        db: FirebaseFirestore
    ): ConfigUserRepository {
        return ConfigUserFirebaseRepository(auth, db)
    }

    @Singleton
    @Provides
    fun provideFeedbackFirebase(
        auth: FirebaseAuth?,
        db: FirebaseFirestore
    ): FeedbackRepository {
        return FeedbackFirebaseRepository(auth, db)
    }

    @Provides
    @Singleton
    fun provideLanguageProvider(@ApplicationContext context: Context): LanguageProvider {
        return DefaultLanguageProvider(context)
    }
}