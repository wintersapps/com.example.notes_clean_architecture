package com.example.notes.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.core.data.Note
import com.example.notes.databinding.ItemNoteBinding
import com.example.notes.presentation.utils.ListAction
import kotlin.collections.ArrayList

class NotesListAdapter(var notes: ArrayList<Note>, val actions: ListAction): RecyclerView.Adapter<NotesListAdapter.NoteViewHolder>() {

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
        holder.view.layoutCardView.setOnClickListener {
            actions.onClick(notes[position].id)
        }
    }

    override fun getItemCount(): Int = notes.size

    inner class NoteViewHolder(var view: ItemNoteBinding): RecyclerView.ViewHolder(view.root)
}