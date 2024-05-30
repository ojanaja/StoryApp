package com.example.storyapp.api

import com.example.storyapp.utils.NetworkRequest
import org.json.JSONObject
import retrofit2.Response

abstract class ApiConfig {
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T> ) : NetworkRequest<T> {
        try {
            val response = apiCall()

            if(response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkRequest.Success(body)
                }
            }
            return error(getErrorMessage(message = response.errorBody()!!.string()))
        } catch (e: Exception) {
            return error("${e.message} : $e")
        }
    }

    private fun getErrorMessage(message: String) : String {
        val obj = JSONObject(message)
        return obj.getString("message")
    }

    private fun <T> error(errorMessage: String, data : T? = null) : NetworkRequest<T> =
        NetworkRequest.Error(errorMessage, data)
}