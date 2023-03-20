package se.umu.cs.dv21sln.ownapplication

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Rect
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import se.umu.cs.dv21sln.ownapplication.databinding.ActivityGameBinding
import java.util.*

class GameActivity: AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityGameBinding

    private var sensorManager: SensorManager? = null
    private var gyroSensor: Sensor? = null

    private var screenWidth = 0
    private var screenHeight = 0

    private val randMeteor = Random()

    private var meteorTimer = Timer()
    private var gameTimer = Timer()

    //private val game = Game(this)

    private var missile = Rect()
    private var meteor = Rect()
    private var ship = Rect()

    /*Game options*/
    private var startHealth = 4
    private var score = 0
    private var points = 5
    private var health = 3
    private var damage = 1
    private var shipSpeed = 2

    private var paused = false


    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val sharedPref = getSharedPreferences("background", MODE_PRIVATE)
        binding.gameLayout.setBackgroundResource(sharedPref.getInt("pic", R.drawable.background_2))

        setDisplaySizeToGame()
        getStartValues()
        loadInLife()

        binding.pauseButton.isVisible = false

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        gyroSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        binding.gameLayout.setOnClickListener() {

            binding.pauseButton.isVisible = true
            binding.arrow1.setImageResource(0)
            binding.arrow2.setImageResource(0)
            binding.tap.text = ""
            binding.menuTitle.text = ""

            shoot()

            meteor()

            pauseButton()

            gameHandler()
        }
    }

    /**
     * Sets the display size to the game.
     */
    private fun setDisplaySizeToGame() {

        //(ÄNDRA SÅ INTE 280 ÄR KVAR!!!!!!!!)
        screenWidth = resources.displayMetrics.widthPixels - 280
        screenHeight = resources.displayMetrics.heightPixels
    }

    /**
     *
     */
    private fun getStartValues() {

        val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)
        startHealth = sharedPref.getInt("health", 3)
        points = sharedPref.getInt("points", 5)
        shipSpeed = sharedPref.getInt("speed", 2)

        health = startHealth
    }

    /**
     *
     */
    override fun onStart() {
        super.onStart()
        sensorManager?.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_GAME)
    }

    /**
     *
     */
    override fun onStop() {
        super.onStop()
        sensorManager?.unregisterListener(this, gyroSensor)
    }

    /**
     *
     */
    override fun onSensorChanged(sensor: SensorEvent?) {

        val x: Float = sensor!!.values[0]

        //Turing Left
        if(x > 0.5 && binding.spaceShip.x >= 0) {

            binding.spaceShip.x -= (x * shipSpeed)
        }

        //Turning Right
        else if(x < -0.5 && binding.spaceShip.x < screenWidth) {

            binding.spaceShip.x -= (x * shipSpeed)
        }

        //Standing still
        else if(x >= (-0.5) && x <= (0.5)) {

        }
    }

    /**
     *
     */
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // TODO Auto-generated method stub
    }

    /**
     * Shoot function init.
     */
    private fun shoot() {

        binding.gameLayout.setOnClickListener() {

            binding.missile.setImageResource(R.drawable.missile)

            binding.missile.x = binding.spaceShip.x + 75
            binding.missile.y = binding.spaceShip.y

            ObjectAnimator.ofFloat(binding.missile, "translationY", -2000f).apply {
                duration = 300
                start()
            }
        }
    }

    /**
     * Spawn meteors.
     */
    private fun meteor() {

        binding.metior.setImageDrawable(null)
        binding.metior.setImageResource(0)

        meteorTimer.scheduleAtFixedRate(object : TimerTask() {

            override fun run() {

                if(!paused) {

                    /*Runs on the UI thread*/
                    runOnUiThread {
                        binding.metior.setImageResource(R.drawable.metior)

                        binding.metior.x = randMeteor.nextFloat() * (screenWidth)
                        binding.metior.y = -200f

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
     *
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

                        //Player dead
                        if(health <= 0) {

                            cancel()
                            onStop()
                            gameTimer.cancel()
                            meteorTimer.cancel()
                            checkIfOnTopList()
                        }

                        //Meteor hits ship
                        if(Rect.intersects(ship, meteor)) {

                            health -= damage

                            removeLife()

                            deleteMeteor()
                        }

                        //Rocket hits meteor
                        else if(Rect.intersects(missile, meteor)) {

                            score += points
                            binding.score.text = "Score: " + score

                            deleteMeteor()
                            deleteMissile()
                        }

                        //Rocket disappear
                        else if(binding.missile.y < 0) {

                            deleteMissile()
                        }

                        //Meteor disappear
                        else if(binding.metior.y > screenHeight) {

                            deleteMeteor()
                        }
                    }
                }
            }
        }, 20, 20)
    }

    /**
     * Load in life bar.
     */
    private fun loadInLife() {

        if (health == 3) {
            binding.life5.setImageResource(0)
            binding.life4.setImageResource(0)
        }

        else if(health == 1) {
            binding.life5.setImageResource(0)
            binding.life4.setImageResource(0)
            binding.life3.setImageResource(0)
            binding.life2.setImageResource(0)
        }
    }

    /**
     * Remove life from life bar.
     */
    private fun removeLife() {

        when (health) {
            4 -> {
                binding.life5.setImageResource(R.drawable.favorite_border_24)
            }
            3 -> {
                binding.life4.setImageResource(R.drawable.favorite_border_24)
            }
            2 -> {
                binding.life3.setImageResource(R.drawable.favorite_border_24)
            }
            1 -> {
                binding.life2.setImageResource(R.drawable.favorite_border_24)
            }
            0 -> {
                binding.life1.setImageResource(R.drawable.favorite_border_24)
            }
        }
    }

    /**
     * Delete Meteor
     */
    private fun deleteMeteor() {

        binding.metior.setImageDrawable(null)
        binding.metior.setImageResource(0)

        binding.metior.x = -10000f
        binding.metior.y = -10000f
    }

    /**
     * Delete missile.
     */
    private fun deleteMissile() {

        binding.missile.setImageDrawable(null)
        binding.missile.setImageResource(0)

        binding.missile.x = 10000f
        binding.missile.y = 10000f
    }

    /**
     * Pause button init.
     */
    private fun pauseButton() {

        binding.pauseButton.setOnClickListener() {

            pause()
        }
    }

    /**
     * Pause the game when back button is pressed.
     */
    override fun onBackPressed() {
        pause()
    }

    /**
     * Pause game, and ask if player want to quit or keep playing.
     */
    private fun pause() {

        onStop()
        paused = true

        val builder = AlertDialog.Builder(this, R.style.MyDialogTheme)

        builder.setTitle("Quit")
        builder.setMessage("Do you want to quit current game?")

        builder.setPositiveButton("YES") {_, _ ->

            backToMenu()
        }

        builder.setNegativeButton("NO") {_, _ ->

            closeContextMenu()
            onStart()
            paused = false
        }

        builder.create()
        builder.show()
    }

    /**
     *
     */
    private fun addScoreToScoreList(name: String) {

        val sharedPref = getSharedPreferences("scoreList", MODE_PRIVATE)

        val arr = arrayOf<Int>(
            sharedPref.getInt("score1", 0),
            sharedPref.getInt("score2", 0),
            sharedPref.getInt("score3", 0),
            sharedPref.getInt("score4", 0),
            sharedPref.getInt("score5", 0),
            0
        )

        arr[5] = score
        arr.sortDescending()

        val editor = sharedPref.edit()
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

        if(score == arr[0]) {

            editor.putString("name5", sharedPref.getString("name4", "-"))
            editor.putString("name4", sharedPref.getString("name3", "-"))
            editor.putString("name3", sharedPref.getString("name2", "-"))
            editor.putString("name2", sharedPref.getString("name1", "-"))
            editor.putString("name1", newName)
        }

        else if(score == arr[1]) {

            editor.putString("name5", sharedPref.getString("name4", "-"))
            editor.putString("name4", sharedPref.getString("name3", "-"))
            editor.putString("name3", sharedPref.getString("name2", "-"))
            editor.putString("name2", newName)
        }

        else if(score == arr[2]) {

            editor.putString("name5", sharedPref.getString("name4", "-"))
            editor.putString("name4", sharedPref.getString("name3", "-"))
            editor.putString("name3", newName)
        }

        else if(score == arr[3]) {

            editor.putString("name5", sharedPref.getString("name4", "-"))
            editor.putString("name4", newName)

        }

        else if(score == arr[4]) {

            editor.putString("name5", newName)
        }

        editor.apply()
    }

    /**
     * Checks if player did make it to the top list.
     */
    private fun checkIfOnTopList() {

        val sharedPref = getSharedPreferences("scoreList", MODE_PRIVATE)

        val arr = arrayOf<Int>(
            sharedPref.getInt("score1", 0),
            sharedPref.getInt("score2", 0),
            sharedPref.getInt("score3", 0),
            sharedPref.getInt("score4", 0),
            sharedPref.getInt("score5", 0)
        )

        for(i in 0..4) {

            if(score >= arr[i] && score > 0) {

                onTopList()
                return
            }
        }

        notOnTopList()
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

            backToMenu()
        }.create()

        alert.show()
    }

    /**
     * Player writes name to be added to the top list, and requested to play again.
     */
    private fun onTopList() {

        val input = EditText(this)

        val alert = AlertDialog.Builder(this, R.style.MyDialogTheme)

        alert.setTitle("Enter Name")
        alert.setView(input)
        alert.setMessage("Enter your name for the top list")
        alert.setPositiveButton("Play Again") { dialog, _ ->

            dialog.cancel()
            addScoreToScoreList(input.text.toString())

            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            finish()
        }

        alert.setNegativeButton("Exit") {_, _ ->

            addScoreToScoreList(input.text.toString())
            backToMenu()
        }.create()

        alert.show()
    }

    /**
     * Back to menu with correct values.
     */
    private fun backToMenu() {

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}