package com.example.gpsmapcameraapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.gpsmapcameraapp.fragments.BottomSheetDialogFragmentAccuracy
import com.example.gpsmapcameraapp.fragments.BottomSheetDialogFragmentAltitude
import com.example.gpsmapcameraapp.fragments.BottomSheetDialogFragmentLatLongData
import com.example.gpsmapcameraapp.fragments.BottomSheetDialogFragmentMapData
import com.example.gpsmapcameraapp.fragments.BottomSheetDialogFragmentPlusCode
import com.example.gpsmapcameraapp.fragments.BottomSheetDialogFragmentPressure
import com.example.gpsmapcameraapp.fragments.BottomSheetDialogFragmentTimeZone
import com.example.gpsmapcameraapp.fragments.BottomSheetDialogFragmentWeather
import com.example.gpsmapcameraapp.fragments.BottomSheetDialogFragmentWindData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TemplateActivity : AppCompatActivity() {

    private lateinit var cityCountryTv: TextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var longLatTv: TextView
    private lateinit var dayDateTimeTv: TextView
    private lateinit var backIcon: ImageView
    private lateinit var editTemplateIcon: ImageView
    private lateinit var mapTypeSelectionIcon: ImageView
    private var isMapTypeSelected = false
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var latLongSelectionIcon: ImageView
    private var isLatLongSelected = false
    private lateinit var plusCodeIcon : ImageView
    private lateinit var dateAndTimeIcon : ImageView
    private var isPlusCodeSelected = false
    private var isDateAndTimeSelected = false
    private lateinit var timeZoneSelectionIcon : ImageView
    private var isTimeZoneSelected = false
    private var isNumberingSelected = false
    private lateinit var noteHashtagIcon : ImageView
    private lateinit var weatherIcon : ImageView
    private var isNoteHashtagSelected = false
    private var isWeatherSelected = false
    private lateinit var windIcon : ImageView
    private lateinit var humidityIcon : ImageView
    private var isWindSelected = false
    private var isHumiditySelected = false
    private lateinit var pressureIcon : ImageView
    private lateinit var altitudeIcon : ImageView
    private var isPressureSelected = false
    private var isAltitudeSelected = false
    private lateinit var accuracyIcon : ImageView
    private var isAccuracySelected = false
    private var selectedMapType = GoogleMap.MAP_TYPE_NORMAL
    private var selectedTextLatLong = "String"
    private lateinit var noteTv : TextView
    private lateinit var plusCodeTv : TextView
    private lateinit var timeZoneTv : TextView
    private var selectedTextPlusCode = "String"
    private var selectedTimeZone = "String"
    private var selectedWeatherText = "String"
    private lateinit var weatherTv : TextView
    private lateinit var cloudyIcon : ImageView
    private lateinit var windTv : TextView
    private lateinit var windIconTemplate : ImageView
    private var selectedWindText = "String"
    private lateinit var humidityIconTemplate : ImageView
    private lateinit var humidityTv : TextView
    private lateinit var pressureIconTemplate : ImageView
    private lateinit var pressureTv : TextView
    private lateinit var altitudeIconTemplate : ImageView
    private lateinit var altitudeTv : TextView
    private var selectedAltitude = "String"
    private lateinit var accuracyIconTemplate : ImageView
    private lateinit var accuracyTv : TextView
    private var selectedAccuracy = "String"


    @SuppressLint("SetTextI18n", "InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_template)

        sharedPreferences = getSharedPreferences("LatLongSelection", Context.MODE_PRIVATE)


        sharedPreferences = getSharedPreferences("TemplatePreferences", Context.MODE_PRIVATE)
        isMapTypeSelected = sharedPreferences.getBoolean("IsMapTypeSelected", false)
        isLatLongSelected = sharedPreferences.getBoolean("IsLatLongSelected", false)
        isPlusCodeSelected = sharedPreferences.getBoolean("IsPlusCodeSelected", false)
        isDateAndTimeSelected = sharedPreferences.getBoolean("IsDateAndTimeSelected", false)
        isTimeZoneSelected = sharedPreferences.getBoolean("IsTimeZoneSelected", false)
        isNumberingSelected = sharedPreferences.getBoolean("isNumberingSelected", false)
        isNoteHashtagSelected = sharedPreferences.getBoolean("isNoteHashtagSelected", false)
        isWeatherSelected = sharedPreferences.getBoolean("isWeatherSelected", false)
        isWindSelected = sharedPreferences.getBoolean("isWindSelected", false)
        isHumiditySelected = sharedPreferences.getBoolean("isHumiditySelected", false)
        isPressureSelected = sharedPreferences.getBoolean("isPressureSelected", false)
        isAltitudeSelected = sharedPreferences.getBoolean("isAltitudeSelected", false)
        isAccuracySelected = sharedPreferences.getBoolean("isAccuracySelected", false)

        selectedMapType = sharedPreferences.getInt("SelectedMapType", GoogleMap.MAP_TYPE_NORMAL)
        selectedTextLatLong = sharedPreferences.getString("selected_lat_long_text", "string").toString()
        selectedTextPlusCode = sharedPreferences.getString("selected_plus_code_text", "String").toString()
        selectedTimeZone = sharedPreferences.getString("selected_time_zone_text", "String").toString()
        selectedWeatherText = sharedPreferences.getString("selected_weather_text", "String").toString()
        selectedWindText = sharedPreferences.getString("selected_wind_text", "String").toString()
        selectedAltitude = sharedPreferences.getString("selected_altitude_text", "String").toString()
        selectedAccuracy = sharedPreferences.getString("selected_accuracy_text", "String").toString()

        editTemplateIcon = findViewById(R.id.edit_template_icon)
        backIcon = findViewById(R.id.back_icon_5)
        cityCountryTv = findViewById(R.id.city_country_tv_1)
        longLatTv = findViewById(R.id.long_lat_tv_1)
        dayDateTimeTv = findViewById(R.id.date_day_time_tv_1)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        noteTv = findViewById(R.id.captured_by_tv_1)
        plusCodeTv = findViewById(R.id.plus_code_tv_template_location_details_card)
        timeZoneTv = findViewById(R.id.time_zone_tv_template)
        weatherTv = findViewById(R.id.weather_tv_template_card)
        cloudyIcon = findViewById(R.id.cloudy_icon)
        windTv = findViewById(R.id.wind_tv_template)
        windIconTemplate = findViewById(R.id.wind_icon)
        humidityIconTemplate = findViewById(R.id.humidity_icon)
        humidityTv = findViewById(R.id.humidity_tv_template)
        pressureIconTemplate = findViewById(R.id.pressure_icon)
        pressureTv = findViewById(R.id.pressure_tv_template)
        altitudeIconTemplate = findViewById(R.id.altitude_icon)
        altitudeTv = findViewById(R.id.altitude_tv_template)
        accuracyIconTemplate = findViewById(R.id.accuracy_icon)
        accuracyTv = findViewById(R.id.accuracy_tv_template)

        loadLatLongText()
        loadPLusCodeText()
        loadTimeZoneText()
        loadWeatherText()
        loadWindText()
        loadPressureText()
        loadAltitudeText()
        loadAccuracyText()

        editTemplateIcon.setOnClickListener {
            editTemplateIcon.visibility = View.INVISIBLE
            showBottomSheetDialog()
        }

        backIcon.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        updateCircleForMapTypeIcon(selectedMapType)

        getLocationData()
    }

    fun updateLatLongText(text: String) {
        longLatTv.text = text
        saveLatLongText(text)
    }

    private fun saveLatLongText(text: String) {
        with(sharedPreferences.edit()) {
            putString("selected_lat_long_text", text)
            apply()
        }
    }

    private fun loadLatLongText() {
        val savedText = sharedPreferences.getString("selected_lat_long_text", "")
        if (!savedText.isNullOrEmpty()) {
            longLatTv.text = savedText
        }
    }

    fun updatePlusCodeText(text: String) {
        plusCodeTv.text = text
        savePlusCodeText(text)
    }

    private fun savePlusCodeText(text: String) {
        with(sharedPreferences.edit()) {
            putString("selected_plus_code_text", text)
            apply()
        }
    }

    private fun loadPLusCodeText() {
        val savedText = sharedPreferences.getString("selected_plus_code_text", "")
        if (!savedText.isNullOrEmpty()) {
            plusCodeTv.text = savedText
        }
    }

    fun updateTimeZoneText(text: String) {
        timeZoneTv.text = text
        saveTimeZoneText(text)
    }

    private fun saveTimeZoneText(text: String) {
        with(sharedPreferences.edit()) {
            putString("selected_time_zone_text", text)
            apply()
        }
    }

    private fun loadTimeZoneText() {
        val savedText = sharedPreferences.getString("selected_time_zone_text", "")
        if (!savedText.isNullOrEmpty()) {
            timeZoneTv.text = savedText
        }
    }

    fun updateWeatherText(text: String) {
        weatherTv.text = text
        saveWeatherText(text)
    }

    private fun saveWeatherText(text: String) {
        with(sharedPreferences.edit()) {
            putString("selected_weather_text", text)
            apply()
        }
    }

    private fun loadWeatherText() {
        val savedText = sharedPreferences.getString("selected_weather_text", "")
        if (!savedText.isNullOrEmpty()) {
            weatherTv.text = savedText
        }
    }

    fun updateWindText(text: String) {
        windTv.text = text
        saveWindText(text)
    }

    private fun saveWindText(text: String) {
        with(sharedPreferences.edit()) {
            putString("selected_wind_text", text)
            apply()
        }
    }

    private fun loadWindText() {
        val savedText = sharedPreferences.getString("selected_wind_text", "")
        if (!savedText.isNullOrEmpty()) {
            windTv.text = savedText
        }
    }

    fun updatePressureText(text: String) {
        pressureTv.text = text
        savePressureText(text)
    }

    private fun savePressureText(text: String) {
        with(sharedPreferences.edit()) {
            putString("selected_pressure_text", text)
            apply()
        }
    }

    private fun loadPressureText() {
        val savedText = sharedPreferences.getString("selected_pressure_text", "")
        if (!savedText.isNullOrEmpty()) {
            pressureTv.text = savedText
        }
    }

    fun updateAltitudeText(text: String) {
        altitudeTv.text = text
        saveAltitudeText(text)
    }

    private fun saveAltitudeText(text: String) {
        with(sharedPreferences.edit()) {
            putString("selected_altitude_text", text)
            apply()
        }
    }

    private fun loadAltitudeText() {
        val savedText = sharedPreferences.getString("selected_altitude_text", "")
        if (!savedText.isNullOrEmpty()) {
            altitudeTv.text = savedText
        }
    }

    fun updateAccuracyText(text: String) {
        accuracyTv.text = text
        saveAccuracyText(text)
    }

    private fun saveAccuracyText(text: String) {
        with(sharedPreferences.edit()) {
            putString("selected_accuracy_text", text)
            apply()
        }
    }

    private fun loadAccuracyText() {
        val savedText = sharedPreferences.getString("selected_accuracy_text", "")
        if (!savedText.isNullOrEmpty()) {
            accuracyTv.text = savedText
        }
    }

    fun updateCircleForMapTypeIcon(mapType: Int) {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment_template) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            googleMap.mapType = mapType
        }
    }

    fun toggleMapSatellite() {
        selectedMapType = GoogleMap.MAP_TYPE_SATELLITE
        saveSelectedMapType(selectedMapType)
        updateCircleForMapTypeIcon(selectedMapType)
    }

    fun toggleMapNormal() {
        selectedMapType = GoogleMap.MAP_TYPE_NORMAL
        saveSelectedMapType(selectedMapType)
        updateCircleForMapTypeIcon(selectedMapType)
    }


    fun toggleMapTerrain() {
        selectedMapType = GoogleMap.MAP_TYPE_TERRAIN
        saveSelectedMapType(selectedMapType)
        updateCircleForMapTypeIcon(selectedMapType)
    }

    fun toggleMapHybrid() {
        selectedMapType = GoogleMap.MAP_TYPE_HYBRID
        saveSelectedMapType(selectedMapType)
        updateCircleForMapTypeIcon(selectedMapType)
    }

    fun saveSelectedMapType(mapType: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("SelectedMapType", mapType)
        editor.apply()
    }



    @SuppressLint("InflateParams")
    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.template_bottom_sheet_dialog, null)
        bottomSheetDialog.setContentView(view)

        // Adjust height of the bottom sheet dialog
        val layoutParams = view.layoutParams
        layoutParams.height = 1500 // Set your desired height here
        view.layoutParams = layoutParams

        mapTypeSelectionIcon = view.findViewById(R.id.uncheck_circle_template_screen)
        latLongSelectionIcon = view.findViewById(R.id.uncheck_circle_template_screen_pressure)
        plusCodeIcon = view.findViewById(R.id.uncheck_circle_template_screen_accuracy)
        dateAndTimeIcon = view.findViewById(R.id.uncheck_circle_template_screen_short_address)
        timeZoneSelectionIcon = view.findViewById(R.id.uncheck_circle_template_screen_address)
        noteHashtagIcon = view.findViewById(R.id.uncheck_circle_template_screen_plus_code)
        weatherIcon = view.findViewById(R.id.uncheck_circle_template_screen_date_and_time)
        windIcon = view.findViewById(R.id.uncheck_circle_template_screen_time_zone)
        humidityIcon = view.findViewById(R.id.uncheck_circle_template_screen_numbering)
        pressureIcon = view.findViewById(R.id.uncheck_circle_template_screen_note_hashtag)
        altitudeIcon = view.findViewById(R.id.uncheck_circle_template_screen_weather)
        accuracyIcon = view.findViewById(R.id.uncheck_circle_template_screen_wind)

        // Restore and update UI elements based on the saved state
        updateIcons()


        val mapTypeTextView = view.findViewById<TextView>(R.id.map_type_tv_template)
        mapTypeTextView.setOnClickListener {
            isMapTypeSelected = !isMapTypeSelected
            saveMapTypeSelectionState()
            updateIcons()
            if (isMapTypeSelected) {
                val bottomSheetDialogFragmentMapData = BottomSheetDialogFragmentMapData()
                bottomSheetDialogFragmentMapData.show(
                    supportFragmentManager,
                    bottomSheetDialogFragmentMapData.tag
                )
            }
        }

        val latLongTextView = view.findViewById<TextView>(R.id.lat_long_tv)
        latLongTextView.setOnClickListener {
            isLatLongSelected = !isLatLongSelected
            saveLatLongSelectionState()
            updateIcons()
            if (isLatLongSelected){
                val bottomSheetDialogFragmentLatLongData = BottomSheetDialogFragmentLatLongData()
                bottomSheetDialogFragmentLatLongData.show(
                    supportFragmentManager,
                    bottomSheetDialogFragmentLatLongData.tag
                )
            }
        }

        bottomSheetDialog.setOnDismissListener {
            editTemplateIcon.visibility = View.VISIBLE
        }
        bottomSheetDialog.show()


        val timeZoneTv = view.findViewById<TextView>(R.id.time_zone_tv)
        timeZoneTv.setOnClickListener {
            isTimeZoneSelected = !isTimeZoneSelected
            isTimeZoneSelectedState()
            updateIcons()
            if (isTimeZoneSelected){
                val bottomSheetDialogFragmentTimeZone = BottomSheetDialogFragmentTimeZone()
                bottomSheetDialogFragmentTimeZone.show(
                    supportFragmentManager,
                    bottomSheetDialogFragmentTimeZone.tag
                )
            }
        }
        bottomSheetDialog.setOnDismissListener {
            editTemplateIcon.visibility = View.VISIBLE
        }
        bottomSheetDialog.show()


        val plusCodeTv = view.findViewById<TextView>(R.id.plus_code_tv)
        plusCodeTv.setOnClickListener {
            isPlusCodeSelected = !isPlusCodeSelected
            savePlusCodeSelectionState()
            updateIcons()
            if (isPlusCodeSelected){
                val bottomSheetDialogFragmentPlusCode = BottomSheetDialogFragmentPlusCode()
                bottomSheetDialogFragmentPlusCode.show(
                    supportFragmentManager,
                    bottomSheetDialogFragmentPlusCode.tag
                )
            }
        }
        bottomSheetDialog.setOnDismissListener {
            editTemplateIcon.visibility = View.VISIBLE
        }
        bottomSheetDialog.show()

        val dateAndTimeTv = view.findViewById<TextView>(R.id.date_amp_time_tv)
        dateAndTimeTv.setOnClickListener {
            isDateAndTimeSelected = !isDateAndTimeSelected
            saveDateAndTimeSelectedState()
            updateIcons()
        }
        bottomSheetDialog.setOnDismissListener {
            editTemplateIcon.visibility = View.VISIBLE
        }
        bottomSheetDialog.show()

        val noteHashtagTv = view.findViewById<TextView>(R.id.note_hashtag_tv)
        noteHashtagTv.setOnClickListener {
            isNoteHashtagSelected = !isNoteHashtagSelected
            saveNoteHashtagSelectedState()
            updateIcons()
        }
        bottomSheetDialog.setOnDismissListener {
            editTemplateIcon.visibility = View.VISIBLE
        }
        bottomSheetDialog.show()

        val weatherTv = view.findViewById<TextView>(R.id.weather_tv)
        weatherTv.setOnClickListener {
            isWeatherSelected = !isWeatherSelected
            saveWeatherSelectedState()
            updateIcons()
            if (isWeatherSelected){
                val bottomSheetDialogFragmentWeather = BottomSheetDialogFragmentWeather()
                bottomSheetDialogFragmentWeather.show(
                    supportFragmentManager,
                    bottomSheetDialogFragmentWeather.tag
                )
            }
        }
        bottomSheetDialog.setOnDismissListener {
            editTemplateIcon.visibility = View.VISIBLE
        }
        bottomSheetDialog.show()

        val windTv = view.findViewById<TextView>(R.id.wind_tv)
        windTv.setOnClickListener {
            isWindSelected = !isWindSelected
            saveWindSelectionState()
            updateIcons()
            if (isWindSelected){
                val bottomSheetDialogFragmentWindData = BottomSheetDialogFragmentWindData()
                bottomSheetDialogFragmentWindData.show(
                    supportFragmentManager,
                    bottomSheetDialogFragmentWindData.tag
                )
            }
        }
        bottomSheetDialog.setOnDismissListener {
            editTemplateIcon.visibility = View.VISIBLE
        }
        bottomSheetDialog.show()

        val humidityTv = view.findViewById<TextView>(R.id.humidity_tv)
        humidityTv.setOnClickListener {
            isHumiditySelected = !isHumiditySelected
            saveHumiditySelectionState()
            updateIcons()
        }
        bottomSheetDialog.setOnDismissListener {
            editTemplateIcon.visibility = View.VISIBLE
        }
        bottomSheetDialog.show()

        val pressureTv = view.findViewById<TextView>(R.id.pressure_tv)
        pressureTv.setOnClickListener {
            isPressureSelected = !isPressureSelected
            savePressureSelectionState()
            updateIcons()
            if (isPressureSelected){
                val bottomSheetDialogFragmentPressure = BottomSheetDialogFragmentPressure()
                bottomSheetDialogFragmentPressure.show(
                    supportFragmentManager,
                    bottomSheetDialogFragmentPressure.tag
                )
            }
        }
        bottomSheetDialog.setOnDismissListener {
            editTemplateIcon.visibility = View.VISIBLE
        }
        bottomSheetDialog.show()

        val altitudeTv = view.findViewById<TextView>(R.id.altitude_tv)
        altitudeTv.setOnClickListener {
            isAltitudeSelected = !isAltitudeSelected
            saveAltitudeSelected()
            updateIcons()
            if (isAltitudeSelected){
                val bottomSheetDialogFragmentAltitude = BottomSheetDialogFragmentAltitude()
                bottomSheetDialogFragmentAltitude.show(
                    supportFragmentManager,
                    bottomSheetDialogFragmentAltitude.tag
                )
            }
        }
        bottomSheetDialog.setOnDismissListener {
            editTemplateIcon.visibility = View.VISIBLE
        }
        bottomSheetDialog.show()

        val accuracyTv = view.findViewById<TextView>(R.id.accuracy_tv)
        accuracyTv.setOnClickListener {
            isAccuracySelected =!isAccuracySelected
            saveAccuracySelected()
            updateIcons()
            if (isAccuracySelected){
                val bottomSheetDialogFragmentAccuracy = BottomSheetDialogFragmentAccuracy()
                bottomSheetDialogFragmentAccuracy.show(
                    supportFragmentManager,
                    bottomSheetDialogFragmentAccuracy.tag
                )
            }
        }
        bottomSheetDialog.setOnDismissListener {
            editTemplateIcon.visibility = View.VISIBLE
        }
        bottomSheetDialog.show()
    }


    private fun updateIcons() {
        if (isMapTypeSelected) {
            mapTypeSelectionIcon.setImageResource(R.drawable.selected_check_icon)
            val fragmentContainerMap = findViewById<FrameLayout>(R.id.map_container_template)
            fragmentContainerMap.visibility = View.VISIBLE

        } else {
            mapTypeSelectionIcon.setImageResource(R.drawable.uncheck_circle_template_screen)
            val fragmentContainerMap = findViewById<FrameLayout>(R.id.map_container_template)
            fragmentContainerMap.visibility = View.INVISIBLE
        }

        if (isLatLongSelected) {
            latLongSelectionIcon.setImageResource(R.drawable.selected_check_icon)
            longLatTv.visibility = View.VISIBLE
        } else {
            latLongSelectionIcon.setImageResource(R.drawable.uncheck_circle_template_screen)
            longLatTv.visibility = View.INVISIBLE

        }

        if (isPlusCodeSelected){
            plusCodeIcon.setImageResource(R.drawable.selected_check_icon)
            plusCodeTv.visibility = View.VISIBLE
        }
        else{
            plusCodeIcon.setImageResource(R.drawable.uncheck_circle_template_screen)
            plusCodeTv.visibility = View.INVISIBLE
        }

        if (isDateAndTimeSelected){
            dateAndTimeIcon.setImageResource(R.drawable.selected_check_icon)
            dayDateTimeTv.visibility = View.VISIBLE

        }else{
            dateAndTimeIcon.setImageResource(R.drawable.uncheck_circle_template_screen)
            dayDateTimeTv.visibility = View.INVISIBLE
        }

        if (isTimeZoneSelected){
            timeZoneSelectionIcon.setImageResource(R.drawable.selected_check_icon)
            timeZoneTv.visibility = View.VISIBLE
        }
        else{
            timeZoneSelectionIcon.setImageResource(R.drawable.uncheck_circle_template_screen)
            timeZoneTv.visibility = View.INVISIBLE

        }

        if (isNoteHashtagSelected){
            noteHashtagIcon.setImageResource(R.drawable.selected_check_icon)
            noteTv.visibility = View.VISIBLE

        }
        else{
            noteHashtagIcon.setImageResource(R.drawable.uncheck_circle_template_screen)
            noteTv.visibility = View.INVISIBLE

        }

        if (isWeatherSelected){
            weatherIcon.setImageResource(R.drawable.selected_check_icon)
            cloudyIcon.visibility = View.VISIBLE
            weatherTv.visibility = View.VISIBLE


        }
        else{
            weatherIcon.setImageResource(R.drawable.uncheck_circle_template_screen)
            cloudyIcon.visibility = View.INVISIBLE
            weatherTv.visibility = View.INVISIBLE

        }
        if(isWindSelected){
            windIcon.setImageResource(R.drawable.selected_check_icon)
            windIconTemplate.visibility = View.VISIBLE
            windTv.visibility = View.VISIBLE


        }
        else{
            windIcon.setImageResource(R.drawable.uncheck_circle_template_screen)
            windIconTemplate.visibility = View.INVISIBLE
            windTv.visibility = View.INVISIBLE

        }
        if (isHumiditySelected){
            humidityIcon.setImageResource(R.drawable.selected_check_icon)
            humidityIconTemplate.visibility = View.VISIBLE
            humidityTv.visibility = View.VISIBLE

        }
        else{
            humidityIcon.setImageResource(R.drawable.uncheck_circle_template_screen)
            humidityIconTemplate.visibility = View.INVISIBLE
            humidityTv.visibility = View.INVISIBLE

        }
        if (isPressureSelected){
            pressureIcon.setImageResource(R.drawable.selected_check_icon)
            pressureIconTemplate.visibility = View.VISIBLE
            pressureTv.visibility = View.VISIBLE

        }
        else{
            pressureIcon.setImageResource(R.drawable.uncheck_circle_template_screen)
            pressureIconTemplate.visibility = View.INVISIBLE
            pressureTv.visibility = View.INVISIBLE

        }
        if (isAltitudeSelected){
            altitudeIcon.setImageResource(R.drawable.selected_check_icon)
            altitudeIconTemplate.visibility = View.VISIBLE
            altitudeTv.visibility = View.VISIBLE

        }
        else{
            altitudeIcon.setImageResource(R.drawable.uncheck_circle_template_screen)
            altitudeIconTemplate.visibility = View.INVISIBLE
            altitudeTv.visibility = View.INVISIBLE

        }
        if (isAccuracySelected){
            accuracyIcon.setImageResource(R.drawable.selected_check_icon)
            accuracyIconTemplate.visibility = View.VISIBLE
            accuracyTv.visibility = View.VISIBLE

        }
        else{
            accuracyIcon.setImageResource(R.drawable.uncheck_circle_template_screen)
            accuracyIconTemplate.visibility = View.INVISIBLE
            accuracyTv.visibility = View.INVISIBLE

        }
    }

    private fun saveMapTypeSelectionState() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("IsMapTypeSelected", isMapTypeSelected)
        editor.apply()
    }

    private fun saveLatLongSelectionState() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("IsLatLongSelected", isLatLongSelected)
        editor.apply()
    }

    private fun savePlusCodeSelectionState(){
        val editor = sharedPreferences.edit()
        editor.putBoolean("IsPlusCodeSelected", isPlusCodeSelected)
        editor.apply()
    }

    private fun saveDateAndTimeSelectedState(){
        val editor = sharedPreferences.edit()
        editor.putBoolean("IsDateAndTimeSelected", isDateAndTimeSelected)
        editor.apply()
    }

    private fun isTimeZoneSelectedState(){
        val editor = sharedPreferences.edit()
        editor.putBoolean("IsTimeZoneSelected", isTimeZoneSelected)
        editor.apply()
    }

    private fun saveNoteHashtagSelectedState(){
        val editor = sharedPreferences.edit()
        editor.putBoolean("isNoteHashtagSelected", isNoteHashtagSelected)
        editor.apply()
    }

    private fun saveWeatherSelectedState(){
        val editor = sharedPreferences.edit()
        editor.putBoolean("isWeatherSelected", isWeatherSelected)
        editor.apply()
    }

    private fun saveWindSelectionState(){
        val editor = sharedPreferences.edit()
        editor.putBoolean("isWindSelected", isWindSelected)
        editor.apply()
    }

    private fun saveHumiditySelectionState(){
        val editor = sharedPreferences.edit()
        editor.putBoolean("isHumiditySelected", isHumiditySelected)
        editor.apply()
    }

    private fun savePressureSelectionState(){
        val editor = sharedPreferences.edit()
        editor.putBoolean("isPressureSelected", isPressureSelected)
        editor.apply()
    }
    private fun saveAltitudeSelected(){
        val editor = sharedPreferences.edit()
        editor.putBoolean("isAltitudeSelected", isAltitudeSelected)
        editor.apply()

    }
    private fun saveAccuracySelected(){
        val editor = sharedPreferences.edit()
        editor.putBoolean("isAccuracySelected", isAccuracySelected)
        editor.apply()
    }

    @SuppressLint("SetTextI18n")
    private fun getLocationData() {
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
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val address = getAddress(location.latitude, location.longitude)
                val city = address.locality
                val province = address.adminArea
                val country = address.countryName

                cityCountryTv.text = "$city, $province, $country"
                longLatTv.text = selectedTextLatLong

                val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                val currentDay = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())
                val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

                dayDateTimeTv.text = "$currentDate, $currentDay, $currentTime"
            }

            val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment_template) as SupportMapFragment
            mapFragment.getMapAsync { googleMap ->
                googleMap.mapType = selectedMapType
            }
        }
    }

    private fun getAddress(latitude: Double, longitude: Double): Address {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)!!
        return addresses[0]
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}