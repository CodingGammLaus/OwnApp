package se.umu.cs.dv21sln.ownapplication

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import se.umu.cs.dv21sln.ownapplication.databinding.ActivitySettingsBinding

/**
 * This class represent the settings screen of the application. The options are change background
 * and reset the top list. The player can choose between four different backgrounds, and when a
 * background is applied it will change for every activity. When the player click on reset the top
 * list button, a dialog will ask if they want to reset the top list.
 *
 * Copyright 2023 Simon Lindgren (dv21sln@cs.umu.se).
 * Usage requires the author's permission.
 *
 * @author Simon Lindgren
 * @since  2023-03-21
 *
 */
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    /**
     * The on create function (Android)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*Get the view binding*/
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpActivity()
        applyButtons()
        resetButton()
    }

    /**
     * Set up the activity
     */
    private fun setUpActivity() {

        /*Set name and back button to action bar*/
        supportActionBar?.title = "Settings"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /*Load in the correct background image*/
        val sharedPref = getSharedPreferences("background", MODE_PRIVATE)
        binding.main.setBackgroundResource(sharedPref.getInt("pic", R.drawable.background_2))
    }

    /**
     * Apply buttons init. Init all the buttons for the background changing.
     */
    private fun applyButtons() {


        /*Change to background image 1*/
        binding.backgroundButton1.setOnClickListener() {

            binding.main.setBackgroundResource(R.drawable.background_1)
            changeBackground(R.drawable.background_1)
        }

        /*Change to background image 2*/
        binding.backgroundButton2.setOnClickListener() {

            binding.main.setBackgroundResource(R.drawable.background_2)
            changeBackground(R.drawable.background_2)
        }

        /*Change to background image 3*/
        binding.backgroundButton3.setOnClickListener() {

            binding.main.setBackgroundResource(R.drawable.background_3)
            changeBackground(R.drawable.background_3)
        }

        /*Change to background image 4*/
        binding.backgroundButton4.setOnClickListener() {

            binding.main.setBackgroundResource(R.drawable.background_4)
            changeBackground(R.drawable.background_4)
        }
    }

    /**
     * Change the background image, and save the changes.
     */
    private fun changeBackground(image: Int) {

        val sharedPref = getSharedPreferences("background", MODE_PRIVATE)
        val editor = sharedPref.edit()

        binding.main.setBackgroundResource(image)

        editor.putInt("pic", image)
        editor.apply()
    }

    /**
     * Reset button init.
     */
    private fun resetButton() {

        binding.resetStats.setOnClickListener() {

            /*Dialog asks if the player want to reset the top list*/
            val alert = AlertDialog.Builder(this, R.style.MyDialogTheme)

            alert.setTitle("Reset score")
            alert.setMessage("Do you want to reset the top list?")

            alert.setPositiveButton("YES") { _, _ ->

                resetTopList()
            }

            alert.setNegativeButton("NO") { _, _ ->

                closeContextMenu()
            }

            alert.create()
            alert.show()
        }
    }

    /**
     * Resets the top list.
     */
    private fun resetTopList() {

        /*Load in the top list*/
        val sharedPref = getSharedPreferences("scoreList", MODE_PRIVATE)
        val editor = sharedPref.edit()

        /*Set names to '-'*/
        editor.putString("name1", "-")
        editor.putString("name2", "-")
        editor.putString("name3", "-")
        editor.putString("name4", "-")
        editor.putString("name5", "-")

        /*Set scores to 0*/
        editor.putInt("score1", 0)
        editor.putInt("score2", 0)
        editor.putInt("score3", 0)
        editor.putInt("score4", 0)
        editor.putInt("score5", 0)

        editor.apply()
    }
}