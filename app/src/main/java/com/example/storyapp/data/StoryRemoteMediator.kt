package com.example.storyapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.storyapp.api.ApiService
import com.example.storyapp.data.database.RemoteKeys
import com.example.storyapp.data.database.StoryDatabase
import com.example.storyapp.utils.wrapEspressoIdlingResource

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database: StoryDatabase,
    private val apiService: ApiService,
    private val auth: String
) : RemoteMediator<Int, Story>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Story>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> getRemoteKeyClosestToCurrentPosition(state)?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            LoadType.PREPEND -> getRemoteKeyForFirstItem(state)?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = false)
            LoadType.APPEND -> getRemoteKeyForLastItem(state)?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = false)
        }

        return wrapEspressoIdlingResource {
            try {
                val responseData = apiService.getStories(auth, page, state.config.pageSize)
                val stories = responseData.body()?.listStory.orEmpty()
                val endOfPaginationReached = stories.isEmpty()

                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        database.remoteKeysDao().deleteRemoteKeys()
                        database.storyDao().deleteAll()
                    }

                    val keys = stories.map {
                        RemoteKeys(id = it.id, prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1, nextKey = if (endOfPaginationReached) null else page + 1)
                    }
                    database.remoteKeysDao().insertAll(keys)
                    responseData.body()!!.listStory.forEach {
                        val story = Story(it.id, it.name, it.description, it.createAt, it.photoUrl, it.longitude, it.latitude)
                        database.storyDao().insertStory(story)
                    }
                }

                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            } catch (exception: Exception) {
                return MediatorResult.Error(exception)
            }
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Story>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { story ->
            database.remoteKeysDao().getRemoteKeysId(story.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Story>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { story ->
            database.remoteKeysDao().getRemoteKeysId(story.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Story>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }
}
