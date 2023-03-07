package se.umu.cs.dv21sln.ownapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import se.umu.cs.dv21sln.ownapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        play()

        stats()

        settings()
    }

    /**
     * Play init
     */
    private fun play() {

        binding.playButton.setOnClickListener() {

            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /**
     * Stats init
     */
    private fun stats() {

        binding.statsButton.setOnClickListener() {

            binding.menuTitle.text = "Stats"
        }
    }

    /**
     * Settings init
     */
    private fun settings() {

        binding.settingsButton.setOnClickListener() {

            binding.menuTitle.text = "Settings"
        }
    }
}
