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

class BottomSheetDialogFragmentTimeZone  : BottomSheetDialogFragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private var isUtcSelected = false
    private var isGmtSelected = false
    private var isPstSelected = false
    private var isFiveSelected = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_layout_time_zone , container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("TimeZoneSelection", Context.MODE_PRIVATE)

        isUtcSelected = sharedPreferences.getBoolean("TimeZoneSelectedUtc", false)
        isGmtSelected = sharedPreferences.getBoolean("TimeZoneSelectedGmt", false)
        isPstSelected = sharedPreferences.getBoolean("TimeZoneSelectedPst", false)
        isFiveSelected = sharedPreferences.getBoolean("TimeZoneSelectedFive", false)

        val utcTv = view.findViewById<TextView>(R.id.utc_tv_bottom_sheet)
        val gmtTv = view.findViewById<TextView>(R.id.gmt_tv_bottom_sheet)
        val pstTv = view.findViewById<TextView>(R.id.pakistan_standard_time_tv_bottom_sheet)
        val fiveTv = view.findViewById<TextView>(R.id.plus_five_zero_zero_tv_bottom_sheet)

        updateDrawable(utcTv, isUtcSelected)
        updateDrawable(gmtTv, isGmtSelected)
        updateDrawable(pstTv, isPstSelected)
        updateDrawable(fiveTv, isFiveSelected)


        utcTv.setOnClickListener {
            val parentActivity = activity as? TemplateActivity
            val selectedText = getString(R.string.utc_tv_bottom_sheet)
            isUtcSelected = true
            isGmtSelected = false
            isPstSelected = false
            isFiveSelected = false
            parentActivity?.updateTimeZoneText(selectedText)
            saveSelectedText(selectedText)
            saveSelectionStates("TimeZoneSelectedUtc")
            updateSelection(getString(R.string.utc_tv_bottom_sheet), "TimeZoneSelectedUtc")
            updateDrawable(utcTv, true)
            updateDrawable(gmtTv, false)
            updateDrawable(pstTv, false)
            updateDrawable(fiveTv, false)
        }

        gmtTv.setOnClickListener {
            val parentActivity = activity as? TemplateActivity
            val selectedText = getString(R.string.gmt_tv_bottom_sheet)
            isUtcSelected = false
            isGmtSelected = true
            isPstSelected = false
            isFiveSelected = false
            parentActivity?.updateTimeZoneText(selectedText)
            saveSelectedText(selectedText)
            saveSelectionStates("TimeZoneSelectedGmt")
            updateSelection(getString(R.string.gmt_tv_bottom_sheet), "TimeZoneSelectedGmt")
            updateDrawable(utcTv, false)
            updateDrawable(gmtTv, true)
            updateDrawable(pstTv, false)
            updateDrawable(fiveTv, false)

        }

        pstTv.setOnClickListener {
            val parentActivity = activity as? TemplateActivity
            val selectedText = getString(R.string.pakistan_standard_time_tv_bottom_sheet)
            isUtcSelected = false
            isGmtSelected = false
            isPstSelected = true
            isFiveSelected = false
            parentActivity?.updateTimeZoneText(selectedText)
            saveSelectedText(selectedText)
            saveSelectionStates("TimeZoneSelectedPst")
            updateSelection(getString(R.string.pakistan_standard_time_tv_bottom_sheet), "TimeZoneSelectedPst")
            updateDrawable(utcTv, false)
            updateDrawable(gmtTv, false)
            updateDrawable(pstTv, true)
            updateDrawable(fiveTv, false)

        }

        fiveTv.setOnClickListener {
            val parentActivity = activity as? TemplateActivity
            val selectedText = getString(R.string.plus_five_zero_zero_tv_bottom_sheet)
            isUtcSelected = false
            isGmtSelected = false
            isPstSelected = false
            isFiveSelected = true
            parentActivity?.updateTimeZoneText(selectedText)
            saveSelectedText(selectedText)
            saveSelectionStates("TimeZoneSelectedFive")
            updateSelection(getString(R.string.plus_five_zero_zero_tv_bottom_sheet) ,"TimeZoneSelectedFive")
            updateDrawable(utcTv, false)
            updateDrawable(gmtTv, false)
            updateDrawable(pstTv, false)
            updateDrawable(fiveTv, true)

        }

    }

    private fun saveSelectedText(text: String) {
        with(sharedPreferences.edit()) {
            putString("selected_time_zone_text", text)
            apply()
        }
    }

    private fun updateSelection(selectedText: String, key: String) {
        val parentActivity = activity as? TemplateActivity
        parentActivity?.updatePlusCodeText(selectedText)
        saveSelectedText(selectedText)
        saveSelectionStates(key)
        (activity as? MainActivity)?.updateTimeZoneText(selectedText)
    }

    private fun saveSelectionStates(key: String) {
        with(sharedPreferences.edit()) {
           putBoolean("TimeZoneSelectedUtc", key == "TimeZoneSelectedUtc")
            putBoolean("TimeZoneSelectedGmt", key == "TimeZoneSelectedGmt")
            putBoolean("TimeZoneSelectedPst", key == "TimeZoneSelectedPst")
            putBoolean("TimeZoneSelectedFive", key == "TimeZoneSelectedFive")
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