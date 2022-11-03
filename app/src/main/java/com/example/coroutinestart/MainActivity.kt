package com.example.coroutinestart

import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.coroutinestart.databinding.ActivityMainBinding
import kotlinx.coroutines.async
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
        val temp = loadTemperature()

        binding.tvTemperature.text = temp.toString()
        binding.progressBar.isVisible = false
        binding.button.isEnabled = true
        Log.d("MainActivity", "Load finished: $this")
    }

    private fun loadWithoutCoroutine(step: Int = 0, obj: Any? = null) {
        when (step) {
            0 -> {
                Log.d("MainActivity", "Load started: $this")
                binding.tvLocation.isVisible = true
                binding.tvTemperature.isVisible = true
                binding.progressBar.isVisible = true
                binding.button.isEnabled = false
                binding.tvLocation.text = ""
                binding.tvTemperature.text = ""

                loadCityWithoutCoroutine {
                    loadWithoutCoroutine(1, it)
                }

            }
            1 -> {
                val city = obj as String
                binding.tvLocation.text = city
                loadTemperatureWithoutCoroutine(city) {
                    loadWithoutCoroutine(2, it)
                }
            }
            2 -> {
                val temp = obj as Int
                binding.tvTemperature.text = temp.toString()
                binding.progressBar.isVisible = false
                binding.button.isEnabled = true
                Log.d("MainActivity", "Load finished: $this")
            }

        }

    }

    private fun loadCityWithoutCoroutine(callback: (String) -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            callback.invoke("Kiev")
        }, 2000)
    }

    private suspend fun loadCity(): String {

        delay(2000)
        return "Minsk"
    }

    private fun loadTemperatureWithoutCoroutine(city: String, callback: (Int) -> Unit) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(
                this,
                "Loading temperature for city: $city",
                Toast.LENGTH_SHORT
            ).show()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            callback.invoke(17)
        }, 2000)

    }

    private suspend fun loadTemperature(): Int {


//        Toast.makeText(
//            this,
//            "Loading temperature for city: $city",
//            Toast.LENGTH_SHORT
//        ).show()

        delay(2000)

        return 17

    }

    private fun bindViews() {
        with(binding) {
            tvLocation.isVisible = false
            tvTemperature.isVisible = false
            progressBar.isVisible = false

            button.setOnClickListener {
                progressBar.isVisible = true
                button.isEnabled = false
                val deferredCity = lifecycleScope.async {
                    val city = loadCity()
                    city
                }
                val deferredTemp = lifecycleScope.async {
                    val temp = loadTemperature()
                    temp
                }
                lifecycleScope.launch {
                    val city = deferredCity.await()
                    val temp = deferredTemp.await()
                    tvLocation.isVisible = true
                    tvLocation.text = city
                    tvTemperature.isVisible = true
                    tvTemperature.text = temp.toString()

                    Toast.makeText(
                        this@MainActivity,
                        "city: $city - temp: $temp",
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar.isVisible = false
                    button.isEnabled = true
                }
                //loadWithoutCoroutine()
            }
        }
    }
}