package se.umu.cs.dv21sln.ownapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import se.umu.cs.dv21sln.ownapplication.databinding.ActivityStatsBinding


class StatsActivity : AppCompatActivity()  {

    private lateinit var binding: ActivityStatsBinding
    private lateinit var statsList: ArrayList<Int>

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Stats"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getStats()
        clearStats()
    }

    /**
     * Setting up the score list.
     */
    private fun getStats() {

        val sharedPref = getSharedPreferences("scoreList", MODE_PRIVATE)
        val score1 = sharedPref.getInt("score1", 0)
        val score2 = sharedPref.getInt("score2", 0)
        val score3 = sharedPref.getInt("score3", 0)
        val score4 = sharedPref.getInt("score4", 0)
        val score5 = sharedPref.getInt("score5", 0)

        binding.score1.text = score1.toString()
        binding.score2.text = score2.toString()
        binding.score3.text = score3.toString()
        binding.score4.text = score4.toString()
        binding.score5.text = score5.toString()
    }

    /**
     * Clear the score list (reset).
     */
    private fun clearStats() {

        binding.resetButton.setOnClickListener {


            val builder = AlertDialog.Builder(this, R.style.MyDialogTheme)

            builder.setTitle("Reset score")
            builder.setMessage("Do you want to reset the score list?")

            builder.setPositiveButton("YES") {_, _ ->

                resetScoreList()
            }

            builder.setNegativeButton("NO") {_, _ ->

                closeContextMenu()
            }

            builder.create()
            builder.show()
        }
    }

    /**
     * Reset the score list.
     */
    private fun resetScoreList() {

        val sharedPref = getSharedPreferences("scoreList", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putInt("score1", 0)
        editor.putInt("score2", 0)
        editor.putInt("score3", 0)
        editor.putInt("score4", 0)
        editor.putInt("score5", 0)
        editor.apply()

        binding.score1.text = "0"
        binding.score2.text = "0"
        binding.score3.text = "0"
        binding.score4.text = "0"
        binding.score5.text = "0"
    }
}