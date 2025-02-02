package com.example.comicsappmobile.data.repository

import com.example.comicsappmobile.data.api.BooksApi
import com.example.comicsappmobile.data.dto.StateResponseDto
import com.example.comicsappmobile.data.dto.entities.BookDto
import com.example.comicsappmobile.data.dto.entities.metadata.Pagination
import com.example.comicsappmobile.data.dto.response.data.ErrorDataDto
import com.example.comicsappmobile.data.mapper.BookMapper
import com.example.comicsappmobile.di.RetrofitInstance
import com.example.comicsappmobile.ui.presentation.model.BookUiModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.utils.Logger


class BooksRepository (
    private val booksApi: BooksApi
): BaseRepository() {

    suspend fun getBook(bookId: Int): UiState<BookUiModel> {
        val test = safeApiCall { booksApi.getBook(bookId) }

        return when(test) {
            is StateResponseDto.Success -> {
                val data = (test.data as? BookDto) ?: BookDto()
                Logger.debug("safeApiCall", "BookId = $bookId")
                Logger.debug("safeApiCall", data.bookTitleImageId.toString() ?: "NoContent")
                UiState.Success(data = BookMapper.map(data))
            }
            is StateResponseDto.Error -> {
                val data = (test.data as? ErrorDataDto)
                Logger.debug("return when(test)", "data = '${test}'")
                UiState.Error(
                    data = null,
                    message = data?.message ?: "BooksRepository -> getBook",
                    typeError = data?.typeError ?: "GenresRepository -> getBookGenres",
                    statusCode = -1
                )
            }
        }
    }

    suspend fun getBooks(
        limit: Int = 10,
        offset: Int = 0,
        search: String = "",
        sortBy: String = "ratingDESC",
        genresId: List<Int> = emptyList()
    ): UiState<List<BookUiModel>> {




        val test = safeApiCall {
            booksApi.getBooks(
                limit = limit,
                offset = offset,
                search = search,
                sortBy = sortBy,
                genresId = genresId
            )
        }

        return when (test) {
            is StateResponseDto.Success -> {
                val data = (test.data as? List<BookDto>) ?: emptyList()
                val metadata = (test.metadata as? Pagination)
                UiState.Success(data = BookMapper.mapList(data), metadata = metadata)
            }

            is StateResponseDto.Error -> {
                val data = (test.data as? ErrorDataDto)
                Logger.debug("return when(test)", "data = '${test}'")
                UiState.Error(
                    data = null,
                    message = data?.message ?: "BooksRepository -> getBooks",
                    typeError = data?.typeError ?: "BooksRepository -> getBooks",
                    statusCode = -1
                )
            }
        }
    }

    suspend fun updateBook(
        bookId: Int,
        bookName: String?,
        bookGenres: List<Int>?,
        bookDescription: String?,
        bookDateOfPublication: String?,
        bookTitleImageId: Int?,
        bookChaptersSequence: List<Int>?
    ): UiState<BookUiModel> {
        val token: String = RetrofitInstance.accessToken ?: ""
        if (token.isEmpty()) return UiState.Error(message = "No access token")

        val test = safeApiCall { booksApi.uploadBook(
            token = token,
            bookId = bookId,
            bookName = bookName,
            bookGenres = bookGenres,
            bookDescription = bookDescription,
            bookDateOfPublication = bookDateOfPublication,
            bookTitleImageId = bookTitleImageId,
            bookChaptersSequence = bookChaptersSequence
        ) }

        return when(test) {
            is StateResponseDto.Success -> {
                val data = (test.data as? BookDto) ?: BookDto()
                Logger.debug("BooksRepository -> updateBook", "BookId = $bookId")
                Logger.debug("BooksRepository -> updateBook", data.bookTitleImageId.toString() ?: "NoContent")
                UiState.Success(data = BookMapper.map(data))
            }
            is StateResponseDto.Error -> {
                val data = (test.data as? ErrorDataDto)
                Logger.debug("BooksRepository -> updateBook", "errorData = '${test}'")
                UiState.Error(
                    data = null,
                    message = data?.message ?: "BooksRepository -> updateBook",
                    typeError = data?.typeError ?: "BooksRepository -> updateBook",
                    statusCode = -1
                )
            }
        }
    }

}