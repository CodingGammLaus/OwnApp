package se.umu.cs.dv21sln.ownapplication

import android.animation.ObjectAnimator
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import se.umu.cs.dv21sln.ownapplication.databinding.ActivityGameBinding

class GameActivity: AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityGameBinding

    private var sensorManager: SensorManager? = null
    private var gyroSensor: Sensor? = null

    private var screenWidth = 0
    private var screenHeight = 0

    //private val game = Game(this)

    var t = 0
    var k = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDisplaySizeToGame()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        gyroSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        binding.menuTitle.text = "Game!"

        shoot()

        pause()
    }

    /**
     * Sets the display size to the game.
     */
    private fun setDisplaySizeToGame() {

        screenWidth = resources.displayMetrics.widthPixels
        screenHeight = resources.displayMetrics.heightPixels
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

            //binding.menuTitle.text = "Score: " + t

            binding.spaceShip.x -= x
        }

        //Turning Right (ÄNDRA SÅ INTE 280 ÄR KVAR!!!!!!!!)
        else if(x < -0.5 && binding.spaceShip.x < screenWidth - 280) {

            //binding.menuTitle.text = "Score: " + t

            binding.spaceShip.x -= x
        }

        else if(x >= (-0.5) && x <= (0.5)) {

            //binding.menuTitle.text = "Score: " + t
        }

        if(binding.missile.y == binding.metior.y && binding.missile.x == binding.metior.x) {

            binding.menuTitle.text = "Score: " + t++
            binding.missile.setImageResource(0)
        }

        else if(binding.missile.y < 0) {
            binding.missile.setImageResource(0)
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

        binding.missile.x = binding.spaceShip.x
        binding.missile.y = binding.spaceShip.y

        binding.gameLayout.setOnClickListener() {

            binding.missile.setImageResource(R.drawable.missile)

            binding.missile.x = binding.spaceShip.x
            binding.missile.y = binding.spaceShip.y

            ObjectAnimator.ofFloat(binding.missile, "translationY", -1710f).apply {
                duration = 300
                start()
            }
        }
    }

    /**
     * Pause button init.
     */
    private fun pause() {

        binding.pauseButton.setOnClickListener() {

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
            }

            builder.create()
            builder.show()
        }
    }
}