package se.umu.cs.dv21sln.ownapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import se.umu.cs.dv21sln.ownapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var health = 3
    private var points = 1
    private var speed = 2

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val score = intent.getIntExtra("score", 0)

        getSettingsValues()

        play()

        stats()

        settings()
    }

    /**
     *
     */
    private fun getSettingsValues() {

        health = intent.getIntExtra("health", 3)
        points = intent.getIntExtra("points", 1)
        speed = intent.getIntExtra("speed", 2)

        binding.menuTitle.text = "Health: " + health +
                "\nPoints: " + points +
                "\nSpeed: " + speed
    }

    /**
     * Play init
     */
    private fun play() {

        binding.playButton.setOnClickListener() {

            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("health", health)
            intent.putExtra("points", points)
            intent.putExtra("speed", speed)
            startActivity(intent)
            finish()
        }
    }

    /**
     * Stats init
     */
    private fun stats() {

        binding.statsButton.setOnClickListener() {

            val intent = Intent(this, StatsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /**
     * Settings init
     */
    private fun settings() {

        binding.settingsButton.setOnClickListener() {

            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
