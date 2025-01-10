package com.example.comicsapp.utils.network

import com.example.comicsapp.data.model.ResponseModel
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import com.example.comicsapp.utils.network.ResponseAdapter

class ResponseAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        // Проверяем, что возвращаемый тип является Call<>
        if (getRawType(returnType) != Call::class.java) {
            return null
        }

        // Проверяем, что Call содержит параметризированный тип
        if (returnType !is ParameterizedType) {
            throw IllegalArgumentException("Call return type must be parameterized as Call<Foo> or Call<out Foo>")
        }

        // Получаем тип внутри Call<>
        val callType = getParameterUpperBound(0, returnType)
        // Проверяем, что это ResponseModel<>
        val rawCallType = getRawType(callType)
        if (rawCallType != ResponseModel::class.java) {
            return null
        }

        // Проверяем, что ResponseModel<> параметризирован
        if (callType !is ParameterizedType) {
            throw IllegalArgumentException("ResponseModel return type must be parameterized as ResponseModel<Foo>")
        }

        // Получаем тип внутри ResponseModel<>
        val responseType = getParameterUpperBound(0, callType)

        // Возвращаем адаптер с правильно переданным типом
        return ResponseAdapter<Any>(responseType)
    }
}

