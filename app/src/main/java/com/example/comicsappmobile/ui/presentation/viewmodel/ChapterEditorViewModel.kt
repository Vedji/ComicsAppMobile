package com.example.comicsappmobile.ui.presentation.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.example.comicsappmobile.data.repository.BooksRepository
import com.example.comicsappmobile.data.repository.ChaptersRepository
import com.example.comicsappmobile.data.repository.FilesRepository
import com.example.comicsappmobile.data.repository.GenresRepository
import com.example.comicsappmobile.data.repository.PagesRepository
import com.example.comicsappmobile.di.GlobalState
import com.example.comicsappmobile.ui.presentation.model.BookUiModel
import com.example.comicsappmobile.ui.presentation.model.ChapterUiModel
import com.example.comicsappmobile.ui.presentation.model.FileUiModel
import com.example.comicsappmobile.ui.presentation.model.PageUiModel
import com.example.comicsappmobile.utils.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChapterEditorViewModel(
    val bookId: Int = -1,
    var chapterId: Int = -1,
    private val chaptersRepository: ChaptersRepository,
    private val pagesRepository: PagesRepository,
    private val filesRepository: FilesRepository,
    private val globalState: GlobalState
): BaseViewModel() {

    private val _chapterUiState = MutableStateFlow<UiState<ChapterUiModel>>(UiState.Loading())
    val chapterUiState: StateFlow<UiState<ChapterUiModel>> = _chapterUiState

    private val _pagesUiState = MutableStateFlow<UiState<List<PageUiModel>>>(UiState.Loading())
    val pagesUiState: StateFlow<UiState<List<PageUiModel>>> = _pagesUiState

    init { refresh() }

    fun refresh(){
        viewModelScope.launch {
            loadChapter(firstLoading = true)
            loadPages()
        }
    }

    private suspend fun loadChapter(firstLoading: Boolean = false){
        if ((_chapterUiState.value !is UiState.Success || chapterId <= 0) && !firstLoading){
            _chapterUiState.value = UiState.Success(data = ChapterUiModel())
            return
        }
        val chapters = loadWithState { chaptersRepository.getBookChapters(bookId = bookId) }
        if (chapters is UiState.Error){
            _chapterUiState.value = UiState.Error(
                message = chapters.message,
                typeError = chapters.typeError,
                statusCode = chapters.statusCode
            )
            return
        }
        val chapterCurrent: ChapterUiModel = chapters.data?.findLast { it.chapterId == chapterId } ?: ChapterUiModel()
        _chapterUiState.value = UiState.Success( data = chapterCurrent )

    }

    private suspend fun loadPages(){
        if (_chapterUiState.value !is UiState.Success || chapterId <= 0){
            _pagesUiState.value = UiState.Success(data = emptyList())
            return
        }
        _pagesUiState.value = UiState.Loading()
        _pagesUiState.value = loadWithState { pagesRepository.getChapterPages(bookId = bookId, chapterId = chapterId) }
    }

    suspend fun uploadChapter(
        chapterTitle: String,
        inputPages: List<PageUiModel>,
        inputImages: Map<Int, Uri>,
        context: Context
    ): Boolean{
        if (_chapterUiState.value !is UiState.Success || _pagesUiState.value !is UiState.Success)
            return false
        val pagesIds: List<Int> = inputPages.map { -1 }
        val pagesImagesIds: MutableList<Int> = mutableListOf()
        for (page in inputPages){
            var pageImageId = page.pageImageId
            if (pageImageId <= 0){
                if (inputImages[page.pageNumber] == null)
                    return false
                val buffer = loadWithState { filesRepository.uploadFile(context, inputImages[page.pageNumber]!!) }
                if (buffer !is UiState.Success)
                    return false
                pageImageId = buffer.data?.fileID ?: 4
            }else{
                pageImageId = page.pageImageId
            }
            pagesImagesIds += pageImageId
        }
        val response = loadWithState { chaptersRepository.updateChapter(
            bookId = bookId,
            chapterId = if(chapterId < 0) 0 else chapterId,
            chapterTitle = chapterTitle,
            chapterPagesIds = pagesIds,
            chapterPagesImageIds = pagesImagesIds
        ) }
        if (response is UiState.Success)
            chapterId = response.data?.chapterId ?: 0
        return response is UiState.Success
    }
}