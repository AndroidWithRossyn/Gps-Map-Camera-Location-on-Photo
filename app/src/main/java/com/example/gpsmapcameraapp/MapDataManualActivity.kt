package com.example.gpsmapcameraapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.airbnb.lottie.LottieAnimationView

class MapDataManualActivity : AppCompatActivity() {

    private lateinit var locationAnimationView: LottieAnimationView
    private lateinit var backIcon : ImageView
    private lateinit var dropDownIcon : ImageView
    private var isDropDownIconDown = true
    private lateinit var addMoreIcon : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_data_manual)

        addMoreIcon = findViewById(R.id.add_more_icon_1)

        locationAnimationView = findViewById(R.id.locationanimation)
        locationAnimationView.visibility = View.VISIBLE
        locationAnimationView.playAnimation()

        backIcon = findViewById(R.id.back_icon_3)
        backIcon.setOnClickListener {
            Toast.makeText(this, "You may add any one manual location", Toast.LENGTH_LONG).show()
        }

        dropDownIcon = findViewById(R.id.down_icon_1)
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

        addMoreIcon.setOnClickListener {
            val intent = Intent(this, MapDataManualEntryActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "Toast.makeText(this, \"You may add any one manual location\", Toast.LENGTH_LONG).show()",
        "android.widget.Toast",
        "android.widget.Toast"
    )
    )
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        Toast.makeText(this, "You may add any one manual location", Toast.LENGTH_LONG).show()
    }
}