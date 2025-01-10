package com.example.comicsapp.utils.network

import android.util.Log
import com.example.comicsapp.BuildConfig
import com.example.comicsapp.data.api.ApiService
import com.example.comicsapp.data.api.BookEndpoints
import com.example.comicsapp.data.api.GenresApi
import com.example.comicsapp.data.model.api.response.ApiBody
import com.example.comicsapp.data.model.api.response.ApiError
import com.example.comicsapp.data.model.api.response.ApiException
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {
    private const val BASE_URL = BuildConfig.API_BASE_URL // Укажите ваш URL

    // Переменная для хранения токена
    var accessToken: String? = null

    // Интерцептор для добавления токена авторизации в заголовок
    private val authInterceptor = Interceptor { chain ->
        val originalRequest: Request = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        // Если токен доступен, добавляем его в заголовок Authorization
        accessToken?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        val request = requestBuilder.build()
        chain.proceed(request)
    }

    private val errorHandlingInterceptor = Interceptor { chain ->
        val response = chain.proceed(chain.request())
        if (!response.isSuccessful) {
            val errorBody = response.body?.string() // Считываем тело ошибки
            var err = ApiException(
                response.code, "nullable in errorHandlingInterceptor",
                ApiBody(
                    data = ApiError(
                        errorBody.toString(),
                        response.code,
                        "RetrofitInstance.errorHandlingInterceptor nullable in errorHandlingInterceptor"),
                    "error")
            )
            try {
                val gson = Gson()
                // Парсим тело ошибки как ApiBody<ApiError>
                val apiErrorBody: ApiBody<ApiError> = gson.fromJson(errorBody, object : TypeToken<ApiBody<ApiError>>() {}.type)
                Log.d("safeApiCall", "errorHandlingInterceptor-> ${apiErrorBody.data.typeError}")
                err = ApiException(response.code, apiErrorBody.data.message, apiErrorBody)

            } catch (e: JsonSyntaxException) {
                // Если ошибка при парсинге JSON, выбрасываем стандартное исключение
                err = ApiException(
                    response.code,
                    "RetrofitInstance.errorHandlingInterceptor -> ${e.localizedMessage}",
                    ApiBody(
                        data = ApiError(
                            "${e.localizedMessage}",
                            response.code,
                            "RetrofitInstance.errorHandlingInterceptor"
                        ),
                        "error")
                )
            } catch (e: Exception) {
                // Логируем и обрабатываем любую другую ошибку
                err = ApiException(
                    response.code,
                    "RetrofitInstance -> ${e.localizedMessage}",
                    ApiBody(
                        data = ApiError(
                            e.localizedMessage, response.code,
                            "RetrofitInstance.errorHandlingInterceptor"),
                        "error")
                )
            }
            Log.d("safeApiCall", "RetrofitInstance -> ${err.hashCode()}")
            throw err
        }
        response
    }


    // Логгирование для удобной отладки
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Настройка OkHttpClient с интерцепторами
    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(errorHandlingInterceptor)
            .build()
    }

    // Retrofit с поддержкой авторизации и формата form-data
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    // Retrofit с поддержкой авторизации и формата form-data
    val genresApi: GenresApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GenresApi::class.java)
    }

    val bookApi: BookEndpoints by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookEndpoints::class.java)
    }


}