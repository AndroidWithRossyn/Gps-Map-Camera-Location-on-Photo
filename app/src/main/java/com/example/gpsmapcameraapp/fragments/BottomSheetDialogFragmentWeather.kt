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

class BottomSheetDialogFragmentWeather : BottomSheetDialogFragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private var isZeroZeroDegreeSelected = false
    private var isThirtyTwoDegreeSelected = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_layout_weather , container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("WeatherSelection", Context.MODE_PRIVATE)

        isZeroZeroDegreeSelected = sharedPreferences.getBoolean("WeatherSelectedCelsius", false)
        isThirtyTwoDegreeSelected = sharedPreferences.getBoolean("WeatherSelectedFahrenheit", false)

        val zeroZeroDegreeTv = view.findViewById<TextView>(R.id.value_in_celsius_tv_bottom_sheet)
        val thirtyTwoDegreeTv = view.findViewById<TextView>(R.id.fahrenheit_value_tv_bottom_sheet)

        updateDrawable(zeroZeroDegreeTv, isZeroZeroDegreeSelected)
        updateDrawable(thirtyTwoDegreeTv, isThirtyTwoDegreeSelected)

        zeroZeroDegreeTv.setOnClickListener {
            val parentActivity = activity as? TemplateActivity
            val selectedText = getString(R.string.value_in_celsius_tv_bottom_sheet)
            isZeroZeroDegreeSelected = true
            isThirtyTwoDegreeSelected = false
            parentActivity?.updateWeatherText(selectedText)
            saveSelectedText(selectedText)
            saveSelectionStates("WeatherSelectedCelsius")
            updateSelection(getString(R.string.value_in_celsius_tv_bottom_sheet), "WeatherSelectedCelsius")
            updateDrawable(zeroZeroDegreeTv, true)
            updateDrawable(thirtyTwoDegreeTv, false)
        }

        thirtyTwoDegreeTv.setOnClickListener {
            val parentActivity = activity as? TemplateActivity
            val selectedText = getString(R.string.fahrenheit_value_tv_bottom_sheet)
            isZeroZeroDegreeSelected = false
            isThirtyTwoDegreeSelected = true
            parentActivity?.updateWeatherText(selectedText)
            saveSelectedText(selectedText)
            saveSelectionStates( "WeatherSelectedFahrenheit")
            updateSelection(getString(R.string.fahrenheit_value_tv_bottom_sheet), "WeatherSelectedFahrenheit")
            updateDrawable(zeroZeroDegreeTv, false)
            updateDrawable(thirtyTwoDegreeTv, true)
        }



    }

    private fun saveSelectedText(text: String) {
        with(sharedPreferences.edit()) {
            putString("selected_weather_text", text)
            apply()
        }
    }

    private fun saveSelectionStates(key: String) {
        with(sharedPreferences.edit()) {
            putBoolean("WeatherSelectedCelsius", key == "WeatherSelectedCelsius")
            putBoolean("WeatherSelectedFahrenheit", key == "WeatherSelectedFahrenheit")
            apply()
        }
    }

    private fun updateSelection(selectedText: String, key: String) {
        val parentActivity = activity as? TemplateActivity
        parentActivity?.updateWeatherText(selectedText)
        saveSelectedText(selectedText)
        saveSelectionStates(key)
        (activity as? MainActivity)?.updateWeatherText(selectedText)
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