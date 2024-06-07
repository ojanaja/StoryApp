package com.example.storyapp.utils


import com.example.storyapp.data.ListStory
import com.example.storyapp.data.Story
import com.example.storyapp.model.ResponseHome
import com.example.storyapp.model.ResponseLogin
import com.example.storyapp.model.ResponseRegister
import com.example.storyapp.model.ResponseUploadStory
import com.example.storyapp.model.Result
import com.example.storyapp.model.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

object DataDummy {
    fun generateDummyStoriesResponse(): ResponseHome {
        val error = false
        val message = "Stories fetched successfully"
        val listStory = mutableListOf<StoryResponse>()

        for (i in 0 until 10) {
            val story = StoryResponse(
                id = "story-${generateRandomString(16)}",
                photoUrl = "https://example.com/image$i.jpg",
                createAt = "2022-01-08T06:34:18.598Z",
                name = "User $i",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                longitude = generateRandomDouble(-180.0, 180.0),
                latitude = generateRandomDouble(-90.0, 90.0)
            )

            listStory.add(story)
        }

        return ResponseHome(error, message, listStory)
    }

    fun generateDummyListStory(): List<Story> {
        val items = arrayListOf<Story>()

        for (i in 0 until 20) {
            val story = Story(
                id = "story-${generateRandomString(16)}",
                photoUrl = "https://example.com/image$i.jpg",
                createdAt = "2022-01-08T06:34:18.598Z",
                name = "User $i",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                lon = generateRandomDouble(-180.0, 180.0),
                lat = generateRandomDouble(-90.0, 90.0)
            )

            items.add(story)
        }

        return items
    }


    fun generateDummyLoginResponse(): ResponseLogin {
        val loginResult = Result(
            userId = "story-DyGewy241D6ZmJI9",
            name = "Sam",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXNHamQzZWx0Wk1zckl1M3IiLCJpYXQiOjE2NTcyMTc2NjV9.ZlZaTNeZX3Db4KYwTkIaiUTBy5oX-3DkSmlSnpSuZws"
        )

        return ResponseLogin(
            result = loginResult,
            error = false,
            message = "success"
        )
    }

    fun generateDummyRegisterResponse(): ResponseRegister {
        return ResponseRegister(
            error = false,
            message = "success"
        )
    }

    fun generateDummyMultipartFile(): MultipartBody.Part {
        val dummyText = "text"
        return MultipartBody.Part.create(dummyText.toRequestBody())
    }

    fun generateDummyRequestBody(): String {
        return "text"
    }

    fun generateDummyFileUploadResponse(): ResponseUploadStory {
        return ResponseUploadStory(
            error = false,
            message = "success"
        )
    }

    fun generateDummyToken(): String {
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXNHamQzZWx0Wk1zckl1M3IiLCJpYXQiOjE2NTcyMTc2NjV9.ZlZaTNeZX3Db4KYwTkIaiUTBy5oX-3DkSmlSnpSuZws"
    }

    private fun generateRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun generateRandomDouble(min: Double, max: Double): Double {
        return min + (max - min) * Math.random()
    }
}