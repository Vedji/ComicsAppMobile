package com.example.comicsappmobile.di

import android.util.Log
import com.example.comicsappmobile.BuildConfig
import com.example.comicsappmobile.data.api.BooksApi
import com.example.comicsappmobile.data.api.ChaptersApi
import com.example.comicsappmobile.data.api.CommentsApi
import com.example.comicsappmobile.data.api.FavoriteApi
import com.example.comicsappmobile.data.api.FilesApi
import com.example.comicsappmobile.data.api.GenresApi
import com.example.comicsappmobile.data.api.PagesApi
import com.example.comicsappmobile.data.api.UsersApi
import com.example.comicsappmobile.data.dto.response.data.ErrorDataDto
import com.example.comicsappmobile.data.dto.response.ExceptionApi
import com.example.comicsappmobile.data.dto.response.ResponseDto
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
    private const val BASE_URL = BuildConfig.API_BASE_URL // URL
    // private const val BASE_URL = "http://10.8.1.2:5000" // URL Local server

    // Переменная для хранения токена
    var accessToken: String? = null
    var refreshToken: String? = null

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

            var err = ResponseDto<ErrorDataDto>(
                data = ErrorDataDto(
                    message = errorBody.toString(),
                    statusCode = response.code,
                    typeError = "RetrofitInstance.errorHandlingInterceptor nullable in errorHandlingInterceptor"
                )
            )
            try {
                val gson = Gson()
                // Парсим тело ошибки как ApiBody<ApiError>
                val apiErrorBody: ResponseDto<ErrorDataDto> = gson.fromJson(
                    errorBody,
                    object : TypeToken<ResponseDto<ErrorDataDto>>() {}.type
                )
                Log.d("safeApiCall", "errorHandlingInterceptor-> ${apiErrorBody.data.typeError}")
                err = ResponseDto<ErrorDataDto>(
                    data = ErrorDataDto(
                        message = apiErrorBody.data.message,
                        statusCode = response.code,
                        typeError = apiErrorBody.data.typeError
                    )
                )

            } catch (e: JsonSyntaxException) {
                // Если ошибка при парсинге JSON, выбрасываем стандартное исключение
                err = ResponseDto<ErrorDataDto>(
                    data = ErrorDataDto(
                        message = "${e.localizedMessage}",
                        statusCode = response.code,
                        typeError = "RetrofitInstance.errorHandlingInterceptor -> ${e.localizedMessage}"
                    )
                )
            } catch (e: Exception) {
                // Логируем и обрабатываем любую другую ошибку
                err = ResponseDto<ErrorDataDto>(
                    data = ErrorDataDto(
                        message = e.localizedMessage?.toString() ?: "RetrofitInstance -> catch",
                        statusCode = response.code,
                        typeError = "RetrofitInstance -> ${e.localizedMessage}"
                    )
                )
            }
            Log.d("safeApiCall", "RetrofitInstance -> ${err.hashCode()}")
            throw ExceptionApi(code = response.code, "", err)
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
    val genresApi: GenresApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GenresApi::class.java)
    }

    val bookApi: BooksApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksApi::class.java)
    }

    val chaptersApi: ChaptersApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChaptersApi::class.java)
    }

    val pagesApi: PagesApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PagesApi::class.java)
    }

    val commentsApi: CommentsApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CommentsApi::class.java)
    }

    val userApi: UsersApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UsersApi::class.java)
    }

    val favoriteApi: FavoriteApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FavoriteApi::class.java)
    }

    val filesApi: FilesApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FilesApi::class.java)
    }


}