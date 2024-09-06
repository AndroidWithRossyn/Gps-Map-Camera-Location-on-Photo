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

class BottomSheetDialogFragmentPressure : BottomSheetDialogFragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private var isZeroHpaSelected = false
    private var isZeroMmhgSelected = false
    private var isZeroInhgSelected = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_layout_pressure , container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("PressureSelection", Context.MODE_PRIVATE)

        isZeroHpaSelected = sharedPreferences.getBoolean("PressureSelectedRangeHpa", false)
        isZeroMmhgSelected = sharedPreferences.getBoolean("PressureSelectedRangeMmhg", false)
        isZeroInhgSelected = sharedPreferences.getBoolean("PressureSelectedRangeInhg", false)

        val zeroHpaTv = view.findViewById<TextView>(R.id.first_pressure_speed_tv)
        val zeroMmhgTv = view.findViewById<TextView>(R.id.second_pressure_speed_tv)
        val zeroInhgTv = view.findViewById<TextView>(R.id.third_pressure_speed_tv)

        updateDrawable(zeroHpaTv, isZeroHpaSelected)
        updateDrawable(zeroMmhgTv, isZeroMmhgSelected)
        updateDrawable(zeroInhgTv, isZeroInhgSelected)

        zeroHpaTv.setOnClickListener {
            val parentActivity = activity as? TemplateActivity
            val selectedText = getString(R.string.first_pressure_speed_tv)
            isZeroHpaSelected = true
            isZeroMmhgSelected = false
            isZeroInhgSelected = false
            parentActivity?.updatePressureText(selectedText)
            saveSelectedText(selectedText)
            saveSelectionStates("PressureSelectedRangeHpa")
            updateSelection(getString(R.string.first_pressure_speed_tv), "PressureSelectedRangeHpa")
            updateDrawable(zeroHpaTv, true)
            updateDrawable(zeroMmhgTv, false)
            updateDrawable(zeroInhgTv, false)
        }

        zeroMmhgTv.setOnClickListener {
            val parentActivity = activity as? TemplateActivity
            val selectedText = getString(R.string.second_pressure_speed_tv)
            isZeroHpaSelected = false
            isZeroMmhgSelected = true
            isZeroInhgSelected = false
            parentActivity?.updatePressureText(selectedText)
            saveSelectedText(selectedText)
            saveSelectionStates("PressureSelectedRangeMmhg")
            updateSelection(getString(R.string.second_pressure_speed_tv), "PressureSelectedRangeMmhg")
            updateDrawable(zeroHpaTv, false)
            updateDrawable(zeroMmhgTv, true)
            updateDrawable(zeroInhgTv, false)
        }

        zeroInhgTv.setOnClickListener {
            val parentActivity = activity as? TemplateActivity
            val selectedText = getString(R.string.third_pressure_speed_tv)
            isZeroHpaSelected = false
            isZeroMmhgSelected = false
            isZeroInhgSelected = true
            parentActivity?.updatePressureText(selectedText)
            saveSelectedText(selectedText)
            saveSelectionStates("PressureSelectedRangeInhg")
            updateSelection(getString(R.string.third_pressure_speed_tv), "PressureSelectedRangeInhg")
            updateDrawable(zeroHpaTv, false)
            updateDrawable(zeroMmhgTv, false)
            updateDrawable(zeroInhgTv, true)
        }


    }

    private fun saveSelectedText(text: String) {
        with(sharedPreferences.edit()) {
            putString("selected_pressure_text", text)
            apply()
        }
    }

    private fun saveSelectionStates(key: String) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("PressureSelectedRangeHpa", key == "PressureSelectedRangeHpa")
        editor.putBoolean("PressureSelectedRangeMmhg", key == "PressureSelectedRangeMmhg")
        editor.putBoolean("PressureSelectedRangeInhg", key == "PressureSelectedRangeInhg")
        editor.apply()
    }

    private fun updateSelection(selectedText: String, key: String) {
        val parentActivity = activity as? TemplateActivity
        parentActivity?.updatePressureText(selectedText)
        saveSelectedText(selectedText)
        saveSelectionStates(key)
        (activity as? MainActivity)?.updatePressureText(selectedText)
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