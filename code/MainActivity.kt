package se.umu.cs.dv21sln.ownapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import se.umu.cs.dv21sln.ownapplication.databinding.ActivityMainBinding

/**
 * This class represent the main screen of the application. The screen contains of 4 options, play,
 * top list, game options and settings.
 *
 * - Play starts the game for the player.
 * - Top list shows the top 5 player with their score.
 * - Game options let the player change the amount of lives, the counting of the points and the space
 * ship speed.
 * - Settings let the player change background and reset the top list.
 *
 * Copyright 2023 Simon Lindgren (dv21sln@cs.umu.se).
 * Usage requires the author's permission.
 *
 * @author Simon Lindgren
 * @since  2023-05-07
 *
 */
class MainActivity : AppCompatActivity() {

    /*View binding*/
    private lateinit var binding: ActivityMainBinding

    /*Represent the highest score*/
    private var highScore = 0

    /**
     * The on create function (Android)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*Get the view binding*/
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setHighScore()
        playButton()
        topListButton()
        gameOptionsButton()
    }

    /**
     * Set the high score text.
     */
    private fun setHighScore() {

        val sharedPref = getSharedPreferences("scoreList", MODE_PRIVATE)
        highScore = sharedPref.getInt("score1", 0)

        binding.highscore.text = "Highscore: " + highScore
    }

    /**
     * Play button init.
     */
    private fun playButton() {

        binding.playButton.setOnClickListener() {

            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Top list button init.
     */
    private fun topListButton() {

        binding.statsButton.setOnClickListener() {

            val intent = Intent(this, TopListActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Game options button init.
     */
    private fun gameOptionsButton() {

        binding.settingsButton.setOnClickListener() {

            val intent = Intent(this, GameOptionsActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Open and uses the created menu bar from res/menu.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Set actions to the setting icon in the menu bar.
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
     * Load in the correct background image.
     */
    override fun onResume() {
        super.onResume()

        val sharedPref = getSharedPreferences("background", MODE_PRIVATE)
        binding.main.setBackgroundResource(sharedPref.getInt("pic", R.drawable.background_2))
    }

    /**
     * Asks if the player wants to exit the application when uses the phones back navigation in the
     * main screen.
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
