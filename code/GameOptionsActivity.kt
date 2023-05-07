package se.umu.cs.dv21sln.ownapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import se.umu.cs.dv21sln.ownapplication.databinding.ActivityGameOptionsBinding

/**
 * This class represent the game options screen of the application. The options are changing the
 * lives, points and speed. All the values have 3 different choices.
 *
 * - Lives represent the lives the player have, with the values 1, 3 or 5
 * - Points represent the points the player gets when shooting meteors, with the values 1, 5 or 10
 * - Speed represent the speed of the space ship, with the values slow, medium or fast.
 *
 * Copyright 2023 Simon Lindgren (dv21sln@cs.umu.se).
 * Usage requires the author's permission.
 *
 * @author Simon Lindgren
 * @since  2023-05-07
 *
 */
class GameOptionsActivity: AppCompatActivity() {

    /*View binding*/
    private lateinit var binding: ActivityGameOptionsBinding

    /*Values for the game (lives, points and speed)*/
    private var lives = 3
    private var points = 5
    private var speed = 2

    /**
     * The on create function (Android)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*Get the view binding*/
        binding = ActivityGameOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpActivity()
        getValues()
        setLives()
        setPoints()
        setSpeed()
        applyButton()
    }

    /**
     * Set up the activity
     */
    private fun setUpActivity() {

        /*Set name and back button to action bar*/
        supportActionBar?.title = "Game options"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /*Load in the correct background image*/
        val sharedPref = getSharedPreferences("background", MODE_PRIVATE)
        binding.main.setBackgroundResource(sharedPref.getInt("pic", R.drawable.background_2))
    }

    /**
     * Load in the saved values.
     */
    private fun getValues() {

        val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)
        lives = sharedPref.getInt("lives", 3)
        points = sharedPref.getInt("points", 5)
        speed = sharedPref.getInt("speed", 2)

        getLives()
        getPoints()
        getSpeed()
    }

    /**
     * Mark the in loaded lives value.
     */
    private fun getLives() {

        if(lives == 1) {

            binding.health1.isChecked = true
        }

        else if (lives == 3) {

            binding.health2.isChecked = true
        }

        else if (lives == 5) {

            binding.health3.isChecked = true
        }
    }

    /**
     * Mark the in loaded points value.
     */
    private fun getPoints() {

        if(points == 1) {

            binding.points1.isChecked = true
        }

        else if (points == 5) {

            binding.points2.isChecked = true
        }

        else if (points == 10) {

            binding.points3.isChecked = true
        }
    }

    /**
     * Mark the in loaded speed value.
     */
    private fun getSpeed() {

        if(speed == 2) {

            binding.speed1.isChecked = true
        }

        else if (speed == 4) {

            binding.speed2.isChecked = true
        }

        else if (speed == 8) {

            binding.speed3.isChecked = true
        }
    }

    /**
     * Set the lives value.
     */
    private fun setLives() {

        binding.healthGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.health_1 -> {

                    lives = 1
                }
                R.id.health_2 -> {

                    lives = 3
                }
                R.id.health_3 -> {

                    lives = 5
                }
            }
        }
    }

    /**
     * Set the points value.
     */
    private fun setPoints() {

        binding.pointsGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.points_1 -> {

                    points = 1
                }
                R.id.points_2 -> {

                    points = 5
                }
                R.id.points_3 -> {

                    points = 10
                }
            }
        }
    }

    /**
     * Set the speed value.
     */
    private fun setSpeed() {

        binding.speedGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.speed_1 -> {

                    speed = 2
                }
                R.id.speed_2 -> {

                    speed = 4
                }
                R.id.speed_3 -> {

                    speed = 8
                }
            }
        }
    }

    /**
     * Apply button init. Saves the marked values and returns to main screen.
     */
    private fun applyButton() {

        binding.settingsButton.setOnClickListener() {

            val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putInt("lives", lives)
            editor.putInt("points", points)
            editor.putInt("speed", speed)
            editor.apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}