package com.example.comicsapp.utils.network

import com.example.comicsapp.data.model.ResponseModel
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ResponseAdapter<T>(
    private val responseType: Type
) : CallAdapter<T, Call<ResponseModel<T>>> {
    override fun responseType(): Type = responseType

    override fun adapt(call: Call<T>): Call<ResponseModel<T>> {
        return ResponseCall(call)
    }
}