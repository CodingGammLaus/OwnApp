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

        binding.score1.text = sharedPref.getInt("score1", 0).toString()
        binding.score2.text = sharedPref.getInt("score2", 0).toString()
        binding.score3.text = sharedPref.getInt("score3", 0).toString()
        binding.score4.text = sharedPref.getInt("score4", 0).toString()
        binding.score5.text = sharedPref.getInt("score5", 0).toString()

        binding.name1.text = sharedPref.getString("name1", "-")
        binding.name2.text = sharedPref.getString("name2", "-")
        binding.name3.text = sharedPref.getString("name3", "-")
        binding.name4.text = sharedPref.getString("name4", "-")
        binding.name5.text = sharedPref.getString("name5", "-")
    }
}