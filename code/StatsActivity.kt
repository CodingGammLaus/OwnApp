package se.umu.cs.dv21sln.ownapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import se.umu.cs.dv21sln.ownapplication.databinding.ActivityStatsBinding

class StatsActivity : AppCompatActivity()  {

    private lateinit var binding: ActivityStatsBinding

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        back()
    }

    /**
     * Back to main screen.
     */
    private fun back() {

        binding.backButton.setOnClickListener() {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}