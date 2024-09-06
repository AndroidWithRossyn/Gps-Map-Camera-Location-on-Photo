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

class BottomSheetDialogFragmentLatLongData: BottomSheetDialogFragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private var isDecimalSelected = false
    private var isDecimalDegsSelected = false
    private var isDecimalDegsMicroSelected = false
    private var isDecimalMinsSelected = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_layout_lat_long_types , container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("LatLongSelection", Context.MODE_PRIVATE)

        isDecimalSelected = sharedPreferences.getBoolean("LatLongSelectedDecimal", false)
        isDecimalDegsSelected = sharedPreferences.getBoolean("LatLongSelectedDecimalDegs", false)
        isDecimalDegsMicroSelected = sharedPreferences.getBoolean("LatLongSelectedDecimalDegsMicro", false)
        isDecimalMinsSelected = sharedPreferences.getBoolean("LatLongSelectedDecimalDegsMins", false)


        val decimalTv = view.findViewById<TextView>(R.id.decimal_tv)
        val decimalDegsTv = view.findViewById<TextView>(R.id.decimal_degs_tv)
        val decimalDegsMicroTv = view.findViewById<TextView>(R.id.decimal_degs_micro_tv)
        val decimalMinsTv = view.findViewById<TextView>(R.id.decimal_mins_tv)

        updateDrawable(decimalTv, isDecimalSelected)
        updateDrawable(decimalDegsTv, isDecimalDegsSelected)
        updateDrawable(decimalDegsMicroTv,isDecimalDegsMicroSelected)
        updateDrawable(decimalMinsTv, isDecimalMinsSelected)



        decimalTv.setOnClickListener {
            val parentActivity = activity as? TemplateActivity
            val selectedText = getString(R.string.lat_long_decimal)
            isDecimalSelected = true
            isDecimalDegsSelected = false
            isDecimalDegsMicroSelected = false
            isDecimalMinsSelected = false
            parentActivity?.updateLatLongText(selectedText)
            saveSelectedText(selectedText)
            saveSelectionStates("LatLongSelectedDecimal")
            updateSelection(getString(R.string.lat_long_decimal), "LatLongSelectedDecimal")
            updateDrawable(decimalTv, true)
            updateDrawable(decimalDegsTv, false)
            updateDrawable(decimalMinsTv, false)
            updateDrawable(decimalDegsMicroTv, false)
        }

        decimalDegsTv.setOnClickListener {
            val parentActivity = activity as? TemplateActivity
            val selectedText = getString(R.string.lat_long_tv_bottom_sheet_selection)
            isDecimalDegsSelected = true
            isDecimalSelected = false
            isDecimalMinsSelected = false
            isDecimalDegsMicroSelected = false
            parentActivity?.updateLatLongText(selectedText)
            saveSelectedText(selectedText)
            saveSelectionStates("LatLongSelectedDecimalDegs")
            updateSelection(getString(R.string.lat_long_tv_bottom_sheet_selection), "LatLongSelectedDecimalDegs")
            updateDrawable(decimalDegsTv, true)
            updateDrawable(decimalTv, false)
            updateDrawable(decimalMinsTv, false)
            updateDrawable(decimalDegsMicroTv, false)
        }

        decimalDegsMicroTv.setOnClickListener {
            val parentActivity = activity as? TemplateActivity
            val selectedText = getString(R.string.lat_long_tv_bottom_sheet_selection_decimal_degs_micro)
            isDecimalDegsMicroSelected = true
            isDecimalSelected = false
            isDecimalDegsSelected = false
            isDecimalMinsSelected = false
            parentActivity?.updateLatLongText(selectedText)
            saveSelectedText(selectedText)
            saveSelectionStates("LatLongSelectedDecimalDegsMicro")
            updateSelection(getString(R.string.lat_long_tv_bottom_sheet_selection_decimal_degs_micro), "LatLongSelectedDecimalDegsMicro")
            updateDrawable(decimalDegsMicroTv, true)
            updateDrawable(decimalTv, false)
            updateDrawable(decimalDegsTv, false)
            updateDrawable(decimalMinsTv, false)
        }

        decimalMinsTv.setOnClickListener {
            val parentActivity = activity as? TemplateActivity
            val selectedText = getString(R.string.lat_long_tv_bottom_sheet_selection_decimal_mins_tv)
            isDecimalMinsSelected = true
            isDecimalSelected = false
            isDecimalDegsSelected = false
            isDecimalDegsMicroSelected = false
            parentActivity?.updateLatLongText(selectedText)
            saveSelectedText(selectedText)
            saveSelectionStates("LatLongSelectedDecimalDegsMicro")
            updateSelection(getString(R.string.lat_long_tv_bottom_sheet_selection_decimal_mins_tv), "LatLongSelectedDecimalDegsMicro")
            updateDrawable(decimalMinsTv, true)
            updateDrawable(decimalTv, false)
            updateDrawable(decimalDegsTv, false)
            updateDrawable(decimalDegsMicroTv, false)
        }
    }

    private fun saveSelectedText(text: String) {
        with(sharedPreferences.edit()) {
            putString("selected_lat_long_text", text)
            apply()
        }
    }
    private fun updateSelection(selectedText: String, key: String) {
        val parentActivity = activity as? TemplateActivity
        parentActivity?.updateLatLongText(selectedText)
        saveSelectedText(selectedText)
        saveSelectionStates(key)
        (activity as? MainActivity)?.updateLatLongText(selectedText)
    }


    private fun saveSelectionStates(key: String) {
        with(sharedPreferences.edit()) {
            putBoolean("LatLongSelectedDecimal", key == "LatLongSelectedDecimal")
            putBoolean("LatLongSelectedDecimalDegs", key == "LatLongSelectedDecimalDegs")
            putBoolean("LatLongSelectedDecimalDegsMicro", key == "LatLongSelectedDecimalDegsMicro")
            putBoolean("LatLongSelectedDecimalDegsMins", key == "LatLongSelectedDecimalDegsMins")
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