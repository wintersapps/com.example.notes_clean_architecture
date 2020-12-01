package com.example.notes.framework.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.core.data.Note
import com.example.core.repository.NoteRepository
import com.example.core.usecase.AddNote
import com.example.core.usecase.GetAllNotes
import com.example.core.usecase.GetNote
import com.example.core.usecase.RemoveNote
import com.example.notes.framework.RoomNoteDataSource
import com.example.notes.framework.UseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListViewModel(application: Application): AndroidViewModel(application) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val repository = NoteRepository(RoomNoteDataSource(application))

    private val useCases = UseCases(
        AddNote(repository),
        GetAllNotes(repository),
        GetNote(repository),
        RemoveNote(repository)
    )

    val notes by lazy { MutableLiveData<List<Note>>() }

    fun getNotes(){
        coroutineScope.launch {
            val noteList = useCases.getAllNotes()
            notes.postValue(noteList)
        }
    }
}