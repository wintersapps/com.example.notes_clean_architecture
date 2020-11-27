package com.example.notes.framework

import android.content.Context
import com.example.core.data.Note
import com.example.core.repository.NoteDataSource
import com.example.notes.framework.db.DatabaseService
import com.example.notes.framework.db.NoteEntity

class RoomNoteDataSource(context: Context): NoteDataSource {

    val noteDao = DatabaseService.getInstance(context).noteDao()

    override suspend fun add(note: Note) = noteDao.addNoteEntity(NoteEntity.fromNote(note))

    override suspend fun get(id: Long) = noteDao.getNoteEntity(id)?.toNote()

    override suspend fun getAll() = noteDao.getAllNoteEntities().map { it.toNote() }

    override suspend fun remove(note: Note) = noteDao.deleteNoteEntity(NoteEntity.fromNote(note))
}