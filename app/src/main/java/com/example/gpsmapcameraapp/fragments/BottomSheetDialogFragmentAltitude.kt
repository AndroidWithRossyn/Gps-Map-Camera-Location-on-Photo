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

class BottomSheetDialogFragmentAltitude : BottomSheetDialogFragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private var isFirstHeightSelected = false
    private var isSecondHeightSelected = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_layout_altitude , container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("AltitudeSelection", Context.MODE_PRIVATE)

        isFirstHeightSelected = sharedPreferences.getBoolean("AltitudeSelectedRangeM", false)
        isSecondHeightSelected = sharedPreferences.getBoolean("AltitudeSelectedRangeFt", false)


        val firstHeightTv = view.findViewById<TextView>(R.id.first_altitude_tv)
        val secondHeightTv = view.findViewById<TextView>(R.id.second_altitude_tv)

        updateDrawable(firstHeightTv, isFirstHeightSelected)
        updateDrawable(secondHeightTv, isSecondHeightSelected)

        firstHeightTv.setOnClickListener {
            val parentActivity = activity as? TemplateActivity
            val selectedText = getString(R.string.first_altitude_tv)
            isFirstHeightSelected = true
            isSecondHeightSelected = false
            parentActivity?.updateAltitudeText(selectedText)
            saveSelectedText(selectedText)
            saveSelectionStates("AltitudeSelectedRangeM")
            updateSelection(getString(R.string.first_altitude_tv), "AltitudeSelectedRangeM")
            updateDrawable(firstHeightTv, true)
            updateDrawable(secondHeightTv, false)
        }

        secondHeightTv.setOnClickListener {
            val parentActivity = activity as? TemplateActivity
            val selectedText = getString(R.string.second_altitude_tv)
            isFirstHeightSelected = false
            isSecondHeightSelected = true
            parentActivity?.updateAltitudeText(selectedText)
            saveSelectionStates("AltitudeSelectedRangeFt")
            updateSelection(getString(R.string.second_altitude_tv), "AltitudeSelectedRangeFt")
            updateDrawable(secondHeightTv, true)
            updateDrawable(firstHeightTv, false)
        }

    }

    private fun saveSelectedText(text: String) {
        with(sharedPreferences.edit()) {
            putString("selected_altitude_text", text)
            apply()
        }
    }

    private fun saveSelectionStates(key: String) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("AltitudeSelectedRangeM", key == "AltitudeSelectedRangeM" )
        editor.putBoolean("AltitudeSelectedRangeFt", key == "AltitudeSelectedRangeFt")
        editor.apply()
    }

    private fun updateSelection(selectedText: String, key: String) {
        val parentActivity = activity as? TemplateActivity
        parentActivity?.updateAltitudeText(selectedText)
        saveSelectedText(selectedText)
        saveSelectionStates(key)
        (activity as? MainActivity)?.updateAltitudeText(selectedText)
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