package com.example.gpsmapcameraapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Locale

class MapDataAutomaticActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var backIcon: ImageView
    private lateinit var latitudeValueTv: TextView
    private lateinit var longitudeValueTv: TextView
    private lateinit var dropDownIcon: ImageView
    private var isDropDownIconDown = true
    private var fromManualActivity = false

    @SuppressLint("SetTextI18n", "RestrictedApi", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_data_automatic)

         fromManualActivity = intent.getBooleanExtra("from_manual_activity", false)

        disableViews()

        backIcon = findViewById(R.id.back_icon_2)
        dropDownIcon = findViewById(R.id.down_icon)
        longitudeValueTv = findViewById(R.id.longitude_value_tv)
        latitudeValueTv = findViewById(R.id.latitude_value_tv)

        dropDownIcon.setOnClickListener {
            if (isDropDownIconDown) {
                dropDownIcon.setImageResource(R.drawable.up_icon)
                val popupMenu = PopupMenu(this, dropDownIcon)
                popupMenu.inflate(R.menu.drop_down_menu)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_item_1 -> {
                            dropDownIcon.setImageResource(R.drawable.down_icon)
                            Toast.makeText(this, "Automatic Selected", Toast.LENGTH_SHORT).show()
                            true
                        }

                        R.id.menu_item_2 -> {
                            val intent = Intent(this, MapDataManualActivity::class.java)
                            startActivity(intent)
                            finish()
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

        backIcon.setOnClickListener {
            navigateBack()
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
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    // Get city, province, and country from the location
                    val address = getAddress(location.latitude, location.longitude)
                    val city = address.locality
                    val province = address.adminArea // Province
                    val country = address.countryName
                    // Update the TextViews with the location information
                    findViewById<TextView>(R.id.city_province_country_tv).text =
                        "$city, $province, $country"
                    findViewById<TextView>(R.id.city_tv).text = city
                    findViewById<TextView>(R.id.province_country_tv).text = "$province, $country"
                    // Update the TextViews with latitude and longitude
                    latitudeValueTv.text = " ${location.latitude}"
                    longitudeValueTv.text = "${location.longitude}"
                }
                val mapFragment =
                    supportFragmentManager.findFragmentById(R.id.map_fragment_data_automatic) as SupportMapFragment
                mapFragment.getMapAsync { googleMap ->
                    // Add a marker at the current location and move the camera
                    val latLng = location?.let { LatLng(it.latitude, location.longitude) }
                    latLng?.let { MarkerOptions().position(it).title("Marker") }
                        ?.let { googleMap.addMarker(it) }
                    latLng?.let { CameraUpdateFactory.newLatLngZoom(it, 15f) }
                        ?.let { googleMap.moveCamera(it) }
                }
            }
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment_data_automatic) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            googleMap.uiSettings.setAllGesturesEnabled(false)
        }
    }

    private fun getAddress(latitude: Double, longitude: Double): Address {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)!!
        return addresses[0]
    }

    private fun disableViews() {
        // Disable all the views
        findViewById<CardView>(R.id.map_card_view_data_automatic).isEnabled = false
        findViewById<TextView>(R.id.gps_coordinates_tv).isEnabled = false
        findViewById<TextView>(R.id.latitude_tv).isEnabled = false
        findViewById<TextView>(R.id.longitude_tv).isEnabled = false
        findViewById<TextView>(R.id.latitude_value_tv).isEnabled = false
        findViewById<TextView>(R.id.longitude_value_tv).isEnabled = false
        findViewById<View>(R.id.latitude_value_view).isEnabled = false
        findViewById<View>(R.id.longitude_value_view).isEnabled = false
        findViewById<TextView>(R.id.location_tv).isEnabled = false
        findViewById<TextView>(R.id.line_1_tv).isEnabled = false
        findViewById<TextView>(R.id.city_province_country_tv).isEnabled = false
        findViewById<View>(R.id.city_province_country_tv_view).isEnabled = false
        findViewById<TextView>(R.id.line_2).isEnabled = false
        findViewById<TextView>(R.id.city_tv).isEnabled = false
        findViewById<View>(R.id.city_tv_view).isEnabled = false
        findViewById<TextView>(R.id.line_3_tv).isEnabled = false
        findViewById<TextView>(R.id.province_country_tv).isEnabled = false
        findViewById<View>(R.id.province_country_tv_view).isEnabled = false

        // Apply fade and blur effect
        applyFadeAndBlur()
    }

    private fun applyFadeAndBlur() {
        // Set alpha to indicate disabled state
        findViewById<CardView>(R.id.map_card_view_data_automatic).alpha = 0.5f
        findViewById<TextView>(R.id.gps_coordinates_tv).alpha = 0.5f
        findViewById<TextView>(R.id.latitude_tv).alpha = 0.5f
        findViewById<TextView>(R.id.longitude_tv).alpha = 0.5f
        findViewById<TextView>(R.id.latitude_value_tv).alpha = 0.5f
        findViewById<TextView>(R.id.longitude_value_tv).alpha = 0.5f
        findViewById<View>(R.id.latitude_value_view).alpha = 0.5f
        findViewById<View>(R.id.longitude_value_view).alpha = 0.5f
        findViewById<TextView>(R.id.location_tv).alpha = 0.5f
        findViewById<TextView>(R.id.line_1_tv).alpha = 0.5f
        findViewById<TextView>(R.id.city_province_country_tv).alpha = 0.5f
        findViewById<View>(R.id.city_province_country_tv_view).alpha = 0.5f
        findViewById<TextView>(R.id.line_2).alpha = 0.5f
        findViewById<TextView>(R.id.city_tv).alpha = 0.5f
        findViewById<View>(R.id.city_tv_view).alpha = 0.5f
        findViewById<TextView>(R.id.line_3_tv).alpha = 0.5f
        findViewById<TextView>(R.id.province_country_tv).alpha = 0.5f
        findViewById<View>(R.id.province_country_tv_view).alpha = 0.5f
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        navigateBack()
    }

    private fun navigateBack() {
        val intent = Intent(this, MainActivity::class.java)

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("The data will be changed according to the current location. Are you sure you want to continue?")
            .setPositiveButton("OK") { dialog, which ->
                if (fromManualActivity) {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // Request permissions if needed
                        return@setPositiveButton
                    }
                    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                        location?.let {
                            val address = getAddress(location.latitude, location.longitude)
                            val city = address.locality
                            val province = address.adminArea
                            val country = address.countryName

                            intent.putExtra("location_details", "$city, $province, $country")
                            intent.putExtra("latitude", location.latitude)
                            intent.putExtra("longitude", location.longitude)
                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
                    startActivity(intent)
                    finish()
                }
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .setCancelable(true)
            .create()

        alertDialog.show()
    }

}




