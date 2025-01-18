package com.example.comicsappmobile.utils.remote

import com.example.comicsappmobile.BuildConfig
import com.example.comicsappmobile.data.api.BooksApi
import com.example.comicsappmobile.data.api.GenresApi
import com.example.comicsappmobile.data.dto.response.data.ErrorDataDto
import com.example.comicsappmobile.data.dto.response.data.ListDto
import com.example.comicsappmobile.data.dto.StateResponseDto
import com.example.comicsappmobile.utils.Logger
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import com.squareup.moshi.*
import com.squareup.moshi.Types


class ResponseDtoAdapter<T>(
    private val dataAdapter: JsonAdapter<T>
) : JsonAdapter<StateResponseDto<T>>() {

    private val errorAdapter: JsonAdapter<ErrorDataDto> =
        Moshi.Builder().build().adapter(ErrorDataDto::class.java)

    @FromJson
    override fun fromJson(reader: JsonReader): StateResponseDto<T> {
        Logger.debug("fromJson", reader.toString())
        var status: String? = null
        var data: T? = null
        var errorData: ErrorDataDto? = null
        var metadata: Any? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "status" -> status = reader.nextString()
                "data" -> {
                    when (status) {
                        "success" -> data = dataAdapter.fromJson(reader)
                        "error" -> errorData = errorAdapter.fromJson(reader)
                        else -> reader.skipValue()
                    }
                }
                "metadata" -> metadata = reader.readJsonValue()
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return when (status) {
            "success" -> {
                if (data == null) throw JsonDataException("Missing 'data' field for success response")
                StateResponseDto.Success(data = data, metadata = metadata)
            }
            "error" -> {
                if (errorData == null) throw JsonDataException("Missing 'data' field for error response")
                StateResponseDto.Error(data = errorData, metadata = metadata)
            }
            else -> throw JsonDataException("Unknown status: $status")
        }
    }

    fun <T> createResponseDtoAdapter(
        moshi: Moshi,
        type: Class<T>
    ): ResponseDtoAdapter<T> {
        val parameterizedType = Types.newParameterizedType(ListDto::class.java, type)
        val dataAdapter = moshi.adapter<T>(parameterizedType)
        return ResponseDtoAdapter(dataAdapter)
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: StateResponseDto<T>?) {
        if (value == null) throw JsonDataException("ResponseDto cannot be null")

        writer.beginObject()
        when (value) {
            is StateResponseDto.Success -> {
                writer.name("status").value("success")
                writer.name("data")
                dataAdapter.toJson(writer, value.data)
                writer.name("metadata").value(value.metadata?.toString())
            }
            is StateResponseDto.Error -> {
                writer.name("status").value("error")
                writer.name("data")
                errorAdapter.toJson(writer, value.data)
                writer.name("metadata").value(value.metadata?.toString())
            }
        }
        writer.endObject()
    }
}

fun provideMoshi(): Moshi {
    return Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
}


// Provide OkHttpClient with logging and timeout settings
fun provideOkHttpClient(): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
}

// Provide Retrofit instance
fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
}

// Provide ApiService instance
fun provideGenresApiService(retrofit: Retrofit): GenresApi {
    return retrofit.create(GenresApi::class.java)
}

fun provideBookApiService(retrofit: Retrofit): BooksApi {
    return retrofit.create(BooksApi::class.java)
}