package com.example.comicsapp.domain.model

import android.util.Log
import com.example.comicsapp.data.model.api.response.ApiError

class DomainError (
    private var message: String = "No message",
    private var statusCode: Int = -2,
    private var typeError: String = "No typ error",
    private var isChecked: Boolean = false
){
    fun getString(): String{
        return "Type error: $typeError, HTTP CODE: $statusCode, Message: $message"
    }

    fun setError(err: DomainError?){
        if (err == null){
            message = "No message"
            statusCode = -2
            typeError = "No typ error"
            isChecked = true
        } else {
            message = err.message
            statusCode = err.statusCode
            typeError = err.typeError
            isChecked = false
        }
        Log.d("ComicsAppTheme", isChecked.toString())
    }

    fun setFromApi(err: ApiError?){

        if (err == null){
            message = "No message"
            statusCode = -2
            typeError = "No typ error"
            isChecked = true
        } else {
            message = err.message
            statusCode = err.statusCode
            typeError = err.typeError
            isChecked = false
        }

    }
    fun getMessage(): String{
        return message
    }

    fun getStatusCode(): Int{
        return statusCode
    }

    fun getTypeError(): String{
        return typeError
    }

    fun isErrorChecked(): Boolean{
        return isChecked
    }

}