package se.umu.cs.dv21sln.ownapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import se.umu.cs.dv21sln.ownapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var highScore = 0

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        play()

        stats()

        gameOptions()

        setHighScore()
    }

    /**
     * Set the High score text.
     */
    private fun setHighScore() {

        val sharedPref = getSharedPreferences("scoreList", MODE_PRIVATE)
        highScore = sharedPref.getInt("score1", 0)

        binding.highscore.text = "Highscore: " + highScore
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

            val intent = Intent(this, TopListActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Game options init
     */
    private fun gameOptions() {

        binding.settingsButton.setOnClickListener() {

            val intent = Intent(this, GameOptionsActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     *
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     *
     */
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.settings -> {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    /**
     *
     */
    override fun onResume() {
        super.onResume()

        val sharedPref = getSharedPreferences("background", MODE_PRIVATE)
        binding.main.setBackgroundResource(sharedPref.getInt("pic", R.drawable.background_2))
    }

    /**
     *
     */
    override fun onBackPressed() {

        val alert = AlertDialog.Builder(this, R.style.MyDialogTheme)

        alert.setTitle("Exit application")
        alert.setMessage("Do you want to exit the application?")
        alert.setPositiveButton("Yes") {_, _ ->

            finish()
        }

        alert.setNegativeButton("No") {_, _ ->

            closeContextMenu()
        }.create()

        alert.show()
    }
}
