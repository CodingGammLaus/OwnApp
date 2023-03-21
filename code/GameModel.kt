package se.umu.cs.dv21sln.ownapplication

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

/**
 * This class represent the game model of the application. Handles the game data.
 * The size of the screen (width and height), keep track of the scores of the rounds,
 * the lives, the points for each meteor hit and the speed of the space ship.
 * Checks if a player did make it to the top list or not, add the player to the top list if they
 * did make it.
 *
 * Copyright 2023 Simon Lindgren (dv21sln@cs.umu.se).
 * Usage requires the author's permission.
 *
 * @author Simon Lindgren
 * @since  2023-03-21
 *
 */
class GameModel: AppCompatActivity() {

    /*The screen size (width and height)*/
    var screenWidth = 0
    var screenHeight = 0

    /*Total score of the round and the amount of damage*/
    var totalScore = 0
    var damage = 1

    /*The game values*/
    var lives = 3
    var points = 5
    var shipSpeed = 2

    /**
     * Set the starting values to the game (lives, points, speed)
     * @param pref The game options.
     */
    fun setStartValues(pref: SharedPreferences) {

        lives = pref.getInt("lives", 3)
        points = pref.getInt("points", 5)
        shipSpeed = pref.getInt("speed", 2)
    }

    /**
     * The player takes damage.
     */
    fun takeDamage() {

        lives -= damage
    }

    /**
     * Add points to score.
     */
    fun addScore() {

        totalScore += points
    }

    /**
     * Checks if player did make it to the top list.
     * @param pref The top list.
     * @return True if player did make it to the top list, false if not.
     */
    fun checkIfOnTopList(pref: SharedPreferences): Boolean {

        val arr = arrayOf<Int>(
            pref.getInt("score1", 0),
            pref.getInt("score2", 0),
            pref.getInt("score3", 0),
            pref.getInt("score4", 0),
            pref.getInt("score5", 0)
        )

        for(i in 0..4) {

            if(totalScore >= arr[i] && totalScore > 0) {

                return true
            }
        }

        return false
    }

    /**
     * Add the player with their score to the top list.
     * @param name The player name.
     * @param pref The top list.
     */
    fun addScoreToScoreList(name: String, pref: SharedPreferences) {

        val arr = arrayOf<Int>(
            pref.getInt("score1", 0),
            pref.getInt("score2", 0),
            pref.getInt("score3", 0),
            pref.getInt("score4", 0),
            pref.getInt("score5", 0),
            0
        )

        /*Add the new score and sort the scores*/
        arr[5] = totalScore
        arr.sortDescending()

        /*Add the score to the top list*/
        val editor = pref.edit()
        editor.putInt("score1", arr[0])
        editor.putInt("score2", arr[1])
        editor.putInt("score3", arr[2])
        editor.putInt("score4", arr[3])
        editor.putInt("score5", arr[4])

        /*Set name to Unknown if player leave name field empty*/
        var newName = name
        if(newName == "") {
            newName = "Unknown"
        }

        /*Checks where to put the name in the top list*/
        if(totalScore == arr[0]) {

            editor.putString("name5", pref.getString("name4", "-"))
            editor.putString("name4", pref.getString("name3", "-"))
            editor.putString("name3", pref.getString("name2", "-"))
            editor.putString("name2", pref.getString("name1", "-"))
            editor.putString("name1", newName)
        }

        else if(totalScore == arr[1]) {

            editor.putString("name5", pref.getString("name4", "-"))
            editor.putString("name4", pref.getString("name3", "-"))
            editor.putString("name3", pref.getString("name2", "-"))
            editor.putString("name2", newName)
        }

        else if(totalScore == arr[2]) {

            editor.putString("name5", pref.getString("name4", "-"))
            editor.putString("name4", pref.getString("name3", "-"))
            editor.putString("name3", newName)
        }

        else if(totalScore == arr[3]) {

            editor.putString("name5", pref.getString("name4", "-"))
            editor.putString("name4", newName)

        }

        else if(totalScore == arr[4]) {

            editor.putString("name5", newName)
        }

        editor.apply()
    }
}