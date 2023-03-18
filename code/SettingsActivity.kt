package se.umu.cs.dv21sln.ownapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import se.umu.cs.dv21sln.ownapplication.databinding.ActivitySettingsBinding

class SettingsActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    /*Starting values*/
    private var health = 3
    private var points = 1
    private var speed = 2

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Settings"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getValues()

        setHealth()

        setPoints()

        setSpeed()

        button()
    }

    /**
     * Get the values.
     */
    private fun getValues() {

        val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)
        health = sharedPref.getInt("health", 0)
        points = sharedPref.getInt("points", 0)
        speed = sharedPref.getInt("speed", 0)

        getHealth()
        getPoints()
        getSpeed()
    }

    /**
     * Set the health value.
     */
    private fun setHealth() {

        binding.healthGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.health_1 -> {

                    health = 1
                }
                R.id.health_2 -> {

                    health = 3
                }
                R.id.health_3 -> {

                    health = 5
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
     *
     */
    private fun button() {

        binding.settingsButton.setOnClickListener() {

            val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putInt("health", health)
            editor.putInt("points", points)
            editor.putInt("speed", speed)
            editor.apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /**
     * Get the health value.
     */
    private fun getHealth() {

        if(health == 1) {

            binding.health1.isChecked = true
        }

        else if (health == 3) {

            binding.health2.isChecked = true
        }

        else if (health == 5) {

            binding.health3.isChecked = true
        }
    }

    /**
     * Get the points value
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
     * Get the speed value.
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
}