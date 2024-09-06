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

class BottomSheetDialogFragmentWindData : BottomSheetDialogFragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private var isZeroKilometerSelected = false
    private var isZeroMpSelected = false
    private var isZeroMsSelected = false
    private var isZeroKtSelected = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_layout_wind , container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        sharedPreferences = requireContext().getSharedPreferences("WindSelection", Context.MODE_PRIVATE)

        isZeroKilometerSelected = sharedPreferences.getBoolean("WindSelectedRangeKilometer", false)
        isZeroMsSelected = sharedPreferences.getBoolean("WindSelectedRangeMs", false)
        isZeroMpSelected = sharedPreferences.getBoolean("WindSelectedRangeMp", false)
        isZeroKtSelected = sharedPreferences.getBoolean("WindSelectedRangeKt", false)

        val zeroKilometerTv = view.findViewById<TextView>(R.id.first_wind_speed_tv)
        val zeroMpTv = view.findViewById<TextView>(R.id.second_wind_speed_tv)
        val zeroMsTv = view.findViewById<TextView>(R.id.third_wind_speed_tv)
        val zeroKtTv = view.findViewById<TextView>(R.id.fourth_wind_speed_tv)

        updateDrawable(zeroKilometerTv, isZeroKilometerSelected)
        updateDrawable(zeroMpTv, isZeroMpSelected)
        updateDrawable(zeroMsTv, isZeroMsSelected)
        updateDrawable(zeroKtTv, isZeroKtSelected)


        zeroKilometerTv.setOnClickListener {
            val parentActivity = activity as? TemplateActivity
            val selectedText = getString(R.string.first_wind_speed_tv)
            isZeroKilometerSelected = true
            isZeroKtSelected = false
            isZeroMpSelected = false
            isZeroMsSelected = false
            parentActivity?.updateWindText(selectedText)
            saveSelectedText(selectedText)
            saveSelectionStates("WindSelectedRangeKilometer")
            updateSelection(getString(R.string.first_wind_speed_tv), "WindSelectedRangeKilometer")
            updateDrawable(zeroKilometerTv, true)
            updateDrawable(zeroMpTv, false)
            updateDrawable(zeroKtTv, false)
            updateDrawable(zeroMsTv, false)
        }

        zeroMpTv.setOnClickListener {
            val parentActivity = activity as? TemplateActivity
            val selectedText = getString(R.string.second_wind_speed_tv)
            isZeroMpSelected = true
            isZeroKtSelected = false
            isZeroMsSelected  = false
            isZeroKilometerSelected = false
            parentActivity?.updateWindText(selectedText)
            saveSelectedText(selectedText)
            saveSelectionStates("WindSelectedRangeMp")
            updateDrawable(zeroKilometerTv, false)
            updateSelection(getString(R.string.second_wind_speed_tv), "WindSelectedRangeMp")
            updateDrawable(zeroKtTv, false)
            updateDrawable(zeroMpTv, true)
            updateDrawable(zeroMsTv, false)
        }
        zeroMsTv.setOnClickListener {
            val parentActivity = activity as? TemplateActivity
            val selectedText = getString(R.string.third_wind_speed_tv)
            isZeroMsSelected = true
            isZeroKtSelected = false
            isZeroMpSelected = false
            isZeroKilometerSelected = false
            parentActivity?.updateWindText(selectedText)
            saveSelectedText(selectedText)
            saveSelectionStates("WindSelectedRangeMs")
            updateSelection(getString(R.string.third_wind_speed_tv), "WindSelectedRangeMs")
            updateDrawable(zeroKilometerTv, false)
            updateDrawable(zeroMsTv, true)
            updateDrawable(zeroMpTv, false)
            updateDrawable(zeroKtTv, false)
        }
        zeroKtTv.setOnClickListener {
            val parentActivity = activity as? TemplateActivity
            val selectedText = getString(R.string.fourth_wind_speed_tv)
            isZeroKtSelected = true
            isZeroMsSelected = false
            isZeroMpSelected = false
            isZeroKilometerSelected = false
            parentActivity?.updateWindText(selectedText)
            saveSelectedText(selectedText)
            saveSelectionStates("WindSelectedRangeKt")
            updateSelection(getString(R.string.fourth_wind_speed_tv), "WindSelectedRangeKt")
            updateDrawable(zeroKtTv, true)
            updateDrawable(zeroKilometerTv, false)
            updateDrawable(zeroMpTv, false)
            updateDrawable(zeroMsTv, false)
        }

    }

    private fun saveSelectedText(text: String) {
        with(sharedPreferences.edit()) {
            putString("selected_wind_text", text)
            apply()
        }
    }

    private fun saveSelectionStates(key: String) {
        with(sharedPreferences.edit()) {
            putBoolean("WindSelectedRangeKilometer", key == "WindSelectedRangeKilometer")
            putBoolean("WindSelectedRangeMp", key == "WindSelectedRangeMp")
            putBoolean("WindSelectedRangeMs", key == "WindSelectedRangeMs")
            putBoolean("WindSelectedRangeKt", key == "WindSelectedRangeKt")
           apply()
        }
    }

    private fun updateSelection(selectedText: String, key: String) {
        val parentActivity = activity as? TemplateActivity
        parentActivity?.updateWindText(selectedText)
        saveSelectedText(selectedText)
        saveSelectionStates(key)
        (activity as? MainActivity)?.updateWindText(selectedText)
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