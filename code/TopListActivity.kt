package se.umu.cs.dv21sln.ownapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import se.umu.cs.dv21sln.ownapplication.databinding.ActivityTopListBinding

/**
 * This class represent the top list screen of the application. The top list consists of the top 5
 * player, with their name and score. The name sets after a game round, when the player die.
 * If a player gets high score they will be at the top of the list (place 1) and all the other player
 * will move down one step.
 * The top list can be reset in the settings screen.
 *
 * Copyright 2023 Simon Lindgren (dv21sln@cs.umu.se).
 * Usage requires the author's permission.
 *
 * @author Simon Lindgren
 * @since  2023-03-21
 *
 */
class TopListActivity : AppCompatActivity()  {

    /*View binding*/
    private lateinit var binding: ActivityTopListBinding

    /**
     * The on create function (Android)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*Get the view binding*/
        binding = ActivityTopListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpActivity()
        setUpTopList()
    }

    /**
     * Set up the activity
     */
    private fun setUpActivity() {

        /*Set name and back button to action bar*/
        supportActionBar?.title = "Top List"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /*Load in the correct background image*/
        val sharedPref = getSharedPreferences("background", MODE_PRIVATE)
        binding.main.setBackgroundResource(sharedPref.getInt("pic", R.drawable.background_2))
    }

    /**
     * Setting up the top list, with the name and the score.
     */
    private fun setUpTopList() {

        /*Get the names and the scores*/
        val sharedPref = getSharedPreferences("scoreList", MODE_PRIVATE)

        /*Set the names*/
        binding.name1.text = sharedPref.getString("name1", "-")
        binding.name2.text = sharedPref.getString("name2", "-")
        binding.name3.text = sharedPref.getString("name3", "-")
        binding.name4.text = sharedPref.getString("name4", "-")
        binding.name5.text = sharedPref.getString("name5", "-")

        /*Set the scores*/
        binding.score1.text = sharedPref.getInt("score1", 0).toString()
        binding.score2.text = sharedPref.getInt("score2", 0).toString()
        binding.score3.text = sharedPref.getInt("score3", 0).toString()
        binding.score4.text = sharedPref.getInt("score4", 0).toString()
        binding.score5.text = sharedPref.getInt("score5", 0).toString()
    }
}