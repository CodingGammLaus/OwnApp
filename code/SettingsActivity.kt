package se.umu.cs.dv21sln.ownapplication

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import se.umu.cs.dv21sln.ownapplication.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("background", MODE_PRIVATE)
        binding.main.setBackgroundResource(sharedPref.getInt("pic", R.drawable.space))

        supportActionBar?.title = "Settings"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        changeBackground()

        resetTopList()
    }

    /**
     *
     */
    private fun changeBackground() {

        val sharedPref = getSharedPreferences("background", MODE_PRIVATE)
        val editor = sharedPref.edit()

        /*Change to regular space background*/
        binding.regularSpaceBackground.setOnClickListener() {

            binding.main.setBackgroundResource(R.drawable.space)

            editor.putInt("pic", R.drawable.space)
            editor.apply()
        }

        /*Change to color space background*/
        binding.colorSpaceBackground.setOnClickListener() {

            binding.main.setBackgroundResource(R.drawable.color_space)

            editor.putInt("pic", R.drawable.color_space)
            editor.apply()
        }
    }

    /**
     *
     */
    private fun resetTopList() {

        binding.resetStats.setOnClickListener() {

            val builder = AlertDialog.Builder(this, R.style.MyDialogTheme)

            builder.setTitle("Reset score")
            builder.setMessage("Do you want to reset the score list?")

            builder.setPositiveButton("YES") {_, _ ->

                clearTopList()
            }

            builder.setNegativeButton("NO") {_, _ ->

                closeContextMenu()
            }

            builder.create()
            builder.show()
        }
    }

    /**
     *
     */
    private fun clearTopList() {

        val sharedPref = getSharedPreferences("scoreList", MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.putInt("score1", 0)
        editor.putInt("score2", 0)
        editor.putInt("score3", 0)
        editor.putInt("score4", 0)
        editor.putInt("score5", 0)

        editor.putString("name1", "-")
        editor.putString("name2", "-")
        editor.putString("name3", "-")
        editor.putString("name4", "-")
        editor.putString("name5", "-")
        editor.apply()
    }
}