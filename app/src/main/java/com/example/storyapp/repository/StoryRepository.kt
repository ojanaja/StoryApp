package com.example.storyapp.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.storyapp.api.ApiService
import com.example.storyapp.data.Story
import com.example.storyapp.data.StoryRemoteMediator
import com.example.storyapp.data.database.StoryDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalPagingApi::class)
class StoryRepository @Inject constructor(
    private val database: StoryDatabase,
    private val apiService: ApiService
) {

    fun getStories(auth: String) : Flow<PagingData<Story>> =
        Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = StoryRemoteMediator(
                database,
                apiService,
                generateAuthorization(auth)
            ),
            pagingSourceFactory ={
                database.storyDao().getStories()
            }
        ).flow

    private fun generateAuthorization(token: String): String {
        return "Bearer $token"
    }

}