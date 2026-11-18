
package com.example.praticazero

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.derivedStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.praticazero.api.WeatherService
import com.example.praticazero.db.fb.FBCity
import com.example.praticazero.db.fb.FBDatabase
import com.example.praticazero.db.fb.FBUser
import com.example.praticazero.db.fb.toFBCity
import com.example.praticazero.ui.model.City
import com.example.praticazero.ui.model.User
import com.google.android.gms.maps.model.LatLng
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.praticazero.ui.model.Weather
import com.example.praticazero.api.toWeather
import com.example.praticazero.api.APIWeatherForecast
import com.example.praticazero.api.toForecast
import com.example.praticazero.ui.model.Forecast
import androidx.compose.runtime.mutableStateMapOf

class MainViewModel(private val db: FBDatabase, private val service: WeatherService) : ViewModel(), FBDatabase.Listener {

    private val _user = mutableStateOf<User?>(null)
    private val _weather = mutableStateMapOf<String, Weather>()
    private val _forecast = mutableStateMapOf<String, List<Forecast>?>()
    private val _city = mutableStateOf<String?>(null)
    private val _loadingForecast = mutableStateMapOf<String, Boolean>()
    val user: User?
        get() = _user.value
    val city: String?
        get() = _city.value
    fun selectCity(name: String) {
        _city.value = name
    }
        //set(tmp) { _city.value = tmp }

    private val _cities = mutableStateMapOf<String, City>()

    val cities: List<City> by derivedStateOf {
        _cities.values.toList().sortedBy { it.name }
    }

    init {
        db.setListener(this)
    }

    fun remove(city: City) {
        db.remove(city.toFBCity())
    }

    fun add(name: String, location: LatLng? = null) {
        db.add(
            City(
                name = name,
                location = location
            ).toFBCity()
        )
    }

    override fun onUserLoaded(user: FBUser) {
        _user.value = user.toUser()
    }

    override fun onUserSignOut() {
        _user.value = null
    }

    override fun onCityAdded(city: FBCity) {
        _cities[city.name!!] = city.toCity()
    }

    override fun onCityUpdated(city: FBCity) {
        _cities.remove(city.name)
        _cities[city.name!!] = city.toCity()
    }

    override fun onCityRemoved(city: FBCity) {
        _cities.remove(city.name)
    }

    fun addCity(name: String) {
        service.getLocation(name) { lat, lng ->
            if (lat != null && lng != null) {
                db.add(
                    City(
                        name = name,
                        location = LatLng(lat, lng)
                    ).toFBCity()
                )
            }
        }
    }

    fun addCity(location: LatLng) {
        service.getName(location.latitude, location.longitude) { name ->
            if (name != null) {
                db.add(
                    City(
                        name = name,
                        location = location
                    ).toFBCity()
                )
            }
        }
    }

    fun addCity(city: City) {
        db.add(city.toFBCity())
    }

    /*fun weather(name: String) = _weather.getOrPut(name) {
        loadWeather(name)
        Weather.LOADING
    }*/
    fun weather(name: String): Weather {
        return _weather.getOrPut(name) {
            loadWeather(name)
            Weather.LOADING
        }
    }
  
    /*fun forecast(name: String): List<Forecast>? {
        return _forecast[name] ?: run {
            loadForecast(name)
            null
        }
    }*/
    fun forecast(name: String): List<Forecast>? {
        val result = _forecast[name]

        if (result == null && _loadingForecast[name] != true) {
            _loadingForecast[name] = true
            loadForecast(name)
        }

        return result
    }
    /*private fun loadForecast(name: String) {
        service.getForecast(name) { apiForecast ->
            apiForecast?.toForecast()?.let {
                _forecast[name] = it
            }
        }
    }*/
    private fun loadForecast(name: String) {
        service.getForecast(name) { apiForecast ->

            val result = apiForecast?.toForecast()

            if (result != null) {
                _forecast[name] = result
            }

            _loadingForecast[name] = false
        }
    }
    /*private fun loadWeather(name: String) {
        service.getWeather(name) { apiWeather ->
            apiWeather?.let {
                _weather[name] = apiWeather.toWeather()
            }
        }
    }*/
    private fun loadWeather(name: String) {
        service.getWeather(name) { apiWeather ->
            if (apiWeather != null) {
                _weather[name] = apiWeather.toWeather()
            } else {
                _weather[name] = Weather.LOADING
            }
        }
    }

    class MainViewModelFactory(
        private val db: FBDatabase,
        private val service: WeatherService
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(
            modelClass: Class<T>
        ): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(db, service) as T
            }

            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
