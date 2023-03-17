package se.umu.cs.dv21sln.ownapplication

import android.content.Intent
import android.os.Bundle
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
    }
}