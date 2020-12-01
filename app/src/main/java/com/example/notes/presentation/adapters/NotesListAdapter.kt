package com.example.notes.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.core.data.Note
import com.example.notes.databinding.ItemNoteBinding
import kotlin.collections.ArrayList

class NotesListAdapter(var notes: ArrayList<Note>): RecyclerView.Adapter<NotesListAdapter.NoteViewHolder>() {

    fun updateNotes(newNotes: List<Note>){
        notes.clear()
        notes.addAll(newNotes)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NoteViewHolder(
        ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.view.note = notes[position]
    }

    override fun getItemCount(): Int = notes.size

    inner class NoteViewHolder(var view: ItemNoteBinding): RecyclerView.ViewHolder(view.root)
}