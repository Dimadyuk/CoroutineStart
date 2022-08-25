package com.example.coroutinestart

import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.coroutinestart.databinding.ActivityMainBinding
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

    private fun loadData() {
        Log.d("MainActivity", "Load started: $this")
        binding.tvLocation.isVisible = true
        binding.tvTemperature.isVisible = true
        binding.progressBar.isVisible = true
        binding.button.isEnabled = false
        loadCity {
            binding.tvLocation.text = it
            loadTemperature(it) {
                binding.tvTemperature.text = it.toString()
                binding.progressBar.isVisible = false
                binding.button.isEnabled = true
                Log.d("MainActivity", "Load finished: $this")
            }

        }

    }

    private fun loadCity(callback: (String) -> Unit) {
        thread {
            Thread.sleep(2000)
            runOnUiThread {
                callback.invoke("Minsk")
            }
        }
    }

    private fun loadTemperature(city: String, callback: (Int) -> Unit) {

        thread {
            runOnUiThread {
                Toast.makeText(
                    this,
                    "Loading temperature for city: $city",
                    Toast.LENGTH_SHORT
                ).show()

                Thread.sleep(2000)
                runOnUiThread {
                    callback.invoke(17)
                }
            }
        }

    }

    private fun bindViews() {
        with(binding) {
            tvLocation.isVisible = false
            tvTemperature.isVisible = false
            progressBar.isVisible = false

            button.setOnClickListener {
                loadData()
            }
        }
    }
}