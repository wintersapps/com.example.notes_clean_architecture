package com.example.notes.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes.R
import com.example.notes.databinding.FragmentListBinding
import com.example.notes.framework.viewmodel.ListViewModel
import com.example.notes.presentation.adapters.NotesListAdapter

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListViewModel by viewModels()
    private val notesListAdapter = NotesListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.notesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = notesListAdapter
        }

        setListeners()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.getNotes()
    }

    private fun setListeners(){
        binding.addNoteFab.setOnClickListener { goToNoteDetails() }
    }

    private fun observeViewModel(){
        viewModel.notes.observe(viewLifecycleOwner, { notes ->
            notes?.let {
                if(notes.isNotEmpty()){
                    binding.loadingErrorMessageTextView.visibility = View.GONE
                    binding.loadingNotesProgressBar.visibility = View.GONE
                    binding.notesRecyclerView.visibility = View.VISIBLE
                    notesListAdapter.updateNotes(notes.sortedByDescending { it.updateTime })
                }else{
                    binding.notesRecyclerView.visibility = View.GONE
                    binding.loadingNotesProgressBar.visibility = View.GONE
                    binding.loadingErrorMessageTextView.text = getString(R.string.you_have_no_notes)
                    binding.loadingErrorMessageTextView.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun goToNoteDetails(id: Long = 0L){
        val action = ListFragmentDirections.actionGoToNote(id)
        Navigation.findNavController(binding.addNoteFab).navigate(action)
    }
}