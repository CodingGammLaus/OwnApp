package se.umu.cs.dv21sln.ownapplication

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Rect
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import se.umu.cs.dv21sln.ownapplication.databinding.ActivityGameBinding
import java.util.*

/**
 * This class represent the game screen of the application.
 *
 * How to play: The player starts the game by tapping anywhere on the screen.
 * Control the space ship by tilting the phone to the left or right.
 * Shoot missiles by tapping anywhere on the screen (except the pause button).
 *
 * Info: Earn points by shooting at the meteors, and try to stay at the top of the top list.
 * Pause/exit the game by clicking on the pause-icon in the top right corner.
 *
 * Copyright 2023 Simon Lindgren (dv21sln@cs.umu.se).
 * Usage requires the author's permission.
 *
 * @author Simon Lindgren
 * @since  2023-03-21
 *
 */
class GameActivity: AppCompatActivity(), SensorEventListener {

    /*View binding*/
    private lateinit var binding: ActivityGameBinding

    /*Sensor*/
    private var sensorManager: SensorManager? = null
    private var accSensor: Sensor? = null

    /*The meteor spawn timer*/
    private var meteorTimer = Timer()
    private var gameTimer = Timer()

    /*The objects (space ship, missile and meteor)*/
    private var missile = Rect()
    private var meteor = Rect()
    private var ship = Rect()

    /*The game handler*/
    private var game = GameModel()

    /*Random generator*/
    private val randMeteor = Random()

    /*Boolean to check if the game is paused*/
    private var paused = false


    /**
     * The on create function (Android)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*Get the view binding*/
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpActivity()

        /*Checks if there exist a saved state*/
        if(savedInstanceState != null) {
            intiSavedState(savedInstanceState)
        }

        else {
            val pref = getSharedPreferences("settings", MODE_PRIVATE)
            game.setStartValues(pref)
        }

        /*Set up the game*/
        setDisplaySize()
        loadInLives()

        /*Hide the pause button until the game starts*/
        binding.pauseButton.isVisible = false

