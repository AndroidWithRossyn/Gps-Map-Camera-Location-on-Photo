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

class BottomSheetDialogFragmentAccuracy : BottomSheetDialogFragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private var isFirstTargetSelected = false
    private var isSecondTargetSelected = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_layout_accuracy , container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("AccuracySelection", Context.MODE_PRIVATE)

        isFirstTargetSelected = sharedPreferences.getBoolean("AccuracySelectedTargetM", false)
        isSecondTargetSelected = sharedPreferences.getBoolean("AccuracySelectedTargetFt", false)

        val firstHeightTv = view.findViewById<TextView>(R.id.first_accuracy_tv)
        val secondHeightTv = view.findViewById<TextView>(R.id.second_accuracy_tv)

        updateDrawable(firstHeightTv, isFirstTargetSelected)
        updateDrawable(secondHeightTv, isSecondTargetSelected)

        firstHeightTv.setOnClickListener {
            val parentActivity = activity as? TemplateActivity
            val selectedText = getString(R.string.first_accuracy_tv)
            isFirstTargetSelected = true
            isSecondTargetSelected = false
            parentActivity?.updateAccuracyText(selectedText)
            saveSelectedText(selectedText)
            updateSelection(getString(R.string.first_accuracy_tv), "AccuracySelectedTargetM")
            saveSelectionStates("AccuracySelectedTargetM")
            updateDrawable(firstHeightTv, true)
            updateDrawable(secondHeightTv, false)
        }

        secondHeightTv.setOnClickListener {
            val parentActivity = activity as? TemplateActivity
            val selectedText = getString(R.string.second_accuracy_tv)
            isFirstTargetSelected = false
            isSecondTargetSelected = true
            parentActivity?.updateAccuracyText(selectedText)
            saveSelectedText(selectedText)
            saveSelectionStates("AccuracySelectedTargetFt")
            updateSelection(getString(R.string.second_accuracy_tv), "AccuracySelectedTargetFt")
            updateDrawable(firstHeightTv, false)
            updateDrawable(secondHeightTv, true)
        }

    }
    private fun saveSelectedText(text: String) {
        with(sharedPreferences.edit()) {
            putString("selected_accuracy_text", text)
            apply()
        }
    }

    private fun saveSelectionStates(key: String) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("AccuracySelectedTargetM", key == "AccuracySelectedTargetM")
        editor.putBoolean("AccuracySelectedTargetFt", key == "AccuracySelectedTargetFt")
        editor.apply()
    }

    private fun updateSelection(selectedText: String, key: String) {
        val parentActivity = activity as? TemplateActivity
        parentActivity?.updateAccuracyText(selectedText)
        saveSelectedText(selectedText)
        saveSelectionStates(key)
        (activity as? MainActivity)?.updateAccuracyText(selectedText)
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