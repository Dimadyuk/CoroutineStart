package com.example.coroutinestart

import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.coroutinestart.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.security.auth.callback.Callback
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bindViews()
    }

    private suspend fun loadData() {
        Log.d("MainActivity", "Load started: $this")
        binding.tvLocation.isVisible = true
        binding.tvTemperature.isVisible = true
        binding.progressBar.isVisible = true
        binding.button.isEnabled = false
        val city = loadCity()
        binding.tvLocation.text = city
        val temp = loadTemperature(city)
        binding.tvTemperature.text = temp.toString()
        binding.progressBar.isVisible = false
        binding.button.isEnabled = true
        Log.d("MainActivity", "Load finished: $this")


    }


    private suspend fun loadCity(): String {

        delay(2000)
        return "Minsk"
    }

    private suspend fun loadTemperature(city: String): Int {


        Toast.makeText(
            this,
            "Loading temperature for city: $city",
            Toast.LENGTH_SHORT
        ).show()

        delay(2000)

        return 17

    }

    private fun bindViews() {
        with(binding) {
            tvLocation.isVisible = false
            tvTemperature.isVisible = false
            progressBar.isVisible = false

            button.setOnClickListener {
                lifecycleScope.launch {
                    loadData()
                }
            }
        }
    }
}