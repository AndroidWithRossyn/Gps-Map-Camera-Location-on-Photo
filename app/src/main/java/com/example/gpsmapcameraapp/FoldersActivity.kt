package com.example.gpsmapcameraapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class FoldersActivity : AppCompatActivity() {

    private lateinit var backIcon: ImageView
    private lateinit var folderCardDefault: ImageView
    private var folderClickListener: FolderClickListener? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var isDefaultFolderSelected = true
    private lateinit var siteOneCard : ImageView
    private var isSiteOneFolderSelected = true
    private lateinit var siteTwoCard : ImageView
    private var isSiteTwoFolderSelected = true

    @SuppressLint("InflateParams", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folders)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        siteOneCard = findViewById(R.id.folders_card_2)
        siteTwoCard = findViewById(R.id.folders_card_3)

       siteOneCard.setOnClickListener {
            isSiteOneFolderSelected = !isSiteOneFolderSelected
            updateSiteOneFolderCard()
            isDefaultFolderSelected = false
           isSiteTwoFolderSelected = false
            updateFolderCard()
           updateSiteTwoCardFolder()
        }

        siteTwoCard.setOnClickListener {
            isSiteTwoFolderSelected = !isSiteTwoFolderSelected
            updateSiteTwoCardFolder()
            isDefaultFolderSelected = false
            isSiteOneFolderSelected = false
            updateSiteOneFolderCard()
            updateFolderCard()
        }

        folderCardDefault = findViewById(R.id.folders_card_1)
        isDefaultFolderSelected = sharedPreferences.getBoolean("isDefaultFolderSelected", false)
        updateFolderCard()

        isSiteOneFolderSelected = sharedPreferences.getBoolean("isSiteOneFolderSelected", false)
        updateSiteOneFolderCard()

        isSiteTwoFolderSelected = sharedPreferences.getBoolean("isSiteTwoFolderSelected", false)
        updateSiteTwoCardFolder()

        folderCardDefault.setOnClickListener {
            isDefaultFolderSelected = !isDefaultFolderSelected
            updateFolderCard()
            isSiteOneFolderSelected = false
            updateSiteOneFolderCard()
            isSiteTwoFolderSelected = false
            updateSiteTwoCardFolder()
        }

        backIcon = findViewById(R.id.back_icon_1)
        backIcon.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun updateFolderCard() {
        if (isDefaultFolderSelected) {
            folderCardDefault.setImageResource(R.drawable.selected_folder_card)
        } else {
            folderCardDefault.setImageResource(R.drawable.folders_card)
        }

        // Store the updated state in SharedPreferences
        sharedPreferences.edit().putBoolean("isDefaultFolderSelected", isDefaultFolderSelected).apply()
    }

    private fun updateSiteOneFolderCard() {
        if (isSiteOneFolderSelected) {
            siteOneCard.setImageResource(R.drawable.selected_folder_card)
        } else {
            siteOneCard.setImageResource(R.drawable.folders_card)
        }

        // Store the updated state in SharedPreferences
        sharedPreferences.edit().putBoolean("isSiteOneFolderSelected", isSiteOneFolderSelected).apply()
   }

    private fun updateSiteTwoCardFolder() {
    if (isSiteTwoFolderSelected) {
        siteTwoCard.setImageResource(R.drawable.selected_folder_card)
    }   else{
        siteTwoCard.setImageResource(R.drawable.folders_card)
    }
        sharedPreferences.edit().putBoolean("isSiteTwoFolderSelected", isSiteTwoFolderSelected).apply()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    interface FolderClickListener {
        fun onFolderClicked()
    }

    fun setFolderClickListener(listener: FolderClickListener) {
        folderClickListener = listener
    }


}