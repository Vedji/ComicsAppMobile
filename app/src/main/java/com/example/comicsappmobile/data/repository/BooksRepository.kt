package com.example.comicsappmobile.data.repository

import com.example.comicsappmobile.data.api.BooksApi
import com.example.comicsappmobile.data.mapper.BookMapper
import com.example.comicsappmobile.data.dto.StateResponseDto
import com.example.comicsappmobile.data.dto.entities.BookDto
import com.example.comicsappmobile.data.dto.entities.metadata.Pagination
import com.example.comicsappmobile.data.dto.response.data.ErrorDataDto
import com.example.comicsappmobile.utils.Logger
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import com.example.comicsappmobile.ui.presentation.model.BookUiModel


class BooksRepository (
    private val booksApi: BooksApi
): BaseRepository() {

    private val books = listOf(
        BookDto(
            bookId = 1,
            rusTitle = "Преступление и наказание",
            bookTitleImageId = 101,
            bookRating = 4.8f,
            bookDescription = "Роман Ф.М. Достоевского о моральных дилеммах и искуплении.",
            bookDatePublication = "1866-01-01",
            bookISBN = "978-5-389-07431-7",
            bookAddedBy = 2,
            uploadDate = "2025-01-01"
        ),
        BookDto(
            bookId = 2,
            rusTitle = "Война и мир",
            bookTitleImageId = 102,
            bookRating = 4.9f,
            bookDescription = "Эпический роман Л.Н. Толстого о жизни во времена Наполеоновских войн.",
            bookDatePublication = "1869-01-01",
            bookISBN = "978-5-389-06194-2",
            bookAddedBy = 3,
            uploadDate = "2025-01-02"
        ),
        BookDto(
            bookId = 3,
            rusTitle = "Мастер и Маргарита",
            bookTitleImageId = 103,
            bookRating = 4.7f,
            bookDescription = "Роман М.А. Булгакова о борьбе добра и зла в советской Москве.",
            bookDatePublication = "1967-01-01",
            bookISBN = "978-5-699-98256-6",
            bookAddedBy = 4,
            uploadDate = "2025-01-03"
        )
    )

    init {

    }

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
}