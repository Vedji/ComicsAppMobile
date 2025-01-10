package com.example.comicsapp.utils.network

import com.example.comicsapp.data.model.ErrorDataModel
import com.example.comicsapp.data.model.ResponseModel
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ResponseCall<T>(
    private val delegate: Call<T>
) : Call<ResponseModel<T>> {
    override fun enqueue(callback: Callback<ResponseModel<T>>) {
        delegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val apiResponse = if (response.isSuccessful) {
                    ResponseModel.Success(
                        data = response.body()!!,
                        metadata = response.headers().toMultimap()
                    )
                } else {
                    ResponseModel.Error(
                        data = ErrorDataModel(
                            message = response.errorBody()?.string() ?: "Unknown error",
                            statusCode = response.code(),
                            typeError = "HttpError"
                        )
                    )
                }
                callback.onResponse(this@ResponseCall, Response.success(apiResponse))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val apiResponse = ResponseModel.Error(
                    data = ErrorDataModel(
                        message = t.message ?: "Unknown error",
                        statusCode = -1,
                        typeError = "NetworkError"
                    )
                )
                callback.onResponse(this@ResponseCall, Response.success(apiResponse))
            }
        })
    }

    override fun clone(): Call<ResponseModel<T>> = ResponseCall(delegate.clone())
    override fun execute(): Response<ResponseModel<T>> =
        throw UnsupportedOperationException("Synchronous execution is not supported")
    override fun isExecuted() = delegate.isExecuted
    override fun cancel() = delegate.cancel()
    override fun isCanceled() = delegate.isCanceled
    override fun request(): Request = delegate.request()
    override fun timeout() = delegate.timeout()
}