package com.example.comicsappmobile.data.repository

import com.example.comicsappmobile.data.dto.StateResponseDto
import com.example.comicsappmobile.data.dto.entities.metadata.Pagination
import com.example.comicsappmobile.data.dto.response.ExceptionApi
import com.example.comicsappmobile.data.dto.response.ResponseDto
import com.example.comicsappmobile.data.dto.response.data.ErrorDataDto
import com.example.comicsappmobile.utils.Logger
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

open class BaseRepository {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<out T>): StateResponseDto<T> {
        // Logger.debug("safeApiCall", "In safeApiCall")
        return try {
            val result = apiCall()
            // Logger.debug("safeApiCall -> UserRepository", "result = '$result'")

            // Logger.debug("safeApiCall", result.toString())
            val state: StateResponseDto<T> = when {

                result.isSuccessful -> {
                    val body = result.body() as ResponseDto<T>
                    val metadata = (result.body() as? ResponseDto<T>)?.metadata as? Pagination ?: null

                    Logger.debug("safeApiCall", " result.isSuccessful -> ${body.status}")
                    if (body.status == "success") {
                        StateResponseDto.Success(
                            data = body.data,
                            metadata = metadata,
                            status = body.status
                        )
                    } else {
                        StateResponseDto.Error(data = ErrorDataDto("", -1, "Empty body"))
                    }
                }
                else -> {
                    val errorBody = result.body() as? ResponseDto<ErrorDataDto>
                    Logger.debug("BaseRepository -> safeApiCall", errorBody.toString())
                    StateResponseDto.Error(data = errorBody?.data ?: ErrorDataDto("", result.code(), "Unknown error"))
                }
            }

            Logger.debug("getBookGenres", "safeApiCall -> $state")
            state
        }catch (e: ExceptionApi) {
            // Logger.debug("getBookGenres", "${e.message}")
            // Logger.debug("getBookGenres", "message = ${e.message}, body = ${e.bodyData.data.message}")
            StateResponseDto.Error(data = e.bodyData.data)
        } catch (e: HttpException) {
            StateResponseDto.Error(
                data = ErrorDataDto(
                    message = e.message ?: "Unknown error",
                    statusCode = e.code(),
                    typeError = "HttpException"
                )
            )
        } catch (e: IOException) {
            StateResponseDto.Error(
                data = ErrorDataDto(
                    message = "Network error: ${e.message}",
                    statusCode = 0,
                    typeError = "IOException"
                )
            )
        } catch (e: Exception) {
            StateResponseDto.Error(
                data = ErrorDataDto(
                    message = "Unexpected error: ${e.message}",
                    statusCode = 0,
                    typeError = "Exception"
                )
            )
        }
    }
}