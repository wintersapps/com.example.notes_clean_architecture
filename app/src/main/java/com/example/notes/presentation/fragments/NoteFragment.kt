package com.example.notes.presentation.fragments

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.core.data.Note
import com.example.notes.R
import com.example.notes.databinding.FragmentNoteBinding
import com.example.notes.framework.viewmodel.NoteViewModel

class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NoteViewModel by viewModels()
    private var currentNote = Note("", "", 0L, 0L)
    private var noteId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            noteId = NoteFragmentArgs.fromBundle(it).noteId
        }
        if(noteId != 0L){
            viewModel.getNote(noteId)
        }

        setListeners()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_delete_note -> {
                if(noteId != 0L){
                    AlertDialog.Builder(binding.root.context)
                        .setTitle(getString(R.string.delete_a_note))
                        .setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_note))
                        .setPositiveButton(getString(R.string.yes)) { _, _ ->
                            viewModel.deleteNote(currentNote)
                        }
                        .setNegativeButton(getString(R.string.Cancel)) { _, _ ->}
                        .create()
                        .show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setListeners() {

        binding.titleEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if(binding.titleEditText.text.isNullOrEmpty()){
                    binding.titleTextLayout.error = getString(R.string.this_field_can_not_be_empty)
                }else{
                    binding.titleTextLayout.error = null
                }
            }
        })

        binding.contentEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if(binding.contentEditText.text.isNullOrEmpty()){
                    binding.contentTextLayout.error = getString(R.string.this_field_can_not_be_empty)
                }else{
                    binding.contentTextLayout.error = null
                }
            }
        })

        binding.saveNoteFab.setOnClickListener {
            if(areFieldsValid()){
                val time = System.currentTimeMillis()
                currentNote.title = binding.titleEditText.text.toString()
                currentNote.content = binding.contentEditText.text.toString()
                currentNote.updateTime = time
                if(currentNote.id == 0L){
                    currentNote.creationTime = time
                }
                viewModel.saveNote(currentNote)
            }else{
                Toast.makeText(context, getString(R.string.correct_the_fields_with_errors), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel(){
        viewModel.saved.observe(viewLifecycleOwner, { saved ->
            saved?.let {
                if(it){
                    Toast.makeText(context, getString(R.string.done), Toast.LENGTH_SHORT).show()
                    hideKeyboard()
                    Navigation.findNavController(binding.saveNoteFab).popBackStack()
                }else{
                    Toast.makeText(context, getString(R.string.something_has_gone_wrong_please_try_again), Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.currentNote.observe(viewLifecycleOwner, { note ->
            note?.let {
                currentNote = it
                binding.note = currentNote
            }
        })
    }

    private fun areFieldsValid(): Boolean{
        var areValid = true

        if(binding.titleEditText.text.isNullOrEmpty()){
            binding.titleTextLayout.error = getString(R.string.this_field_can_not_be_empty)
            areValid = false
        }
        if (binding.contentEditText.text.isNullOrEmpty()){
            binding.contentTextLayout.error = getString(R.string.this_field_can_not_be_empty)
            areValid = false
        }

        return areValid
    }

    private fun hideKeyboard(){
        val inputMethodManager = binding.root.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}