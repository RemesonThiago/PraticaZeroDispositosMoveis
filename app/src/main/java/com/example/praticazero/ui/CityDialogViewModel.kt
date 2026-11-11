package com.example.praticazero.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.praticazero.api.WeatherService
import com.example.praticazero.ui.model.City
import com.google.android.gms.maps.model.LatLng

class CityDialogViewModel( private val service: WeatherService) : ViewModel() {
    var query by mutableStateOf("")
    var suggestions by mutableStateOf<List<City>>(emptyList())
        private set

    fun onQueryChanged(value: String) {
        query = value
        if (value.length < 3) {
            suggestions = emptyList()
        } else {
            service.cancelAll()
            service.search(query) { res ->
                val cities = res?.map {
                    City(it.name!!, location = LatLng(it.lat!!, it.lon!!))
                }
                cities?.let { suggestions = cities.toList() }
            }
        }
    }
}
class CityDialogViewModelFactory(
    private val weatherService: WeatherService) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(CityDialogViewModel::class.java)) {
            return CityDialogViewModel(weatherService) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
