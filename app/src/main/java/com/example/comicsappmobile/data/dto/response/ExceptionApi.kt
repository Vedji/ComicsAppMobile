package com.example.comicsappmobile.data.dto.response

import com.example.comicsappmobile.data.dto.response.data.ErrorDataDto
import java.io.IOException

class ExceptionApi(
    val code: Int,
    val errorText: String?,
    val bodyData: ResponseDto<ErrorDataDto>
) : IOException("HTTP $code: $errorText")