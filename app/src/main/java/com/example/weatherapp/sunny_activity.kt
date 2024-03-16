package com.example.weatherapp

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.annotation.DrawableRes
import com.example.weatherapp.databinding.ActivitySplashBinding
import com.example.weatherapp.databinding.ActivitySunnyBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// cb70658e4b847e41fb7bf56432f9f152
class sunny_activity : AppCompatActivity() {
    private val binding:ActivitySunnyBinding by lazy{
        ActivitySunnyBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeatherData("Jaipur")
        SearchCity()
    }

    private fun SearchCity() {
        var searchview= binding.searchView
        searchview.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun fetchWeatherData(cityname:String) {
        val retrofit= Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(APIInterface::class.java)
        val response=retrofit.getweatherData(cityname,"cb70658e4b847e41fb7bf56432f9f152","metric")
        response.enqueue(object: Callback<WeatherApp> {
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody=response.body()
                if(response.isSuccessful && responseBody!=null){
                    val temperature=responseBody.main.temp.toString()
                    binding.temp.text="$temperature °C"
                    val humidity=responseBody.main.humidity
                    val windSpeed=responseBody.wind.speed
                    val sunRise=responseBody.sys.sunrise
                    val sunSet=responseBody.sys.sunset
                    val seaLevel=responseBody.main.pressure
                    val condition=responseBody.weather.firstOrNull()?.main?:"unknown"
                    val maxTemp=responseBody.main.temp_max
                    val minTemp=responseBody.main.temp_min

                    binding.temp.text="$temperature °C"
                    binding.weather.text=condition
                    binding.maxtemp.text="Max Temp: $maxTemp °C"
                    binding.mintemp.text="Min Temp: $minTemp °C"
                    binding.humidity.text="$humidity %"
                    binding.windspeed.text="$windSpeed m/s"
                    binding.sunrise.text="${time(sunRise.toLong())}"
                    binding.sunset.text="${time(sunSet.toLong())}"
                    binding.sea.text="$seaLevel hPa"
                    binding.conditions.text=condition
                    binding.day.text=dayName(System.currentTimeMillis())
                        binding.date.text=date()
                        binding.cityname.text="$cityname"


                    //Log.d("TAG","onResponse: $temperature")

                    changeimage(condition)
                }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                TODO("Not yet implemented")
            }




        })

    }
    private fun changeimage(conditions :String) {
        when(conditions){
            "Partly Clouds","Clouds","Overcast","Mist",",Foggy" ->
            {
                binding.root.setBackgroundResource(R.drawable.tufaan)
                binding.lottieAnimationView.setAnimation(R.raw.cloudy)

            }
            "Clear Sky","Sunny","Clear" ->
            {
                binding.root.setBackgroundResource(R.drawable.sunny)
                binding.lottieAnimationView.setAnimation(R.raw.sunnyanimation)


            }
            "light Rain","Drizzle","Moderate Rain","Showers","Heavy Rain" ->
            {
                binding.root.setBackgroundResource(R.drawable.rain)
                binding.lottieAnimationView.setAnimation(R.raw.raining)

            }
            "Light Snow","Moderate Snow","Heavy Snow","Blizzard" ->
            {
                binding.root.setBackgroundResource(R.drawable.snow)
                binding.lottieAnimationView.setAnimation(R.raw.snowman)

            }
            else ->{
                binding.root.setBackgroundResource(R.drawable.sunny)
                binding.lottieAnimationView.setAnimation(R.raw.sunnyanimation)
            }

        }
        binding.lottieAnimationView.playAnimation()

    }

    private fun date(): String {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return sdf.format((Date()))

    }

    private fun time(timestamp:Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((Date(timestamp*1000)))

    }
    fun dayName(timestamp:Long):String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }
}

