package com.example.storyapp.di

import android.content.Context
import androidx.room.Room
import com.example.storyapp.BuildConfig
import com.example.storyapp.api.ApiService
import com.example.storyapp.data.database.RemoteKeysDao
import com.example.storyapp.data.database.StoryDao
import com.example.storyapp.data.database.StoryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val API_BASE_URL = BuildConfig.SERVER_URL

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .connectTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(client: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideStoryDatabase(@ApplicationContext context: Context): StoryDatabase {
        return Room.databaseBuilder(context, StoryDatabase::class.java, "db_story")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideStoryDao(storyDatabase: StoryDatabase): StoryDao = storyDatabase.storyDao()

    @Provides
    fun provideRemoteKeysDao(storyDatabase: StoryDatabase): RemoteKeysDao = storyDatabase.remoteKeysDao()

}
