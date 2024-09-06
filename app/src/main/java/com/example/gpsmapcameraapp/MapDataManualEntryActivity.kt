package com.example.gpsmapcameraapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.example.gpsmapcameraapp.fragments.BottomSheetDialogFragmentMapData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapDataManualEntryActivity : AppCompatActivity() {

    private lateinit var backIcon: ImageView
    private lateinit var dropDownIcon: ImageView
    private var isDropDownIconDown = true
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var latitudeValueTv: TextView
    private lateinit var longitudeValueTv: TextView
    private lateinit var pinPointLocationIcon: ImageView
    private lateinit var mapFullScreenIcon: ImageView
    private var isFullScreen = false
    private lateinit var mapTypesIcon : ImageView
    private lateinit var gpsCoordinatesTv : TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesManual : SharedPreferences
    private var selectedMapType: Int = GoogleMap.MAP_TYPE_NORMAL
    private lateinit var editTextOne : EditText
    private lateinit var editTextTwo: EditText
    private lateinit var editTextThree: EditText
    private lateinit var tickIcon : ImageView

    private companion object {
        private const val PREF_NAME = "MapTypeSelection"
        private const val PREF_NAME_MANUAL = "MapDataManualEntryPrefs"
        private const val KEY_SELECTED_MAP_TYPE = "SelectedMapType"
        private const val KEY_LINE_1 = "key_line_1"
        private const val KEY_LINE_2 = "key_line_2"
        private const val KEY_LINE_3 = "key_line_3"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_data_manual_entry)

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        sharedPreferencesManual = getSharedPreferences(PREF_NAME_MANUAL, Context.MODE_PRIVATE)

        // Retrieve the saved map type
        selectedMapType = sharedPreferences.getInt(KEY_SELECTED_MAP_TYPE, GoogleMap.MAP_TYPE_NORMAL)

        editTextOne = findViewById(R.id.line_1_et)
        editTextTwo = findViewById(R.id.line_2_et)
        editTextThree = findViewById(R.id.line_3_et)

        tickIcon = findViewById(R.id.tick_map_data_manual_icon)
        tickIcon.setOnClickListener {
            saveEditTextData()
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra(KEY_LINE_1, editTextOne.text.toString())
                putExtra(KEY_LINE_2, editTextTwo.text.toString())
                putExtra(KEY_LINE_3, editTextThree.text.toString())
            }
            startActivity(intent)
            finish()
        }

        mapTypesIcon = findViewById(R.id.map_types_icon)
        mapTypesIcon.setOnClickListener {
            val bottomSheetFragment = BottomSheetDialogFragmentMapData()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }
        gpsCoordinatesTv = findViewById(R.id.gps_coordinates_tv_1)

        mapFullScreenIcon = findViewById(R.id.map_full_screen_icon)
        mapFullScreenIcon.setOnClickListener {
            toggleMapFullScreen()
        }

        pinPointLocationIcon = findViewById(R.id.pin_location_icon)
        pinPointLocationIcon.setOnClickListener {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        val latLng = LatLng(it.latitude, it.longitude)
                        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment_data_manual_entry) as SupportMapFragment
                        mapFragment.getMapAsync { googleMap ->
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                        }
                    }
                }
        }

        longitudeValueTv = findViewById(R.id.longitude_value_tv_manual)
        latitudeValueTv = findViewById(R.id.latitude_value_tv_manual)

        backIcon = findViewById(R.id.back_icon_4)
        backIcon.setOnClickListener {
            val intent = Intent(this, MapDataManualActivity::class.java)
            startActivity(intent)
            finish()
        }

        dropDownIcon = findViewById(R.id.down_icon_2)
        dropDownIcon.setOnClickListener {
            if (isDropDownIconDown) {
                dropDownIcon.setImageResource(R.drawable.up_icon)
                val popupMenu = PopupMenu(this, dropDownIcon)
                popupMenu.inflate(R.menu.drop_down_menu)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_item_1 -> {
                            val intent = Intent(this, MapDataAutomaticActivity::class.java)
                            startActivity(intent)
                            finish()
                            Toast.makeText(this, "Automatic Selected", Toast.LENGTH_SHORT).show()
                            true
                        }

                        R.id.menu_item_2 -> {
                            dropDownIcon.setImageResource(R.drawable.down_icon)
                            Toast.makeText(this, "Manual Selected", Toast.LENGTH_SHORT).show()
                            true
                        }

                        else -> false
                    }
                }
                popupMenu.show()
            } else {
                dropDownIcon.setImageResource(R.drawable.down_icon)
            }
            isDropDownIconDown = !isDropDownIconDown
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    // Update the TextViews with latitude and longitude
                    latitudeValueTv.text = " ${location.latitude}"
                    longitudeValueTv.text = "${location.longitude}"
                }
                val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment_data_manual_entry) as SupportMapFragment
                mapFragment.getMapAsync { googleMap ->
                    // Add a marker at the current location and move the camera
                    // Apply the selected map type
                    googleMap.mapType = selectedMapType
                    val latLng = location?.let { LatLng(it.latitude, location.longitude) }
                    latLng?.let { MarkerOptions().position(it).title("Marker") }
                        ?.let { googleMap.addMarker(it) }
                    latLng?.let { CameraUpdateFactory.newLatLngZoom(it, 15f) }
                        ?.let { googleMap.moveCamera(it) }
                }
            }

    }
    private fun updateMapType(mapType: Int) {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment_data_manual_entry) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            googleMap.mapType = mapType
        }
    }

    private fun saveMapType(mapType: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_SELECTED_MAP_TYPE, mapType)
        editor.apply()
    }

    private fun saveEditTextData() {
        val editor = sharedPreferencesManual.edit()
        editor.putString(KEY_LINE_1, editTextOne.text.toString())
        editor.putString(KEY_LINE_2, editTextTwo.text.toString())
        editor.putString(KEY_LINE_3, editTextThree.text.toString())
        editor.apply()
    }

    private fun toggleMapFullScreen() {
        val mapContainer: FrameLayout = findViewById(R.id.map_container)
        val params = mapContainer.layoutParams as ConstraintLayout.LayoutParams

        if (isFullScreen) {
            // Revert to original size
            params.height = resources.getDimensionPixelSize(R.dimen._260dp)
            params.topToBottom = R.id.manual_tv_activity_map_data_entry
            params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            pinPointLocationIcon.visibility = View.VISIBLE
            mapTypesIcon.visibility = View.VISIBLE
            gpsCoordinatesTv.visibility = View.VISIBLE
            params.topMargin = resources.getDimensionPixelSize(R.dimen.margin_top_frame_container)
            val mapFullScreenIconParams = mapFullScreenIcon.layoutParams as FrameLayout.LayoutParams
            mapFullScreenIconParams.topMargin = resources.getDimensionPixelSize(R.dimen.margin_top_not_full_screen_mode)

            mapFullScreenIcon.layoutParams = mapFullScreenIconParams

        } else {
            pinPointLocationIcon.visibility = View.INVISIBLE
            mapTypesIcon.visibility = View.INVISIBLE
            // Apply margin to mapTypesIcon
            val mapFullScreenIconParams = mapFullScreenIcon.layoutParams as FrameLayout.LayoutParams
            mapFullScreenIconParams.topMargin = resources.getDimensionPixelSize(R.dimen.margin_top)

            mapFullScreenIcon.layoutParams = mapFullScreenIconParams
            gpsCoordinatesTv.visibility = View.INVISIBLE

            // Expand to full screen
            params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
            params.height = ConstraintLayout.LayoutParams.MATCH_PARENT
            params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        }
        mapContainer.layoutParams = params
        isFullScreen = !isFullScreen
    }

    fun toggleMapSatellite() {
        selectedMapType = GoogleMap.MAP_TYPE_SATELLITE
        saveMapType(selectedMapType)
        updateMapType(selectedMapType)
    }

    fun toggleMapNormal() {
        selectedMapType = GoogleMap.MAP_TYPE_NORMAL
        saveMapType(selectedMapType)
        updateMapType(selectedMapType)
    }


    fun toggleMapTerrain() {
        selectedMapType = GoogleMap.MAP_TYPE_TERRAIN
        saveMapType(selectedMapType)
        updateMapType(selectedMapType)
    }

    fun toggleMapHybrid() {
        selectedMapType = GoogleMap.MAP_TYPE_HYBRID
        saveMapType(selectedMapType)
        updateMapType(selectedMapType)
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MapDataManualActivity::class.java)
        startActivity(intent)
        finish()
    }
}