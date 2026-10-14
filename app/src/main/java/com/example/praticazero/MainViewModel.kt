package com.example.praticazero

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.example.praticazero.ui.model.City
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.android.gms.maps.model.LatLng

class  MainViewModel : ViewModel() {
    private val _cities: SnapshotStateList<City> =  getCities().toMutableStateList()
    /*private fun getCities(): List<City> {
        )
    }*/

    val cities
    get() = _cities.toList()
    fun remove(city: City) {
        _cities.remove(city)
    }
    fun add(name: String, location: LatLng? = null) {
        _cities.add(City(name = name, location = location))
    }
}
private fun getCities() = List(20) { i ->
    City(name = "Cidade $i", weather = "Carregando clima...")
}
