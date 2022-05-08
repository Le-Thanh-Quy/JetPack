package com.quy.mynote.database

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MyViewModelFactory constructor(val application: Application): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(NoteViewModel::class.java!!)) {
            NoteViewModel(application = application) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}