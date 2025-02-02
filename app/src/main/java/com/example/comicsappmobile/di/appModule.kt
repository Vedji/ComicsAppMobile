package com.example.comicsappmobile.di

import com.example.comicsappmobile.data.api.BooksApi
import com.example.comicsappmobile.data.api.CommentsApi
import com.example.comicsappmobile.data.api.FilesApi
import com.example.comicsappmobile.data.api.GenresApi
import com.example.comicsappmobile.data.repository.BooksRepository
import com.example.comicsappmobile.data.repository.ChaptersRepository
import com.example.comicsappmobile.data.repository.CommentsRepository
import com.example.comicsappmobile.data.repository.FavoriteRepository
import com.example.comicsappmobile.data.repository.FilesRepository
import com.example.comicsappmobile.data.repository.GenresRepository
import com.example.comicsappmobile.data.repository.PagesRepository
import com.example.comicsappmobile.data.repository.UserRepository
import com.example.comicsappmobile.ui.presentation.viewmodel.BookEditorViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.BookViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.CatalogViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.ChapterEditorViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.LoginFormViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.PagesViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.ProfileEditorViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.ProfileViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.RegistrationFormViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.SettingsViewModel
import com.example.comicsappmobile.utils.remote.provideBookApiService
import com.example.comicsappmobile.utils.remote.provideGenresApiService
import com.example.comicsappmobile.utils.remote.provideMoshi
import com.example.comicsappmobile.utils.remote.provideOkHttpClient
import com.example.comicsappmobile.utils.remote.provideRetrofit
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single {
        GlobalState(
            isOpenBottomSheet = true,
            bottomSheetSkipPartiallyExpanded = false,
            androidContext(),
        )
    }

    single { SharedViewModel() }
    single { provideMoshi() }
    single { provideOkHttpClient() }
    single { provideRetrofit(get(), get()) }

    // Apis
    single<GenresApi> { provideGenresApiService(get()) }
    single<BooksApi> { provideBookApiService(get()) }
    single<CommentsApi> { RetrofitInstance.commentsApi }
    single<FilesApi> { RetrofitInstance.filesApi }


    // Repositories
    single { BooksRepository(booksApi = RetrofitInstance.bookApi) }
    single { GenresRepository(genreApi = RetrofitInstance.genresApi) }
    single { ChaptersRepository(chaptersApi = RetrofitInstance.chaptersApi) }
    single { CommentsRepository(commentsApi = RetrofitInstance.commentsApi) }
    single { PagesRepository(pagesApi = RetrofitInstance.pagesApi) }
    single { UserRepository(userApi = RetrofitInstance.userApi, sharedViewModel = get(), globalState = get()) }
    single { FavoriteRepository(favoriteApi = RetrofitInstance.favoriteApi, globalState = get()) }
    single { FilesRepository(filesApi = RetrofitInstance.filesApi, globalState = get()) }



    // ViewModels
    factory { (bookId: Int) -> BookViewModel(
        bookId = bookId,
        booksRepository = get(),
        genresRepository = get(),
        chaptersRepository = get(),
        commentsRepository = get(),
        sharedViewModel = get(),
        userRepository = get(),
        favoriteRepository = get(),
        globalState = get()
    ) }

    single { CatalogViewModel(
            booksRepository = get(),
            genresRepository = get(),
            favoriteRepository = get()
    ) }

    factory { (bookId: Int, chapterId: Int) -> PagesViewModel(
        bookId = bookId,
        chapterId = chapterId,
        pagesRepository = get(),
        chaptersRepository = get(),
        booksRepository = get(),
        favoriteRepository = get(),
        sharedViewModel = get(),
        globalState = get()
    ) }

    factory { LoginFormViewModel(
            userRepository = get(),
            sharedViewModel = get(),
            globalState = get()
    ) }

    factory { ProfileViewModel(
        userRepository = get(),
        booksRepository = get(),
        globalState = get()
    ) }

    factory { ProfileEditorViewModel(
        userRepository = get(),
        filesRepository = get(),
        globalState = get()
    ) }

    factory { SettingsViewModel(
            userRepository = get(),
            globalState = get()
    ) }

    factory { (bookId: Int) -> BookEditorViewModel(
        bookId = bookId,
        booksRepository = get(),
        genresRepository = get(),
        chaptersRepository = get(),
        filesRepository = get(),
        globalState = get()
    ) }

    factory { (bookId: Int, chapterId: Int) -> ChapterEditorViewModel(
        bookId = bookId,
        chapterId = chapterId,
        chaptersRepository = get(),
        pagesRepository = get(),
        filesRepository = get(),
        globalState = get()
    ) }

    factory { RegistrationFormViewModel(
        userRepository = get(),
        globalState = get()
    ) }

}