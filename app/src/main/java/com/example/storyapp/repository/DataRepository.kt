package com.example.storyapp.repository

import com.example.storyapp.model.ResponseLogin
import com.example.storyapp.model.ResponseRegister
import com.example.storyapp.model.ResponseUploadStory
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.utils.NetworkRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import javax.inject.Inject
class DataRepository @Inject constructor(
    private val dataSource: DataSource
) : ApiConfig() {
    suspend fun uploadStory(
        auth: String,
        description: String,
        file: MultipartBody.Part
    ): Flow<NetworkRequest<ResponseUploadStory>> =
        flow {
            emit(safeApiCall {
                val generateToken = generateAuthorization(auth)
                dataSource.uploadStory(generateToken, description, file)
            })
        }.flowOn(Dispatchers.IO)

    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Flow<NetworkRequest<ResponseRegister>> = flow {
        emit(safeApiCall {
            dataSource.register(name, email, password)
        })
    }.flowOn(Dispatchers.IO)

    suspend fun login(email: String, password: String): Flow<NetworkRequest<ResponseLogin>> =
        flow {
            emit(safeApiCall {
                dataSource.login(email, password)
            })
        }.flowOn(Dispatchers.IO)


    private fun generateAuthorization(token: String): String {
        return "Bearer $token"
    }
}