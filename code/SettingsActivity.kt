package se.umu.cs.dv21sln.ownapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import se.umu.cs.dv21sln.ownapplication.databinding.ActivitySettingsBinding

class SettingsActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private var health = 100
    private var points = 1
    private var speed = 2

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setHealth()

        setPoints()

        setSpeed()

        back()
    }

    /**
     *
     */
    private fun setHealth() {

        binding.healthGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.health_1 -> {

                    health = 100
                }
                R.id.health_2 -> {

                    health = 250
                }
                R.id.health_3 -> {

                    health = 500
                }
            }
        }
    }

    /**
     *
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
     *
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
     * Back to main screen with the marked choices.
     */
    private fun back() {

        binding.backButton.setOnClickListener() {

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("health", health)
            intent.putExtra("points", points)
            intent.putExtra("speed", speed)
            startActivity(intent)
            finish()
        }
    }
}