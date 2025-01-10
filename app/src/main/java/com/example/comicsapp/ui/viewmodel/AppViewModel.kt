package com.example.comicsapp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.comicsapp.domain.model.DomainError
import com.example.comicsapp.data.model.api.response.ApiError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// class AppViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
//     override fun <T : ViewModel> create(modelClass: Class<T>): T {
//         if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
//             return AppViewModel(SavedStateHandle(), context) as T
//         }
            //         throw IllegalArgumentException("Unknown ViewModel class")
                //     }
// }

class AppViewModelFactory(
    private val context: Context,
    private val owner: SavedStateRegistryOwner
) : AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            return AppViewModel(handle, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


class AppViewModel(private val state: SavedStateHandle, val context: Context) : ViewModel()  {

    private var _apiError = MutableStateFlow<DomainError?>( null )
    val apiError: StateFlow<DomainError?> get() = _apiError

    val bookCatalog =  BookCatalogViewModel()
    val selectedBook = SelectedBookViewModel(this)
    val editBook = EditBookViewModel(this)
    val currentUser = UserViewModel(state, context)
    val settings = SettingsViewModel(state, context)


    fun setApiError(err: DomainError?){
        _apiError.value = err
    }
    fun setApiErrorFromApi(err: ApiError?){
        val e = DomainError()
        e.setFromApi(err)
        _apiError.value = e
    }

}