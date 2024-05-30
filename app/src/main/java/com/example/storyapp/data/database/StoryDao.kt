package com.example.storyapp.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storyapp.data.Story

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(vararg story: Story)

    @Query("SELECT * FROM story")
    fun getStories(): PagingSource<Int, Story>


    @Query("DELETE FROM story")
    fun deleteAll()
}