package com.example.comicsapp.data.model.api.response

import java.io.IOException

class ApiException(
    val code: Int,
    val errorText: String?,
    val bodyData: ApiBody<ApiError>
) : IOException("HTTP $code: $errorText")