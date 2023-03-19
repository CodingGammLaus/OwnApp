package se.umu.cs.dv21sln.ownapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import se.umu.cs.dv21sln.ownapplication.databinding.ActivityTopListBinding


class TopListActivity : AppCompatActivity()  {

    private lateinit var binding: ActivityTopListBinding

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTopListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("background", MODE_PRIVATE)
        binding.main.setBackgroundResource(sharedPref.getInt("pic", R.drawable.space))

        supportActionBar?.title = "Top List"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getScore()
    }

    /**
     * Setting up the score list.
     */
    private fun getScore() {

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
}