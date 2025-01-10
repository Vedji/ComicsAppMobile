package com.example.comicsapp.data.repository

import android.util.Log
import com.example.comicsapp.data.model.api.response.ApiBody
import com.example.comicsapp.data.model.api.response.ApiError
import com.example.comicsapp.data.model.api.response.ApiException
import com.example.comicsapp.data.model.api.response.ApiResponse
import retrofit2.Response

open class BaseRepository {

    suspend fun <T> safeApiCall(call: suspend () -> Response<T>): ApiResponse<T> {
        Log.d("safeApiCall", " run safeApiCall")

        return try {
            val response = call()
            Log.d("safeApiCall", "Example body ${response.body()!!.toString()}")
            Log.d("safeApiCall", response.code().toString())
            if (response.isSuccessful) {
                Log.d("safeApiCall", response.body()!!.toString())
                ApiResponse.Success(response.body()!!)
            } else {
                Log.d("safeApiCall", response.body()!!.toString())
                ApiResponse.Error(ApiBody(ApiError("in try", -1, "BaseRepository.safeApiCall"), "error"))
            }
        } catch (e: ApiException) {
            Log.d("safeApiCall", "safeApiCall -> ${e.hashCode()}")
            Log.d("safeApiCall", " Text ${e.errorText}")
            Log.d("safeApiCall", " Test ${e.bodyData.data.typeError}")
            ApiResponse.Error(e.bodyData)
        }catch (e: Exception) {
            Log.d("safeApiCall", e.toString())
            ApiResponse.Error(ApiBody(ApiError(e.localizedMessage?.toString() ?: "-1", -1, e.toString()), "error"))
         }
    }

}