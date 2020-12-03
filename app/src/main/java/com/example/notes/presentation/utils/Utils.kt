package com.example.notes.presentation.utils

import androidx.databinding.BindingAdapter
import com.google.android.material.textview.MaterialTextView
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("android:convertToDate")
fun convertToDate(textView: MaterialTextView, updateTime: Long){
    val sdf = SimpleDateFormat("MMM dd, HH:mm:ss", Locale.getDefault())
    val resultDate = Date(updateTime)
    val dateToView = "Last update: ${sdf.format(resultDate)}"
    textView.text = dateToView
}

@BindingAdapter("android:wordsCount")
fun getWordsCount(textView: MaterialTextView, wordsCount: Int){
    val str = "Words: $wordsCount"
    textView.text = str
}