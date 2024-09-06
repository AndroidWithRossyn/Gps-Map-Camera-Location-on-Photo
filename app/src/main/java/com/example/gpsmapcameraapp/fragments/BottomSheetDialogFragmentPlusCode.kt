package com.example.gpsmapcameraapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.gpsmapcameraapp.MainActivity
import com.example.gpsmapcameraapp.R
import com.example.gpsmapcameraapp.TemplateActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialogFragmentPlusCode : BottomSheetDialogFragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private var isAccurateSelected = false
    private var isConciseSelected = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_layout_plus_code , container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("PLusCodeSelection", Context.MODE_PRIVATE)

        isAccurateSelected = sharedPreferences.getBoolean("PlusCodeSelectedAccurate", false)
        isConciseSelected = sharedPreferences.getBoolean("PLusCodeSelectedConcise", false)


        val accurateTv = view.findViewById<TextView>(R.id.accurate_tv_bottom_sheet)
        val conciseTv = view.findViewById<TextView>(R.id.concise_tv_bottom_sheet)

        updateDrawable(accurateTv, isAccurateSelected)
        updateDrawable(conciseTv, isConciseSelected)

        accurateTv.setOnClickListener {
            val parentActivity = activity as? TemplateActivity
            val selectedText = getString(R.string.plus_code_accuracy_option_8J5MG485HR_sheet)
            isAccurateSelected = true
            isConciseSelected = false
            parentActivity?.updatePlusCodeText(selectedText)
            saveSelectedText(selectedText)
            saveSelectionStates("PlusCodeSelectedAccurate")
            updateSelection(getString(R.string.plus_code_accuracy_option_8J5MG485HR_sheet), "PlusCodeSelectedAccurate")
            updateDrawable(accurateTv, true)
            updateDrawable(conciseTv, false)
        }
        conciseTv.setOnClickListener {
            val parentActivity = activity as? TemplateActivity
            val selectedText = getString(R.string.concise_option_tv_bottom_sheet)
            isAccurateSelected = false
            isConciseSelected = true
            parentActivity?.updatePlusCodeText(selectedText)
            saveSelectedText(selectedText)
            saveSelectionStates("PlusCodeSelectedConcise")
            updateSelection(getString(R.string.concise_option_tv_bottom_sheet), "PlusCodeSelectedConcise")
            updateDrawable(accurateTv, false)
            updateDrawable(conciseTv, true)
        }

    }

    private fun updateSelection(selectedText: String, key: String) {
        val parentActivity = activity as? TemplateActivity
        parentActivity?.updatePlusCodeText(selectedText)
        saveSelectedText(selectedText)
        saveSelectionStates(key)
        (activity as? MainActivity)?.updatePlusCodeText(selectedText)
    }

    private fun saveSelectedText(text: String) {
        with(sharedPreferences.edit()) {
            putString("selected_plus_code_text", text)
            apply()
        }
    }

    private fun saveSelectionStates(key: String) {
        with(sharedPreferences.edit()) {
            putBoolean("PlusCodeSelectedAccurate", key == "PlusCodeSelectedAccurate")
            putBoolean("PlusCodeSelectedConcise", key == "PlusCodeSelectedConcise")
            apply()
        }
    }
    private fun updateDrawable(textView: TextView, selected: Boolean) {
        val originalDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.tick_map_data_manual_icon)
        val drawable = if (selected && originalDrawable != null) {
            val scaledDrawable = BitmapDrawable(resources, Bitmap.createScaledBitmap((originalDrawable as BitmapDrawable).bitmap, 25, 25, true))
            scaledDrawable.setBounds(0, 0, scaledDrawable.intrinsicWidth, scaledDrawable.intrinsicHeight)
            scaledDrawable
        } else {
            null
        }

        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
    }

}