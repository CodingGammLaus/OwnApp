package se.umu.cs.dv21sln.ownapplication

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Rect
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
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
    private var points = 10
    private var health = 3
    private var damage = 1
    private var shipSpeed = 4

    private var paused = false


    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

            pause()

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

        startHealth = intent.getIntExtra("health", 3)
        points = intent.getIntExtra("points", 1)
        shipSpeed = intent.getIntExtra("speed", 2)

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
                            dead()
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
                binding.life5.setImageResource(R.drawable.empty_heart)
            }
            3 -> {
                binding.life4.setImageResource(R.drawable.empty_heart)
            }
            2 -> {
                binding.life3.setImageResource(R.drawable.empty_heart)
            }
            1 -> {
                binding.life2.setImageResource(R.drawable.empty_heart)
            }
            0 -> {
                binding.life1.setImageResource(R.drawable.empty_heart)
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
     * When player died
     */
    private fun dead() {

        onStop()
        gameTimer.cancel()
        meteorTimer.cancel()

        val builder = AlertDialog.Builder(this, R.style.MyDialogTheme)

        builder.setTitle("Score")
        builder.setMessage("Score: " + score + "\nDo you want to play again?")

        builder.setPositiveButton("YES") {_, _ ->

            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("health", startHealth)
            intent.putExtra("points", points)
            intent.putExtra("speed", shipSpeed)
            startActivity(intent)
            finish()
        }

        builder.setNegativeButton("NO") {_, _ ->

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("score", score)
            intent.putExtra("health", startHealth)
            intent.putExtra("points", points)
            intent.putExtra("speed", shipSpeed)
            startActivity(intent)
            finish()
        }

        builder.create()
        builder.show()
    }

    /**
     * Pause button init.
     */
    private fun pause() {

        binding.pauseButton.setOnClickListener() {

            onStop()
            paused = true

            val builder = AlertDialog.Builder(this, R.style.MyDialogTheme)

            builder.setTitle("Quit")
            builder.setMessage("Do you want to quit current game?")

            builder.setPositiveButton("YES") {_, _ ->

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            builder.setNegativeButton("NO") {_, _ ->

                closeContextMenu()
                onStart()
                paused = false
            }

            builder.create()
            builder.show()
        }
    }
}