        startGame()
    }

    /**
     * Set up the activity
     */
    private fun setUpActivity() {

        /*Hide the app bar*/
        supportActionBar?.hide()

        /*Load in the correct background image*/
        val sharedPref = getSharedPreferences("background", MODE_PRIVATE)
        binding.gameLayout.setBackgroundResource(sharedPref.getInt("pic", R.drawable.background_2))

        /*Set up the accelerometer sensor*/
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    /**
     * Init app with saved state values.
     * @param savedInstanceState The saved state.
     */
    private fun intiSavedState(savedInstanceState: Bundle) {

        super.onRestoreInstanceState(savedInstanceState)

        game.lives = savedInstanceState.getInt("lives")
        game.totalScore = savedInstanceState.getInt("score")
        game.points = savedInstanceState.getInt("points")
        game.shipSpeed = savedInstanceState.getInt("speed")

        binding.score.text = "Score: " + game.totalScore
        binding.tap.text = "Tap to resume!"
        binding.menuTitle.text = "Paused"
        binding.arrow1.setImageResource(0)
        binding.arrow2.setImageResource(0)

        paused = true
        onStop()
    }

    /**
     * Sets the display size to the game.
     */
    private fun setDisplaySize() {

        //(ÄNDRA SÅ INTE 280 ÄR KVAR!!!!!!!!)
        game.screenWidth = resources.displayMetrics.widthPixels - 280
        game.screenHeight = resources.displayMetrics.heightPixels
    }

    /**
     * Check the amount of lives and load in the life bar.
     */
    private fun loadInLives() {

        if(game.lives == 4) {
            binding.lives5.setImageResource(0)
        }

        else if(game.lives == 3) {
            binding.lives5.setImageResource(0)
            binding.lives4.setImageResource(0)
        }

        else if(game.lives == 2) {
            binding.lives5.setImageResource(0)
            binding.lives4.setImageResource(0)
            binding.lives3.setImageResource(0)
        }

        else if(game.lives == 1) {
            binding.lives5.setImageResource(0)
            binding.lives4.setImageResource(0)
            binding.lives3.setImageResource(0)
            binding.lives2.setImageResource(0)
        }
    }

    /**
     * Starting the game when the player tapping the screen.
     */
    private fun startGame() {

        /*Tapping anywhere on the screen*/
        binding.gameLayout.setOnClickListener() {

            removeGameInfo()
            pauseButton()
            shootInit()
            meteor()
            gameHandler()
            onStart()
            paused = false
        }
    }

    /**
     * Removes the game info from the screen.
     */
    private fun removeGameInfo() {

        binding.pauseButton.isVisible = true
        binding.arrow1.setImageResource(0)
        binding.arrow2.setImageResource(0)
        binding.tap.text = ""
        binding.menuTitle.text = ""
    }

    /**
     * Pause button init.
     */
    private fun pauseButton() {

        binding.pauseButton.setOnClickListener() {

            paused = true
            onStop()
            askExit()
        }
    }

    /**
     * Shoot function init, tap anywhere on the screen to shoot (except the pause button).
     */
    private fun shootInit() {

        binding.gameLayout.setOnClickListener() {

            /*Respawn missile at the space ship position*/
            binding.missile.setImageResource(R.drawable.missile)
            binding.missile.x = binding.spaceShip.x + 75
            binding.missile.y = binding.spaceShip.y

            /*Animate the missile movement*/
            ObjectAnimator.ofFloat(binding.missile, "translationY", -2000f).apply {
                duration = 300
                start()
            }
        }
    }

    /**
     * Respawn meteor and make it fall every 3 seconds.
     */
    private fun meteor() {

        binding.metior.setImageDrawable(null)
        binding.metior.setImageResource(0)

        meteorTimer.scheduleAtFixedRate(object : TimerTask() {

            override fun run() {

                if(!paused) {

                    /*Runs on the UI thread*/
                    runOnUiThread {

                        /*Respawn meteor at the top of the screen*/
                        binding.metior.setImageResource(R.drawable.metior)
                        binding.metior.x = randMeteor.nextFloat() * (game.screenWidth)
                        binding.metior.y = -200f

                        /*Animate the meteors movement*/
                        ObjectAnimator.ofFloat(binding.metior, "translationY", 2000f).apply {
                            duration = 2000
                            start()
                        }
                    }
                }
            }
        }, 3000, 3000)
    }

    /**
     * Handles the collisions between ship and meteor, and between meteor and missile.
     * Calls the dead function when player is out of lives.
     * Removes the missile and meteor if they gets out of bounds.
     */
    private fun gameHandler() {

        gameTimer.scheduleAtFixedRate(object : TimerTask() {

            override fun run() {

                if(!paused) {

                    /*Runs on the UI thread*/
                    runOnUiThread {

                        binding.missile.getHitRect(missile)
                        binding.metior.getHitRect(meteor)
                        binding.spaceShip.getHitRect(ship)

                        /*Player dead*/
                        if(game.lives <= 0) {

                            dead()
                        }

                        /*Meteor hits ship*/
                        if(Rect.intersects(ship, meteor)) {

                            game.takeDamage()
                            removeLife()
                            removeMeteor()
                        }

                        /*Missile hits meteor*/
                        else if(Rect.intersects(missile, meteor)) {

                            game.addScore()
                            removeMeteor()
                            removeMissile()
                            binding.score.text = "Score: " + game.totalScore
                        }

                        /*Removes missile*/
                        else if(binding.missile.y < 0) {

                            removeMissile()
                        }

                        /*Remove meteor*/
                        else if(binding.metior.y > game.screenHeight) {

                            removeMeteor()
                        }
                    }
                }
            }
        }, 20, 20)
    }

    /**
     * Remove life from life bar.
     */
    private fun removeLife() {

        when (game.lives) {
            4 -> {
                binding.lives5.setImageResource(0)
            }
            3 -> {
                binding.lives4.setImageResource(0)
            }
            2 -> {
                binding.lives3.setImageResource(0)
            }
            1 -> {
                binding.lives2.setImageResource(0)
            }
            0 -> {
                binding.lives1.setImageResource(0)
            }
        }
    }

    /**
     * Remove meteor
     */
    private fun removeMeteor() {

        binding.metior.setImageDrawable(null)
        binding.metior.setImageResource(0)

        binding.metior.x = -10000f
        binding.metior.y = -10000f
    }

    /**
     * Remove missile.
     */
    private fun removeMissile() {

        binding.missile.setImageDrawable(null)
        binding.missile.setImageResource(0)

        binding.missile.x = 10000f
        binding.missile.y = 10000f
    }

    /**
     * When player die.
     */
    private fun dead() {

        /*Unregister the sensor listener*/
        onStop()

        /*Cancel timers*/
        gameTimer.cancel()
        meteorTimer.cancel()

        val sharedPref = getSharedPreferences("scoreList", MODE_PRIVATE)

        /*Check if player did it to the top list*/
        if(game.checkIfOnTopList(sharedPref)) {
            onTopList()
            return
        }

        notOnTopList()
    }

    /*------------------------------------Alert dialogs-------------------------------------------*/

    /**
     * Ask if player want to quit or keep playing.
     */
    private fun askExit() {

        val alert = AlertDialog.Builder(this, R.style.MyDialogTheme)

        alert.setTitle("Quit")
        alert.setMessage("Do you want to quit current game?")

        alert.setPositiveButton("YES") { _, _ ->

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        alert.setNegativeButton("NO") { _, _ ->

            closeContextMenu()
            onStart()
            paused = false
        }

        alert.create()
        alert.show()
    }

    /**
     * Request to play again if player didn't make it to the top list.
     */
    private fun notOnTopList() {

        val alert = AlertDialog.Builder(this, R.style.MyDialogTheme)

        alert.setTitle("Play again")
        alert.setMessage("You didn't make it to the top list.\nDo you want to try again?")
        alert.setPositiveButton("Yes") {_, _ ->

            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            finish()
        }

        alert.setNegativeButton("No") {_, _ ->

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }.create()

        alert.show()
    }

    /**
     * Player writes name to be added to the top list, and requested to play again.
     */
    private fun onTopList() {

        val sharedPref = getSharedPreferences("scoreList", MODE_PRIVATE)

        val input = EditText(this)

        val alert = AlertDialog.Builder(this, R.style.MyDialogTheme)

        alert.setTitle("Made it to the top list")
        alert.setView(input)
        alert.setMessage("You got " + game.totalScore + " points\nEnter your name for the top list")
        alert.setPositiveButton("Play Again") { dialog, _ ->

            dialog.cancel()
            game.addScoreToScoreList(input.text.toString(), sharedPref)

            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            finish()
        }

        alert.setNegativeButton("Exit") {_, _ ->

            game.addScoreToScoreList(input.text.toString(), sharedPref)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }.create()

        alert.show()
    }

    /*------------------------------------Override functions--------------------------------------*/

    /**
     * Moving the space ship when the phone is tilting.
     */
    override fun onSensorChanged(sensor: SensorEvent?) {

        val x: Float = sensor!!.values[0]

        /*Turing Left*/
        if(x > 0.5 && binding.spaceShip.x >= 0) {

            binding.spaceShip.x -= (x * game.shipSpeed)
        }

        /*Turning Right*/
        else if(x < -0.5 && binding.spaceShip.x < game.screenWidth) {

            binding.spaceShip.x -= (x * game.shipSpeed)
        }
    }

    /**
     * Auto function.
     */
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // TODO Auto-generated method stub
    }

    /**
     * Register the sensor listener, on the android start function.
     */
    override fun onStart() {
        super.onStart()
        sensorManager?.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_GAME)
    }

    /**
     * Unregister the sensor listener, on the android stop function.
     */
    override fun onStop() {
        super.onStop()
        sensorManager?.unregisterListener(this, accSensor)
    }

    /**
     * Pause the game when phones back button is pressed.
     */
    override fun onBackPressed() {
        askExit()
    }

    /**
     * Save current state if app crashes.
     * @param outState Where to be save.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("lives", game.lives)
        outState.putInt("score", game.totalScore)
        outState.putInt("speed", game.shipSpeed)
        outState.putInt("points", game.points)
    }
}