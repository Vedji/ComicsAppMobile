package com.example.comicsapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.comicsapp.data.repository.BookRepository

abstract class BaseBookViewModel(protected val repository: BookRepository) : ViewModel() {




}