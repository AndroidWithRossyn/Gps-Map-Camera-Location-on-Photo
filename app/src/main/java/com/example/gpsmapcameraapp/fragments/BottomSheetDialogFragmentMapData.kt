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
import com.example.gpsmapcameraapp.MapDataManualEntryActivity
import com.example.gpsmapcameraapp.R
import com.example.gpsmapcameraapp.TemplateActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialogFragmentMapData : BottomSheetDialogFragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private var satelliteMapSelected = false
    private var normalMapSelected = false
    private var terrainMapSelected = false
    private var hybridMapSelected = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_layout_map_types, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("MapSelection", Context.MODE_PRIVATE)

        satelliteMapSelected = sharedPreferences.getBoolean("SatelliteMapSelected", false)
        normalMapSelected = sharedPreferences.getBoolean("NormalMapSelected", false)
        terrainMapSelected = sharedPreferences.getBoolean("TerrainMapSelected", false)
        hybridMapSelected = sharedPreferences.getBoolean("HybridMapSelected", false)

        val satelliteMap = view.findViewById<TextView>(R.id.satellite_tv)
        val normalMap = view.findViewById<TextView>(R.id.normal_tv)
        val terrainMap = view.findViewById<TextView>(R.id.terrain_tv)
        val hybridMap = view.findViewById<TextView>(R.id.hybrid_tv)

        updateDrawable(satelliteMap, satelliteMapSelected)
        updateDrawable(normalMap, normalMapSelected)
        updateDrawable(terrainMap, terrainMapSelected)
        updateDrawable(hybridMap, hybridMapSelected)


        satelliteMap.setOnClickListener {
            val parentActivity = activity as? MapDataManualEntryActivity
            parentActivity?.toggleMapSatellite()
            satelliteMapSelected = true
            normalMapSelected = false
            terrainMapSelected = false
            hybridMapSelected = false
            updateDrawable(satelliteMap, true)
            updateDrawable(normalMap, false)
            updateDrawable(terrainMap, false)
            updateDrawable(hybridMap, false)
            saveSelectionStates()
            updateSelection(GoogleMap.MAP_TYPE_SATELLITE)
            val parentActivityTwo = activity as? TemplateActivity
            parentActivityTwo?.toggleMapSatellite()
            parentActivityTwo?.saveSelectedMapType(GoogleMap.MAP_TYPE_SATELLITE)
            parentActivityTwo?.updateCircleForMapTypeIcon(GoogleMap.MAP_TYPE_SATELLITE)
            dismiss()
        }

        normalMap.setOnClickListener {
            val parentActivity = activity as? MapDataManualEntryActivity
            parentActivity?.toggleMapNormal()
            satelliteMapSelected = false
            normalMapSelected = true
            terrainMapSelected = false
            hybridMapSelected = false
            updateDrawable(satelliteMap, false)
            updateDrawable(normalMap, true)
            updateDrawable(terrainMap, false)
            updateDrawable(hybridMap, false)
            saveSelectionStates()
            updateSelection(GoogleMap.MAP_TYPE_NORMAL)
            val parentActivityTwo = activity as? TemplateActivity
            parentActivityTwo?.toggleMapNormal()
            parentActivityTwo?.saveSelectedMapType(GoogleMap.MAP_TYPE_NORMAL)
            parentActivityTwo?.updateCircleForMapTypeIcon(GoogleMap.MAP_TYPE_NORMAL)
            dismiss()
        }

        terrainMap.setOnClickListener {
            val parentActivity = activity as? MapDataManualEntryActivity
            parentActivity?.toggleMapTerrain()
            satelliteMapSelected = false
            normalMapSelected = false
            terrainMapSelected = true
            hybridMapSelected = false
            updateDrawable(satelliteMap, false)
            updateDrawable(normalMap, false)
            updateDrawable(terrainMap, true)
            updateDrawable(hybridMap, false)
            saveSelectionStates()
            updateSelection(GoogleMap.MAP_TYPE_TERRAIN)
            val parentActivityTwo = activity as? TemplateActivity
            parentActivityTwo?.toggleMapTerrain()
            parentActivityTwo?.saveSelectedMapType(GoogleMap.MAP_TYPE_TERRAIN)
            parentActivityTwo?.updateCircleForMapTypeIcon(GoogleMap.MAP_TYPE_TERRAIN)
            dismiss()
        }

        hybridMap.setOnClickListener {
            val parentActivity = activity as? MapDataManualEntryActivity
            parentActivity?.toggleMapHybrid()
            satelliteMapSelected = false
            normalMapSelected = false
            terrainMapSelected = false
            hybridMapSelected = true
            updateDrawable(satelliteMap, false)
            updateDrawable(normalMap, false)
            updateDrawable(terrainMap, false)
            updateDrawable(hybridMap, true)
            saveSelectionStates()
            updateSelection(GoogleMap.MAP_TYPE_HYBRID)
            val parentActivityTwo = activity as? TemplateActivity
            parentActivityTwo?.toggleMapHybrid()
            parentActivityTwo?.saveSelectedMapType(GoogleMap.MAP_TYPE_HYBRID)
            parentActivityTwo?.updateCircleForMapTypeIcon(GoogleMap.MAP_TYPE_HYBRID)
            dismiss()
        }

    }

    private fun updateSelection(mapType: Int) {
        val parentActivity = activity as? MapDataManualEntryActivity
        val parentActivityTwo = activity as? TemplateActivity

        when (mapType) {
            GoogleMap.MAP_TYPE_SATELLITE -> {
                parentActivity?.toggleMapSatellite()
                parentActivityTwo?.toggleMapSatellite()
            }
            GoogleMap.MAP_TYPE_NORMAL -> {
                parentActivity?.toggleMapNormal()
                parentActivityTwo?.toggleMapNormal()
            }
            GoogleMap.MAP_TYPE_TERRAIN -> {
                parentActivity?.toggleMapTerrain()
                parentActivityTwo?.toggleMapTerrain()
            }
            GoogleMap.MAP_TYPE_HYBRID -> {
                parentActivity?.toggleMapHybrid()
                parentActivityTwo?.toggleMapHybrid()
            }
        }

        // Update selection state
        satelliteMapSelected = mapType == GoogleMap.MAP_TYPE_SATELLITE
        normalMapSelected = mapType == GoogleMap.MAP_TYPE_NORMAL
        terrainMapSelected = mapType == GoogleMap.MAP_TYPE_TERRAIN
        hybridMapSelected = mapType == GoogleMap.MAP_TYPE_HYBRID

        // Update drawables
        val satelliteMap = view?.findViewById<TextView>(R.id.satellite_tv)
        val normalMap = view?.findViewById<TextView>(R.id.normal_tv)
        val terrainMap = view?.findViewById<TextView>(R.id.terrain_tv)
        val hybridMap = view?.findViewById<TextView>(R.id.hybrid_tv)

        if (satelliteMap != null) {
            updateDrawable(satelliteMap, satelliteMapSelected)
        }
        if (normalMap != null) {
            updateDrawable(normalMap, normalMapSelected)
        }
        if (terrainMap != null) {
            updateDrawable(terrainMap, terrainMapSelected)
        }
        if (hybridMap != null) {
            updateDrawable(hybridMap, hybridMapSelected)
        }

        // Save the selection state and map type
        saveSelectionStates()
        parentActivityTwo?.saveSelectedMapType(mapType)
        parentActivityTwo?.updateCircleForMapTypeIcon(mapType)
        dismiss()

        // Save the selected map type in SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("MapPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("SelectedMapType", mapType)
        editor.apply()

        // Update map type in MainActivity
        (activity as? MainActivity)?.updateMapType(mapType)
    }

    private fun saveSelectionStates() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("SatelliteMapSelected", satelliteMapSelected)
        editor.putBoolean("NormalMapSelected", normalMapSelected)
        editor.putBoolean("TerrainMapSelected", terrainMapSelected)
        editor.putBoolean("HybridMapSelected", hybridMapSelected)
        editor.apply()
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
