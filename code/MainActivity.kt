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

    private var score = 0

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSettingsValues()

        play()

        stats()

        settings()

        /*val sharedPref = getSharedPreferences("scoreList", MODE_PRIVATE)
        score = sharedPref.getInt("score1", 0)

        binding.setting.text = "Highscore: " + score*/
    }

    /**
     *
     */
    private fun getSettingsValues() {

        val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)
        health = sharedPref.getInt("health", 0)
        points = sharedPref.getInt("points", 0)
        speed = sharedPref.getInt("speed", 0)

        binding.setting.text = "Health: " + health +
                "\nPoints: " + points +
                "\nSpeed: " + speed
    }

    /**
     * Play init
     */
    private fun play() {

        binding.playButton.setOnClickListener() {

            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Stats init
     */
    private fun stats() {

        binding.statsButton.setOnClickListener() {

            val intent = Intent(this, StatsActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Settings init
     */
    private fun settings() {

        binding.settingsButton.setOnClickListener() {

            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        /*val sharedPref = getSharedPreferences("scoreList", MODE_PRIVATE)

        val editor = sharedPref.edit()
        editor.putInt("highScore", score)
        editor.commit()*/
    }
}
