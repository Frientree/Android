package com.d101.frientree.di

import com.d101.data.api.AuthService
import com.d101.data.utils.AuthAuthenticator
import com.d101.data.utils.AuthInterceptor
import com.d101.domain.utils.TokenManager
import com.d101.frientree.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            setLevel(HttpLoggingInterceptor.Level.NONE)
        }
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class FrientreeClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class SocialLoginClient

    @Singleton
    @Provides
    @FrientreeClient
    fun provideFrientreeClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.apply {
            addInterceptor(authInterceptor)
            authenticator(authAuthenticator)
            addInterceptor(loggingInterceptor)
        }
        return builder.build()
    }

    @Singleton
    @Provides
    @AuthClient
    fun provideAuthClient(
        authInterceptor: AuthInterceptor,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.apply {
            addInterceptor(authInterceptor)
            addInterceptor(loggingInterceptor)
        }
        return builder.build()
    }

    @Singleton
    @Provides
    @SocialLoginClient
    fun provideSocialLoginClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.apply {
            addInterceptor(loggingInterceptor)
        }
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun providesAuthorizationInterceptor(tokenManager: TokenManager): AuthInterceptor =
        AuthInterceptor(tokenManager)

    @Singleton
    @Provides
    fun provideTokenAuthenticator(
        authService: AuthService,
        tokenManager: TokenManager,
    ): AuthAuthenticator = AuthAuthenticator(authService, tokenManager)
}
